package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.SupplierService;
import com.teknokote.cm.core.service.UserService;
import com.teknokote.cm.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.USER_ROOT)
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SupplierService supplierService;


    @PostMapping(EndPoints.ADD)
    public ResponseEntity<UserDto> addUser(@PathVariable Long supplierId, @RequestBody UserDto dto) {
        UserDto savedUser = supplierService.createUser(supplierId, dto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }


    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<UserDto> updateUser(@PathVariable Long supplierId, @RequestBody UserDto dto) {
        UserDto savedUser = supplierService.updateUser(supplierId, dto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping(EndPoints.INFO)
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto foundUser = userService.checkedFindById(id);
        return new ResponseEntity<>(foundUser, HttpStatus.CREATED);
    }

    @GetMapping
    public List<UserDto> listUser(@PathVariable Long supplierId) {
        return userService.findBySupplier(supplierId);
    }

    @GetMapping(EndPoints.PAGE_USER_BY_SUPPLIER)
    public Page<UserDto> listUser(@PathVariable Long supplierId,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "25") int size) {
        List<UserDto> userList = userService.findBySupplier(supplierId);
        int start = page * size;
        int end = Math.min((start + size), userList.size());
        return new PageImpl<>(userList.subList(start, end), PageRequest.of(page, size), userList.size());
    }
}
