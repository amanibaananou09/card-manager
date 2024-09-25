package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.TransactionDao;
import com.teknokote.cm.core.model.*;
import com.teknokote.cm.core.service.*;
import com.teknokote.cm.dto.*;
import com.teknokote.core.exceptions.ServiceValidationException;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
public class TransactionServiceImpl extends GenericCheckedService<Long, TransactionDto> implements TransactionService {
    @Autowired
    private ESSValidator<TransactionDto> validator;
    @Autowired
    private TransactionDao dao;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CardService cardService;
    @Autowired
    private CardGroupService cardGroupService;
    @Autowired
    private SalePointService salePointService;

    @Override
    public TransactionDto createTransaction(TransactionDto dto) {
        SupplierDto supplierDto = supplierService.findByReference(dto.getReference());
        if (supplierDto != null) {
            ProductDto productDto = productService.findBySupplierAndName(dto.getProductName(), supplierDto.getId());
            if (productDto != null) {
                dto.setProductId(productDto.getId());
            }
            List<SalePointDto> salePoints = supplierDto.getSalePoints().stream().filter(salePointDto -> salePointDto.getName().equals(dto.getSalePointName())).toList();
            if (salePoints != null && !salePoints.isEmpty()) {
                dto.setSalePointId(salePoints.get(0).getId());
            }
        }
        CardDto card = cardService.checkedFindById(dto.getCardId());
        CardGroupDto cardGroupDto = cardGroupService.checkedFindById(card.getCardGroupId());
        CeilingDto ceilingDto = cardGroupDto.getCeilings().stream().findFirst().get();
        BigDecimal availableBalance = calculateAvailableBalance(dto, dto.getCardId(), ceilingDto, cardGroupDto);
        if (ceilingDto.getCeilingType().equals(EnumCeilingType.AMOUNT)) {
            dto.setAvailableBalance(availableBalance);
        } else {
            dto.setAvailableVolume(availableBalance);
        }
        return create(dto);
    }

    @Override
    public Optional<TransactionDto> findLastTransactionByCardId(Long cardId, EnumCeilingLimitType limitType, LocalDateTime dateTime) {
        if (limitType.equals(EnumCeilingLimitType.MONTHLY)) {
            return getDao().findLastTransactionByCardIdAndMonth(cardId, dateTime.getMonthValue());
        } else if (limitType.equals(EnumCeilingLimitType.WEEKLY)) {
            LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
            LocalDateTime endOfWeek = startOfWeek.plusDays(7);
            return getDao().findLastTransactionByCardIdAndWeek(cardId, startOfWeek, endOfWeek);
        } else {
            return getDao().findTodayLastTransactionByCardId(cardId);
        }
    }

    @Override
    public List<TransactionDto> findTodayTransaction(Long cardId) {
        return getDao().findTodayTransaction(cardId);
    }

    private BigDecimal calculateAvailableBalance(TransactionDto transactionDto, Long cardId, CeilingDto ceilingDto, CardGroupDto groupDto) {
        Optional<TransactionDto> lastTransaction = this.findLastTransactionByCardId(cardId, ceilingDto.getLimitType(), LocalDateTime.now());
        BigDecimal valueToSubtract;
        if (lastTransaction.isEmpty()) {
            // First transaction on ceiling limit type
            valueToSubtract = calculateAmountToSubtract(transactionDto, ceilingDto.getCeilingType());
            return ceilingDto.getValue().subtract(valueToSubtract);
        } else {
            // Subsequent transaction within the same ceiling limit type
            TransactionDto transaction = lastTransaction.get();
            valueToSubtract = calculateAmountToSubtract(transactionDto, ceilingDto.getCeilingType());
            if (ceilingDto.getCeilingType().equals(EnumCeilingType.AMOUNT)) {
                return transaction.getAvailableBalance().subtract(valueToSubtract);
            }else {
                return transaction.getAvailableVolume().subtract(valueToSubtract);
            }
        }
    }

    private BigDecimal calculateAmountToSubtract(TransactionDto transactionDto, EnumCeilingType ceilingType) {
        return ceilingType == EnumCeilingType.AMOUNT ? transactionDto.getAmount() : transactionDto.getQuantity();
    }

