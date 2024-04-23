package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.SupplierService;
import com.teknokote.cm.core.service.UserService;
import com.teknokote.cm.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.USER_ROOT)
public class UserController
{
   @Autowired
   private UserService userService;
   @Autowired
   private SupplierService supplierService;


   @PostMapping(EndPoints.ADD)
   public ResponseEntity<UserDto> addUser(@RequestBody UserDto dto)
   {
      UserDto savedUser = supplierService.createUser(dto);
      return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
   }


   @PutMapping(EndPoints.UPDATE)
   public ResponseEntity<UserDto> updateUser(@RequestBody UserDto dto)
   {
      UserDto savedUser = supplierService.updateUser(dto);
      return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
   }

   @GetMapping(EndPoints.INFO)
   public ResponseEntity<UserDto> getUser(@PathVariable Long id)
   {
      UserDto foundUser = userService.checkedFindById(id);
      return new ResponseEntity<>(foundUser, HttpStatus.CREATED);
   }

   @GetMapping
   public List<UserDto> listUser()
   {
      return userService.findAll();
   }
}
