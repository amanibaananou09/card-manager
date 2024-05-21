package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.TransactionDao;
import com.teknokote.cm.core.dao.mappers.TransactionMapper;
import com.teknokote.cm.core.model.*;
import com.teknokote.cm.core.repository.TransactionRepository;
import com.teknokote.cm.dto.TransactionDto;
import com.teknokote.core.dao.JpaGenericDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class TransactionDaoImpl extends JpaGenericDao<Long, TransactionDto, Transaction> implements TransactionDao
{
   @Autowired
   private TransactionMapper mapper;
   @Autowired
   private TransactionRepository repository;
   @Override
   protected Transaction beforeCreate(Transaction transaction, TransactionDto dto) {
      transaction.setCard(getEntityManager().getReference(Card.class,dto.getCardId()));
      transaction.setAuthorization(getEntityManager().getReference(Authorization.class, dto.getAuthorizationId()));
      transaction.setProduct(getEntityManager().getReference(Product.class,dto.getProductId()));
      return super.beforeCreate(transaction, dto);
   }
   @Override
   protected Transaction beforeUpdate(Transaction transaction, TransactionDto dto) {
      transaction.setCard(getEntityManager().getReference(Card.class,dto.getCardId()));
      transaction.setAuthorization(getEntityManager().getReference(Authorization.class, dto.getAuthorizationId()));
      transaction.setProduct(getEntityManager().getReference(Product.class,dto.getProductId()));
      return super.beforeUpdate(transaction, dto);
   }
}