    @Override
    public Page<Transaction> findTransactionsByFilter(
            Long customerId,
            TransactionFilterDto filterDto,
            int page, int size
    ) {
        Page<Transaction> transactions = getDao().findByCriteria(customerId, filterDto, page, size);
        return transactions;
    }


    @Override
    public TransactionDto mapToTransactionDto(Transaction transaction) {
        TransactionDto.TransactionDtoBuilder builder = TransactionDto.builder()
                .id(transaction.getId())
                .version(transaction.getVersion())
                .amount(transaction.getAmount())
                .price(transaction.getPrice())
                .quantity(transaction.getQuantity())
                .dateTime(transaction.getDateTime());
        if (Objects.nonNull(transaction.getAvailableBalance())) {
            builder.availableBalance(transaction.getAvailableBalance());
        }
        if (Objects.nonNull(transaction.getAvailableVolume())) {
            builder.availableVolume(transaction.getAvailableVolume());
        }

        if (Objects.nonNull(transaction.getCard())) {
            builder.cardId(transaction.getCardId())
                    .cardIdentifier(transaction.getCard().getCardId());
        }

        if (Objects.nonNull(transaction.getAuthorization())) {
            builder.authorizationId(transaction.getAuthorizationId());
        }

        if (Objects.nonNull(transaction.getProduct())) {
            builder.productId(transaction.getProductId())
                    .productName(transaction.getProduct().getName());
        }

        if (Objects.nonNull(transaction.getSalePoint())) {
            SalePointDto salePointDto = mapToSalePointDto(transaction.getSalePoint());
            builder.salePoint(salePointDto)
                    .salePointId(transaction.getSalePointId())
                    .salePointName(transaction.getSalePoint().getName())
                    .city(transaction.getSalePoint().getCity());
        }
        // Calculate SRÉ for each product and add it to the DTO
        Map<String, BigDecimal> remainingBalancePerProduct = calculateRemainingBalance(transaction);
        builder.remainingBalancePerProduct(remainingBalancePerProduct);
        return builder.build();
    }

    private Map<String, BigDecimal> calculateRemainingBalance(Transaction transaction) {
        // Assuming getFuelPrices() gives you a map of product ID to price
        List<ProductDto> productList = productService.findBySupplier(transaction.getSalePoint().getSupplierId());
        Map<String, Double> fuelPrices = productList.stream()
                .collect(Collectors.toMap(ProductDto::getName, ProductDto::getPrice));

        // Initialize the map to store SRÉ values
        Map<String, BigDecimal> remainingBalancePerProduct = new HashMap<>();

        // Get the remaining volume from the transaction (adjust as necessary)
        BigDecimal remainingVolume = transaction.getAvailableVolume() != null ?
                transaction.getAvailableVolume() : BigDecimal.ZERO;

        // Loop through each product price and calculate the SRÉ
        for (Map.Entry<String, Double> entry : fuelPrices.entrySet()) {
            String productName = entry.getKey();
            Double pricePerUnit = entry.getValue();

            // Calculate SRÉ: remaining volume * price per unit
            BigDecimal remainingBalance = remainingVolume.multiply(BigDecimal.valueOf(pricePerUnit));

            // Store the result in the map
            remainingBalancePerProduct.put(productName, remainingBalance);
        }

        return remainingBalancePerProduct;
    }

    private SalePointDto mapToSalePointDto(SalePoint salePoint) {
        CountryDto countryDto = mapToCountryDto(salePoint.getCountry());
        return SalePointDto.builder()
                .id(salePoint.getId())
                .name(salePoint.getName())
                .city(salePoint.getCity())
                .country(countryDto)
                .build();
    }

    private CountryDto mapToCountryDto(Country salePoint) {
        return CountryDto.builder()
                .id(salePoint.getId())
                .name(salePoint.getName())
                .code(salePoint.getCode())
                .build();
    }

