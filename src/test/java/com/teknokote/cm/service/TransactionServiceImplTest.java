package com.teknokote.cm.service;

import com.teknokote.cm.core.dao.TransactionDao;
import com.teknokote.cm.core.model.*;
import com.teknokote.cm.core.service.impl.TransactionServiceImpl;
import com.teknokote.cm.core.service.interfaces.*;
import com.teknokote.cm.dto.*;
import com.teknokote.core.exceptions.ServiceValidationException;
import com.teknokote.core.service.ESSValidationResult;
import com.teknokote.core.service.ESSValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionDao transactionDao;

    @Mock
    private SupplierService supplierService;

    @Mock
    private ProductService productService;

    @Mock
    private CardService cardService;

    @Mock
    private CardGroupService cardGroupService;

    @Mock
    private SalePointService salePointService;

    @Mock
    private ESSValidator<TransactionDto> validator;

    private TransactionDto transactionDto;
    private CardDto cardDto;

    @BeforeEach
    void setUp() {
        transactionDto = TransactionDto.builder().build();
        transactionDto.setCardId(1L);
        transactionDto.setProductName("Test Product");
        transactionDto.setReference("Test Supplier");
        transactionDto.setSalePointName("Test SalePoint");
        transactionDto.setAmount(new BigDecimal("100.00"));
        transactionDto.setQuantity(new BigDecimal("10"));
    }

    @Test
    void createTransaction_Success() {
        // Arrange
        SupplierDto supplierDto = SupplierDto.builder().build();
        supplierDto.setId(1L);
        supplierDto.setSalePoints(Collections.singleton(SalePointDto.builder().name("Test SalePoint").city("Test City").build()));

        ProductDto productDto = ProductDto.builder().build();
        productDto.setId(1L);
        productDto.setName("Test Product");

        ESSValidationResult validationResult = new ESSValidationResult();
        validationResult.hasErrors();

        when(validator.validateOnCreate(transactionDto)).thenReturn(validationResult);

        when(supplierService.findByReference(transactionDto.getReference())).thenReturn(supplierDto);
        when(productService.findBySupplierAndName(transactionDto.getProductName(), supplierDto.getId())).thenReturn(productDto);
        when(cardService.checkedFindById(anyLong())).thenReturn(cardDto);
        when(transactionDao.create(any())).thenReturn(TransactionDto.builder().build()); // You might want to adjust this

        // Act
        TransactionDto result = transactionService.createTransaction(transactionDto);

        // Assert
        assertNotNull(result);
        verify(transactionDao).create(any());
    }

    @Test
    void createTransaction_SupplierNotFound() {
        // Arrange
        when(supplierService.findByReference(transactionDto.getReference())).thenReturn(null);

        ESSValidationResult validationResult = new ESSValidationResult();
        validationResult.hasErrors();

        when(validator.validateOnCreate(transactionDto)).thenReturn(validationResult);

        // Act
        TransactionDto result = transactionService.createTransaction(transactionDto);

        // Assert
        assertNull(result);
    }

    @Test
    void findLastTransactionByCardId_Success() {
        // Arrange
        Long cardId = 1L;
        EnumCeilingLimitType limitType = EnumCeilingLimitType.MONTHLY;
        LocalDateTime dateTime = LocalDateTime.now();
        TransactionDto transaction = TransactionDto.builder().build();
        transaction.setCardId(cardId);

        when(transactionDao.findLastTransactionByCardIdAndMonth(cardId, dateTime.getMonthValue())).thenReturn(Optional.of(transaction));

        // Act
        Optional<TransactionDto> result = transactionService.findLastTransactionByCardId(cardId, limitType, dateTime);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(cardId, result.get().getCardId());
    }

    @Test
    void chartTransaction_ThrowServiceValidationException() {
        // Arrange
        Long customerId = 1L;
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        // Act & Assert
        assertThrows(ServiceValidationException.class, () -> {
            transactionService.chartTransaction(customerId, null, null, startDate, endDate);
        });
    }

    @Test
    void getTransactionChart_Success() {
        // Arrange
        Long customerId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(7); // Start date set to one week ago
        LocalDateTime endDate = LocalDateTime.now(); // End date set to now
        DailyTransactionChart dailyChart = new DailyTransactionChart();
        dailyChart.setSum(BigDecimal.ZERO); // Ensure sum is initialized
        List<DailyTransactionChart> dailyTransactionCharts = Collections.singletonList(dailyChart);

        // Mock the charting method to return a predefined list of daily transaction charts
        when(transactionService.chartTransaction(customerId, null, null, startDate, endDate))
                .thenReturn(dailyTransactionCharts);

        // Act
        List<TransactionChart> result = transactionService.getTransactionChart(customerId, null, null, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findTransactionsByFilter_Success() {
        // Arrange
        Long customerId = 1L;
        TransactionFilterDto filterDto = new TransactionFilterDto(); // Set up filters as necessary
        int page = 0;
        int size = 10;

        // Mocking a returned Page object
        Page<Transaction> mockPage = mock(Page.class);
        when(transactionDao.findByCriteria(customerId, filterDto, page, size)).thenReturn(mockPage);

        // Act
        Page<Transaction> result = transactionService.findTransactionsByFilter(customerId, filterDto, page, size);

        // Assert
        assertNotNull(result);
        verify(transactionDao).findByCriteria(customerId, filterDto, page, size);
    }

    @Test
    void mapToTransactionDto_Success() {
        // Arrange
        SalePoint salePoint = new SalePoint();
        salePoint.setId(1L);
        salePoint.setName("Test SalePoint");
        salePoint.setCity("Test City");

        Country country = new Country();
        country.setId(1L);
        country.setName("Test Country");
        country.setCode("TC");

        salePoint.setCountry(country); // Set the Country object to SalePoint

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setVersion(1L);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setPrice(10.00);
        transaction.setQuantity(new BigDecimal("10"));
        transaction.setDateTime(LocalDateTime.now());
        transaction.setAvailableBalance(new BigDecimal("50.00"));
        transaction.setAvailableVolume(new BigDecimal("5"));

        Card card = new Card();
        card.setCardId("Card123");
        transaction.setCard(card);

        Product product = new Product();
        product.setName("Test Product");
        transaction.setProduct(product);

        transaction.setSalePoint(salePoint); // Set the SalePoint object to Transaction

        // Act
        TransactionDto result = transactionService.mapToTransactionDto(transaction);

        // Assert
        assertNotNull(result);
        assertEquals(transaction.getId(), result.getId());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getAvailableBalance(), result.getAvailableBalance());
        assertEquals(transaction.getProduct().getName(), result.getProductName());
        assertEquals(transaction.getSalePoint().getName(), result.getSalePointName());
        assertEquals(transaction.getSalePoint().getCity(), result.getCity()); // Optional, based on your DTO structure
    }

    @Test
    void mapToSalePointDto_Success() {
        // Arrange
        SalePoint salePoint = new SalePoint();
        salePoint.setId(1L);
        salePoint.setName("Test SalePoint");
        salePoint.setCity("Test City");
        Country country = new Country();
        country.setId(1L);
        country.setName("Test Country");
        salePoint.setCountry(country);

        // Act
        SalePointDto result = transactionService.mapToSalePointDto(salePoint);

        // Assert
        assertNotNull(result);
        assertEquals(salePoint.getId(), result.getId());
        assertEquals(salePoint.getName(), result.getName());
        assertEquals(salePoint.getCity(), result.getCity());
        assertEquals(country.getName(), result.getCountry().getName());
    }

    @Test
    void getDailyTransactionChart_Success() {
        // Arrange
        Long customerId = 1L;
        Long cardId = 1L;
        String period = null;
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        DailyTransactionChart dailyChart = new DailyTransactionChart();
        dailyChart.setSum(BigDecimal.ZERO);
        List<DailyTransactionChart> mockCharts = Collections.singletonList(dailyChart);

        when(transactionService.chartTransaction(customerId, cardId, period, startDate, endDate)).thenReturn(mockCharts);

        // Act
        List<DailyTransactionChart> result = transactionService.getDailyTransactionChart(customerId, cardId, period, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

}