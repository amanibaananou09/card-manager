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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

   @Override
   public Optional<TransactionDto> findLastTransactionByCardIdAndMonth(Long cardId, int month) {
      return Optional.ofNullable(getMapper().toDto(getRepository().findLastTransactionByCardIdAndMonth(cardId,month).orElse(null)));

   }

   @Override
   public List<TransactionDto> findTodayTransaction(Long cardId, LocalDateTime dateTime) {
      return getRepository().findAllByCardIdAndDateTimeBefore(cardId,dateTime).stream().map(getMapper()::toDto).collect(Collectors.toList());
   }
}
