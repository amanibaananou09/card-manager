package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.AccountService;
import com.teknokote.cm.dto.AccountDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.ACCOUNT_ROOT)
public class AccountController {
@Autowired
private AccountService accountService;


@PostMapping(EndPoints.ADD)
public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto dto) {
    AccountDto savedAccount = accountService.create(dto);
    return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }


    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<AccountDto> updateAccount(@RequestBody AccountDto dto) {
        AccountDto savedAccount = accountService.update(dto);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.INFO)
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long id)
    {
        AccountDto foundAccount = accountService.checkedFindById(id);
        return new ResponseEntity<>(foundAccount, HttpStatus.CREATED);
    }
    @GetMapping(EndPoints.LIST_BY_ACTIF)
    public List<AccountDto> listAccountByActif(@PathVariable boolean actif) {
         return accountService.findAllByActif(actif);
    }


    @PostMapping(EndPoints.DEACTIVATE)
    public ResponseEntity<AccountDto> deactivateAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.deactivate(id));
    }

    @PostMapping(EndPoints.ACTIVATE)
    public ResponseEntity<AccountDto> activateAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.activate(id));
    }
}
