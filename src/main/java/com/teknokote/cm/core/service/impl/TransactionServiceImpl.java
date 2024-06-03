package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.TransactionDao;
import com.teknokote.cm.core.model.Transaction;
import com.teknokote.cm.core.service.*;
import com.teknokote.cm.dto.*;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Getter
public class TransactionServiceImpl extends GenericCheckedService<Long, TransactionDto> implements TransactionService
{
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
      SupplierDto supplierDto= supplierService.findByReference(dto.getReference());
      if (supplierDto!=null){
         ProductDto productDto = productService.findBySupplierAndName(dto.getProductName(),supplierDto.getId());
         if (productDto!=null){
            dto.setProductId(productDto.getId());
         }
         SalePointDto salePoint = supplierDto.getSalePoints().stream().filter(salePointDto -> salePointDto.getName().equals(dto.getSalePointName())).toList().get(0);
         if (salePoint!=null){
            dto.setSalePointId(salePoint.getId());
         }
      }
      CardDto card = cardService.checkedFindById(dto.getCardId());
      CardGroupDto cardGroupDto= cardGroupService.checkedFindById(card.getCardGroupId());
      BigDecimal ceilingValue = cardGroupDto.getCeilings().stream().findFirst().get().getValue();
      BigDecimal availableBalance = calculateAvailableBalance(dto,dto.getCardId(),ceilingValue);
      dto.setAvailableBalance(availableBalance);
      return create(dto);
   }

   @Override
   public Optional<TransactionDto> findLastTransactionByCardIdAndMonth(Long cardId, int month) {
      return getDao().findLastTransactionByCardIdAndMonth(cardId,month);
   }

   @Override
   public List<TransactionDto> findTodayTransaction(Long cardId, LocalDateTime dateTime) {
      return getDao().findTodayTransaction(cardId,dateTime);
   }

   private BigDecimal calculateAvailableBalance(TransactionDto transactionDto, Long cardId, BigDecimal ceilingValue) {
      Optional<TransactionDto> lastTransaction = this.findLastTransactionByCardIdAndMonth(
              cardId, transactionDto.getDateTime().getMonthValue()
      );
      if (lastTransaction.isEmpty()) {
         // First transaction of the month
         return ceilingValue.multiply(BigDecimal.valueOf(1000)).subtract(transactionDto.getAmount());
      } else {
         // Subsequent transaction within the same month
         TransactionDto transaction = lastTransaction.get();
         return transaction.getAvailableBalance().subtract(transactionDto.getAmount());
      }
   }
   @Override
   public Page<Transaction> findTransactionsByFilter(
           Long customerId,
           TransactionFilterDto filterDto,
           int page, int size
   ) {
      return getDao().findByCriteria(customerId,filterDto, page, size);
   }

   @Override
   public TransactionDto mapToTransactionDto(Transaction transaction) {
      TransactionDto transactionDto = new TransactionDto(transaction.getId(),transaction.getVersion());
      transactionDto.setAmount(transaction.getAmount());
      transactionDto.setCardId(transaction.getCardId());
      transactionDto.setQuantity(transaction.getQuantity());
      transactionDto.setProductId(transaction.getProductId());
      if (Objects.nonNull(transaction.getAuthorization())){
         transactionDto.setAuthorizationId(transaction.getAuthorizationId());
      }
      transactionDto.setDateTime(transaction.getDateTime());
      transactionDto.setAvailableBalance(transaction.getAvailableBalance());
      if (Objects.nonNull(transaction.getProduct())){
         transactionDto.setProductName(transaction.getProduct().getName());
      }
      if (Objects.nonNull(transaction.getSalePoint())){
         transactionDto.setSalePointName(transaction.getSalePoint().getName());
         transactionDto.setSalePointId(transaction.getSalePointId());
         transactionDto.setCity(transaction.getSalePoint().getCity());
      }

      return transactionDto;
   }
}
