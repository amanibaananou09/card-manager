package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.CustomerService;
import com.teknokote.cm.core.service.UserService;
import com.teknokote.cm.dto.CustomerDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.CUSTOMER_ROOT)
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;

    @PostMapping(EndPoints.ADD)
    public ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerDto dto) {
        CustomerDto savedCustomer = customerService.addCustomer(dto);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }


    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<CustomerDto> updateCustomer(@RequestBody CustomerDto dto) {
        CustomerDto savedCustomer = customerService.update(dto);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @GetMapping(EndPoints.INFO_OLD)
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        CustomerDto foundCustomer = customerService.checkedFindById(id);
        return new ResponseEntity<>(foundCustomer, HttpStatus.CREATED);
    }

    @GetMapping(EndPoints.LIST_BY_ACTIF)
    public List<CustomerDto> listCustomerByActif(@RequestParam boolean actif) {
        return customerService.findAllByActif(actif);
    }

    @GetMapping
    public List<CustomerDto> listCustomerBySupplier(@PathVariable Long supplierId) {
        return customerService.findCustomerBySupplier(supplierId);
    }

    @PostMapping(EndPoints.DEACTIVATE)
    public ResponseEntity<CustomerDto> deactivateCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.deactivate(id));
    }

    @PostMapping(EndPoints.ACTIVATE)
    public ResponseEntity<CustomerDto> activateCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.activate(id));
    }

    @GetMapping(EndPoints.LIST_BY_FILTER)
    public List<CustomerDto> listCustomerByFilter(@RequestParam(required = false) String identifier) {
        return customerService.findCustomerByFilter(identifier);
    }

    @GetMapping(EndPoints.GET_BY_IDENTIFIER)
    public ResponseEntity<?> getByIdentifier(@PathVariable String identifier) {
        Optional<CustomerDto> customer = customerService.findByIdentifier(identifier);

        if (customer.isPresent()) {
            List<String> suggestions = customerService.generateIdentiferSuggestions(identifier);

            return ResponseEntity.ok(suggestions);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
