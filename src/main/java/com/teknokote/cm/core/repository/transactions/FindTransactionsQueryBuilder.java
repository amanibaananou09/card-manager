package com.teknokote.cm.core.repository.transactions;

import com.teknokote.cm.core.model.Transaction;
import com.teknokote.cm.dto.PeriodFilterDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FindTransactionsQueryBuilder
{
    private static final String INIT_SELECT = "SELECT t FROM Transaction t ";
    private static final String INIT_SELECT_COUNT = "SELECT count(t) FROM Transaction t ";

    private EntityManager entityManager;
    private Pageable pageable;
    private StringBuilder joins = new StringBuilder("");
    private StringBuilder whereClauses = new StringBuilder(" where 1=1 ");
    private Map<String, Object> parameters = new HashMap<>();


    private FindTransactionsQueryBuilder(EntityManager entityManager, Pageable pageable)
    {
        this.entityManager = entityManager;
        this.pageable = pageable;
    }

    public static FindTransactionsQueryBuilder init(EntityManager entityManager, Pageable pageable)
    {
        return new FindTransactionsQueryBuilder(entityManager, pageable);
    }

    public String prepare()
    {
        return INIT_SELECT + joins + whereClauses;
    }
    public String prepareCount()
    {
        return INIT_SELECT_COUNT + joins + whereClauses;
    }

    public Page<Transaction> getResults()
    {
        StringBuilder queryBuilder = new StringBuilder(prepare());
        addDefaultSorting(queryBuilder);
        final TypedQuery<Transaction> jQuery = entityManager.createQuery(queryBuilder.toString(), Transaction.class);
        parameters.forEach(jQuery::setParameter);
        jQuery.setFirstResult((int) pageable.getOffset());
        jQuery.setMaxResults(pageable.getPageSize());
        final List<Transaction> resultList = jQuery.getResultList();
        // Count
        final TypedQuery<Long> jQueryCount = entityManager.createQuery(this.prepareCount(), Long.class);
        parameters.forEach(jQueryCount::setParameter);
        Long total= jQueryCount.getSingleResult();
        return new PageImpl<>(resultList, pageable, total);
    }
    private void addDefaultSorting(StringBuilder queryBuilder) {
        // Add sorting by dateTimeStart in descending order
        queryBuilder.append(" ORDER BY t.dateTime DESC");
    }


    public FindTransactionsQueryBuilder addCustomerIdClause(Long customerId)
    {
        joins.append(" join t.card c ")
                .append(" join c.cardGroup cp  ");
        whereClauses.append(" and cp.customer.id= :customerId");
        parameters.put("customerId", customerId);
        return this;
    }

    public FindTransactionsQueryBuilder addCardClause(List<Long> cardIds)
    {
        if(Objects.isNull(cardIds)) return this;
        whereClauses.append(" and t.card.id in (:cardIds)");
        parameters.put("cardIds", cardIds);
        return this;
    }

    public FindTransactionsQueryBuilder addSalePointClause(List<Long> salePointIds)
    {
        if(Objects.isNull(salePointIds)) return this;
        whereClauses.append(" and t.salePoint.id in (:salePointIds)");
        parameters.put("salePointIds", salePointIds);
        return this;
    }

    public FindTransactionsQueryBuilder addProductClause(List<Long> productIds)
    {
        if(CollectionUtils.isEmpty(productIds)) return this;
        whereClauses.append(" and t.product.id in (:productIds)");
        parameters.put("productIds", productIds);
        return this;
    }

    public FindTransactionsQueryBuilder addDateTimeStartBetweenClause(PeriodFilterDto period)
    {
        if(Objects.isNull(period)) return this;

        if(!Objects.isNull(period.getFrom())){
            whereClauses.append(" AND t.dateTime >= :periodFrom");
            parameters.put("periodFrom",period.getFrom());
        }

        if(!Objects.isNull(period.getTo())){
            whereClauses.append(" AND t.dateTime <= :periodTo");
            parameters.put("periodTo",period.getTo());
        }

        return this;
    }


    public FindTransactionsQueryBuilder addCityClause(List<String> city) {
        if (Objects.isNull(city)) return this;
        whereClauses.append(" and t.salePoint.city in (:city)");
        parameters.put("city", city);
        return this;
    }
}

