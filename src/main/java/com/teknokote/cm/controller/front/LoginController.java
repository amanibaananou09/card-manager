package com.teknokote.cm.controller.front;

import com.teknokote.cm.authentification.model.LoginRequest;
import com.teknokote.cm.authentification.model.LoginResponse;
import com.teknokote.cm.controller.EndPoints;
import com.teknokote.cm.core.service.authentication.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
@CrossOrigin("*")
@Slf4j
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping(EndPoints.LOGIN)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginrequest) {
        return new ResponseEntity<>(loginService.login(loginrequest, true), HttpStatus.OK);
    }

}
