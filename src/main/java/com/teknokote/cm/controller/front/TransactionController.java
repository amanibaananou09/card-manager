package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.TransactionService;
import com.teknokote.cm.dto.DailyTransactionChart;
import com.teknokote.cm.dto.TransactionChart;
import com.teknokote.cm.dto.TransactionDto;
import com.teknokote.cm.dto.TransactionFilterDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.TRANSACTION_ROOT)
public class TransactionController {
    @Autowired
    private TransactionService transactionService;


    @PostMapping(EndPoints.ADD)
    public ResponseEntity<TransactionDto> addTransaction(@RequestBody TransactionDto dto) {
        TransactionDto savedTransaction = transactionService.createTransaction(dto);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }


    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<TransactionDto> updateTransaction(@RequestBody TransactionDto dto) {
        TransactionDto savedTransaction = transactionService.update(dto);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }

    @GetMapping(EndPoints.INFO)
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long id) {
        TransactionDto foundTransaction = transactionService.checkedFindById(id);
        return new ResponseEntity<>(foundTransaction, HttpStatus.CREATED);
    }

    @PostMapping
    public Page<TransactionDto> listTransaction(@RequestBody TransactionFilterDto filterDto,
                                                @RequestParam Long customerId,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "50") int size) {
        Page<TransactionDto> transactionDtoPage = transactionService.findTransactionsByFilter(customerId, filterDto, page, size)
                .map(transactionService::mapToTransactionDto);
        return transactionDtoPage;
    }

    @GetMapping(EndPoints.DAILY_CHART)
    public List<DailyTransactionChart> allCardChartTransaction(
            @RequestParam Long customerId,
            @RequestParam(required = false) Long cardId,
            @RequestParam(required = false)  String period,
            @RequestParam(required = false)String startDate,
            @RequestParam(required = false) String endDate)
    {
        LocalDateTime realStartDate = null;
        LocalDateTime realEndDate = null;
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            realStartDate = LocalDateTime.parse(startDate);
            realEndDate = LocalDateTime.parse(endDate);
        }
        return transactionService.chartTransaction(customerId,cardId,period,realStartDate,realEndDate);
    }

    @GetMapping(EndPoints.CHART)
    public List<TransactionChart> chartTransactionWithPeriod(
            @RequestParam Long customerId,
            @RequestParam(required = false) Long cardId,
            @RequestParam(required = false)  String period,
            @RequestParam(required = false)String startDate,
            @RequestParam(required = false) String endDate)
    {
        LocalDateTime realStartDate = null;
        LocalDateTime realEndDate = null;
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            realStartDate = LocalDateTime.parse(startDate);
            realEndDate = LocalDateTime.parse(endDate);
        }
        return transactionService.getTransactionChart(customerId,cardId,period,realStartDate,realEndDate);
    }
}
