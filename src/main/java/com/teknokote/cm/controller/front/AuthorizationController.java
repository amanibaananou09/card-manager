package com.teknokote.cm.controller.front;

import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.AuthorizationService;
import com.teknokote.cm.core.service.CardService;
import com.teknokote.cm.dto.AuthorizationDto;
import com.teknokote.cm.dto.AuthorizationRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(EndPoints.AUTHORIZATION_ROOT)
public class AuthorizationController
{
   @Autowired
   private AuthorizationService authorizationService;
   @Autowired
   private CardService cardService;


   @PostMapping(EndPoints.ADD)
   public ResponseEntity<AuthorizationDto> addAuthorization(@RequestBody AuthorizationDto dto)
   {
      AuthorizationDto savedAuthorization = authorizationService.create(dto);
      return new ResponseEntity<>(savedAuthorization, HttpStatus.CREATED);
   }
   @PostMapping(EndPoints.FREE_CARD)
   public void freeCard(@RequestParam String authorizationReference,@RequestParam String transactionReference)
   {
      cardService.freeCard(authorizationReference,transactionReference);
   }
   @PostMapping(EndPoints.AUTHORIZE)
   public AuthorizationDto createAuthorization(@RequestBody AuthorizationRequest authorizationRequest)
   {
      AuthorizationDto savedAuthorization = authorizationService.createAuthorization(authorizationRequest);
      return savedAuthorization;
   }

   @PutMapping(EndPoints.UPDATE)
   public ResponseEntity<AuthorizationDto> updateAuthorization(@RequestBody AuthorizationDto dto)
   {
      AuthorizationDto savedAuthorization = authorizationService.update(dto);
      return new ResponseEntity<>(savedAuthorization, HttpStatus.CREATED);
   }

   @GetMapping(EndPoints.INFO)
   public ResponseEntity<AuthorizationDto> getAuthorization(@PathVariable Long id)
   {
      AuthorizationDto foundAuthorization = authorizationService.checkedFindById(id);
      return new ResponseEntity<>(foundAuthorization, HttpStatus.CREATED);
   }

   @GetMapping
   public List<AuthorizationDto> listAuthorization()
   {
      return authorizationService.findAll();
   }
}
