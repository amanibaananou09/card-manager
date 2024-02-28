package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.TransactionService;
import com.teknokote.cm.dto.TransactionDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.TRANSACTION_ROOT)
public class TransactionController
{
   @Autowired
   private TransactionService transactionService;


   @PostMapping(EndPoints.ADD)
   public ResponseEntity<TransactionDto> addTransaction(@RequestBody TransactionDto dto)
   {
      TransactionDto savedTransaction = transactionService.create(dto);
      return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
   }


   @PutMapping(EndPoints.UPDATE)
   public ResponseEntity<TransactionDto> updateTransaction(@RequestBody TransactionDto dto)
   {
      TransactionDto savedTransaction = transactionService.update(dto);
      return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
   }

   @GetMapping(EndPoints.INFO)
   public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long id)
   {
      TransactionDto foundTransaction = transactionService.checkedFindById(id);
      return new ResponseEntity<>(foundTransaction, HttpStatus.CREATED);
   }

   @GetMapping
   public List<TransactionDto> listTransaction()
   {
      return transactionService.findAll();
   }
}
