package com.teknokote.cm.core.dao.impl;

import com.teknokote.cm.core.dao.TransactionDao;
import com.teknokote.cm.core.dao.mappers.TransactionMapper;
import com.teknokote.cm.core.model.Transaction;
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
}
