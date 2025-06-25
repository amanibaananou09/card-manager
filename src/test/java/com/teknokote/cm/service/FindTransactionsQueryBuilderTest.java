package com.teknokote.cm.service;

import com.teknokote.cm.core.model.Transaction;
import com.teknokote.cm.core.repository.transactions.FindTransactionsQueryBuilder;
import com.teknokote.cm.dto.PeriodFilterDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindTransactionsQueryBuilderTest {

    private EntityManager entityManager;
    private TypedQuery<Transaction> mockQuery;
    private TypedQuery<Long> mockCountQuery;
    @BeforeEach
    public void setUp() {
        entityManager = mock(EntityManager.class);
        mockQuery = mock(TypedQuery.class);
        mockCountQuery = mock(TypedQuery.class);

        when(entityManager.createQuery(anyString(), eq(Transaction.class))).thenReturn(mockQuery);
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(mockCountQuery);

        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockCountQuery.setParameter(anyString(), any())).thenReturn(mockCountQuery);

        when(mockQuery.getResultList()).thenReturn(Collections.emptyList());
        when(mockCountQuery.getSingleResult()).thenReturn(0L);
    }
    @Test
    public void testBuildQueryWithNullAndEmptyFilters() {
        Pageable pageable = PageRequest.of(0, 5);
        FindTransactionsQueryBuilder builder = FindTransactionsQueryBuilder.init(entityManager, pageable);

        // Pass null filters
        builder
                .addCustomerIdClause(null)
                .addCardClause(null)
                .addSalePointClause(null)
                .addProductClause(Collections.emptyList())
                .addCityClause(null)
                .addDateTimeStartBetweenClause(null);

        String queryString = builder.prepare();

        // Verify that the query contains only base and order by clauses
        assertTrue(queryString.contains("FROM Transaction t"));
        assertTrue(queryString.contains("where 1=1"));

        // Execute getResults
        Page<Transaction> page = builder.getResults();

        // Verify total count is 0 since no filters
        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
    }
    @Test
    public void testBuildQueryWithMultipleFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        FindTransactionsQueryBuilder builder = FindTransactionsQueryBuilder.init(entityManager, pageable);

        // Sample filters
        List<Long> cardIds = Arrays.asList(1L, 2L);
        List<Long> salePointIds = Arrays.asList(3L);
        List<Long> productIds = Arrays.asList(4L, 5L);
        List<String> cities = Arrays.asList("New York", "Los Angeles");
        PeriodFilterDto period = new PeriodFilterDto(LocalDateTime.parse("2023-01-01T00:00:00"),LocalDateTime.parse("2023-01-31T23:59:59"));

        // Build the query
        builder
                .addCustomerIdClause(123L)
                .addCardClause(cardIds)
                .addSalePointClause(salePointIds)
                .addProductClause(productIds)
                .addCityClause(cities)
                .addDateTimeStartBetweenClause(period);

        // Prepare the query string
        String queryString = builder.prepare();

        // Verify that the query string contains expected clauses
        assertTrue(queryString.contains("FROM Transaction t"));
        assertTrue(queryString.contains("join t.card c"));
        assertTrue(queryString.contains("join c.cardGroup cp"));
        assertTrue(queryString.contains("where 1=1"));

        // Get the page results
        Page<Transaction> page = builder.getResults();

        // Verify that parameters are set correctly
        verify(mockQuery).setParameter("customerId", 123L);
        verify(mockQuery).setParameter("cardIds", cardIds);
        verify(mockQuery).setParameter("salePointIds", salePointIds);
        verify(mockQuery).setParameter("productIds", productIds);
        verify(mockQuery).setParameter("city", cities);

        // Verify total count query
        verify(mockCountQuery).setParameter("customerId", 123L);
        verify(mockCountQuery).setParameter("cardIds", cardIds);
        verify(mockCountQuery).setParameter("salePointIds", salePointIds);
        verify(mockCountQuery).setParameter("productIds", productIds);
        verify(mockCountQuery).setParameter("city", cities);

        // Since getResultList() returns empty list, verify that
        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
        assertEquals(0, page.getContent().size());
    }
}