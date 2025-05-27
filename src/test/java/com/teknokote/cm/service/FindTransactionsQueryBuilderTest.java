package com.teknokote.cm.service;

import com.teknokote.cm.core.model.Transaction;
import com.teknokote.cm.core.repository.transactions.FindTransactionsQueryBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

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
}