    @Override
    public List<DailyTransactionChart> chartTransaction(Long customerId, Long cardId, String period, LocalDateTime startDate, LocalDateTime endDate) {
        if (period != null) {
            if (EnumFilterPeriod.today.toString().equalsIgnoreCase(period)) {
                if (Objects.isNull(cardId)) {
                    return getDao().todayChartTransaction(customerId);
                } else {
                    return getDao().todayChartTransactionWithCardId(customerId, cardId);
                }
            }
            if (EnumFilterPeriod.yesterday.toString().equalsIgnoreCase(period)) {
                LocalDate yesterday = LocalDate.now().minusDays(1);
                LocalDateTime startOfDay = yesterday.atStartOfDay();
                LocalDateTime endOfDay = yesterday.atTime(LocalTime.MAX);
                if (Objects.isNull(cardId)) {
                    return getDao().findTransactionBetween(customerId, startOfDay, endOfDay);
                } else {
                    return getDao().findTransactionBetweenDateWithCardId(customerId, cardId, startOfDay, endOfDay);
                }
            }
            if (EnumFilterPeriod.weekly.toString().equalsIgnoreCase(period)) {
                if (Objects.isNull(cardId)) {
                    return getDao().weeklyChartTransaction(customerId);
                } else {
                    return getDao().weeklyChartTransactionWithCardId(customerId, cardId);
                }
            }
            if (EnumFilterPeriod.monthly.toString().equalsIgnoreCase(period)) {
                if (Objects.isNull(cardId)) {
                    return getDao().monthlyChartTransaction(customerId);
                } else {
                    return getDao().monthlyChartTransactionWithCardId(customerId, cardId);
                }
            }
        } else {
            if (startDate == null || endDate == null) {
                throw new ServiceValidationException("startDate and endDate must not be null");
            }
            if (Objects.isNull(cardId)) {
                return getDao().findTransactionBetween(customerId, startDate, endDate);
            } else {
                return getDao().findTransactionBetweenDateWithCardId(customerId, cardId, startDate, endDate);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<DailyTransactionChart> getDailyTransactionChart(Long customerId, Long cardId, String period, LocalDateTime startDate, LocalDateTime endDate) {
        List<DailyTransactionChart> dailyTransactionCharts = chartTransaction(customerId,cardId,period,startDate,endDate);

        // Group only by cardIdentifier for DailyTransactionChart
        Map<String, DailyTransactionChart> dailyTransactionChartMap = new HashMap<>();

        for (DailyTransactionChart daily : dailyTransactionCharts) {
            String key = daily.getCardIdentifier(); // Grouping by only cardId

            // Create or update the DailyTransactionChart entry
            DailyTransactionChart dailyChart = dailyTransactionChartMap.getOrDefault(
                    key,
                    new DailyTransactionChart(daily.getDate(), null, daily.getCardIdentifier(), BigDecimal.ZERO)
            );

            // Sum the quantities for the cardIdentifier
            dailyChart.setSum(dailyChart.getSum().add(daily.getSum()));
            dailyTransactionChartMap.put(key, dailyChart);
        }

        return new ArrayList<>(dailyTransactionChartMap.values());
    }

    @Override
    public List<TransactionChart> getTransactionChart(Long customerId, Long cardId, String period, LocalDateTime startDate, LocalDateTime endDate) {
        List<DailyTransactionChart> dailyTransactionCharts = chartTransaction(customerId,cardId,period,startDate,endDate);

        // Group by fuelGrade and cardIdentifier for TransactionChart
        Map<String, TransactionChart> transactionChartMap = new HashMap<>();

        for (DailyTransactionChart daily : dailyTransactionCharts) {
            String key = daily.getFuelGrade() + "-" + daily.getCardIdentifier();

            TransactionChart transactionChart = transactionChartMap.getOrDefault(
                    key,
                    new TransactionChart(daily.getFuelGrade(), daily.getCardIdentifier(), BigDecimal.ZERO)
            );

            // Sum the quantities
            transactionChart.setSum(transactionChart.getSum().add(daily.getSum()));
            transactionChartMap.put(key, transactionChart);
        }
        return new ArrayList<>(transactionChartMap.values());
    }
}
