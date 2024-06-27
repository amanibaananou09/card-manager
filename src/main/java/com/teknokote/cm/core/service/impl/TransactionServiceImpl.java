package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.TransactionDao;
import com.teknokote.cm.core.model.EnumCeilingType;
import com.teknokote.cm.core.model.EnumFilterPeriod;
import com.teknokote.cm.core.model.Transaction;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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
        dto.setAvailableBalance(availableBalance);
        return create(dto);
    }

    @Override
    public Optional<TransactionDto> findLastTransactionByCardIdAndMonth(Long cardId, int month) {
        return getDao().findLastTransactionByCardIdAndMonth(cardId, month);
    }

    @Override
    public List<TransactionDto> findTodayTransaction(Long cardId, LocalDateTime dateTime) {
        return getDao().findTodayTransaction(cardId, dateTime);
    }

    private BigDecimal calculateAvailableBalance(TransactionDto transactionDto, Long cardId, CeilingDto ceilingDto, CardGroupDto groupDto) {
        Optional<TransactionDto> lastTransaction = this.findLastTransactionByCardIdAndMonth(cardId, transactionDto.getDateTime().getMonthValue());
        BigDecimal valueToSubtract;
        if (lastTransaction.isEmpty()) {
            // First transaction of the month
            valueToSubtract = calculateAmountToSubtract(transactionDto, ceilingDto.getCeilingType());
            return ceilingDto.getValue().subtract(valueToSubtract);
        } else {
            // Subsequent transaction within the same month
            TransactionDto transaction = lastTransaction.get();
            valueToSubtract = calculateAmountToSubtract(transactionDto, ceilingDto.getCeilingType());
            return transaction.getAvailableBalance().subtract(valueToSubtract);
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
        return getDao().findByCriteria(customerId, filterDto, page, size);
    }

    @Override
    public TransactionDto mapToTransactionDto(Transaction transaction) {
        TransactionDto.TransactionDtoBuilder builder = TransactionDto.builder()
                .id(transaction.getId())
                .version(transaction.getVersion())
                .amount(transaction.getAmount())
                .quantity(transaction.getQuantity())
                .dateTime(transaction.getDateTime())
                .availableBalance(transaction.getAvailableBalance());

        if (Objects.nonNull(transaction.getCard())) {
            builder.cardId(transaction.getCardId())
                    .cardIdentifier(transaction.getCard().getCardId());
        }

        if (Objects.nonNull(transaction.getAuthorization())) {
            builder.authorizationId(transaction.getAuthorizationId());
        }

        if (Objects.nonNull(transaction.getProduct())) {
            builder.productId(transaction.getProductId())
                    .productName(transaction.getProduct().getName())
                    .price(transaction.getProduct().getPrice());
        }

        if (Objects.nonNull(transaction.getSalePoint())) {
            builder.salePointName(transaction.getSalePoint().getName())
                    .salePointId(transaction.getSalePointId())
                    .city(transaction.getSalePoint().getCity());
        }

        return builder.build();
    }

    @Override
    public List<DailyTransactionChart> chartTransaction(Long customerId, Long cardId, String period, LocalDateTime startDate, LocalDateTime endDate) {
        if (period!=null) {
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
        }else{
            if (startDate == null || endDate == null) {
                throw new ServiceValidationException("startDate and endDate must not be null");
            }
            if (Objects.isNull(cardId)){
                return getDao().findTransactionBetween(customerId,startDate,endDate);
            }else {
                return getDao().findTransactionBetweenDateWithCardId(customerId,cardId,startDate,endDate);
            }
        }
        return null;
    }

    @Override
    public List<TransactionChart> getTransactionChart(Long customerId, Long cardId, String period, LocalDateTime startDate, LocalDateTime endDate) {
        List<DailyTransactionChart> dailyTransactionCharts = chartTransaction(customerId, cardId, period, startDate, endDate);

        Map<String, TransactionChart> transactionChartMap = new HashMap<>();
        for (DailyTransactionChart daily : dailyTransactionCharts) {
            String key = daily.getFuelGrade() + "-" + daily.getCardIdentifier();
            TransactionChart transactionChart = transactionChartMap.getOrDefault(key, new TransactionChart(daily.getFuelGrade(), daily.getCardIdentifier(), BigDecimal.ZERO));
            transactionChart.setSum(transactionChart.getSum().add(daily.getSum()));
            transactionChartMap.put(key, transactionChart);
        }

        return new ArrayList<>(transactionChartMap.values());
    }


}
