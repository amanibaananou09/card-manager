package com.teknokote.cm.authentification.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LoginResponse {
    @JsonProperty(value = "access_token")
    private String accessToken;
    @JsonProperty(value = "refresh_token")
    private String refreshToken;
    @JsonProperty(value = "expires_in")
    private String expiresIn;
    @JsonProperty(value = "refresh_expires_in")
    private String refreshExpiresIn;
    @JsonProperty(value = "token_type")
    private String tokenType;
    @JsonProperty(value = "customer_account_id")
    private Long customerAccountId;
}


