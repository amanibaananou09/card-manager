package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.CustomerService;
import com.teknokote.cm.dto.CustomerDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.CUSTOMER_ROOT)
public class CustomerController
{
   @Autowired
   private CustomerService customerService;


   @PostMapping(EndPoints.ADD)
   public ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerDto dto)
   {
      CustomerDto savedCustomer = customerService.create(dto);
      return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
   }


   @PutMapping(EndPoints.UPDATE)
   public ResponseEntity<CustomerDto> updateCustomer(@RequestBody CustomerDto dto)
   {
      CustomerDto savedCustomer = customerService.update(dto);
      return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
   }

   @GetMapping(EndPoints.INFO)
   public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id)
   {
      CustomerDto foundCustomer = customerService.checkedFindById(id);
      return new ResponseEntity<>(foundCustomer, HttpStatus.CREATED);
   }

   @GetMapping
   public List<CustomerDto> listCustomer()
   {
      return customerService.findAll();
   }
}
