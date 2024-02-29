package com.teknokote.cm.authentification.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {

    private String username;
    private String password;
    public static LoginRequest init(String username,String password){
        return new LoginRequest(username,password);
    }
}
