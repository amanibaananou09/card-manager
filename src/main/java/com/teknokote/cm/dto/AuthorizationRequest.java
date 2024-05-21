package com.teknokote.cm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizationRequest {
    private String reference;
    private String tag;
    private String productName;
    private String salePointName;
    @Builder
    public AuthorizationRequest(String reference, String tag, String productName, String salePointName) {
        this.reference = reference;
        this.tag = tag;
        this.productName = productName;
        this.salePointName = salePointName;
    }
}
