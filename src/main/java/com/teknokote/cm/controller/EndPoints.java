package com.teknokote.cm.controller;

public class EndPoints {
    public static final String ADD = "/add";
    public static final String DEACTIVATE = "/deactivate/{id}";
    public static final String ACTIVATE = "/activate/{id}";// id de l'entité cible de l'activation
    public static final String UPDATE = "/update"; // id de l'entité cible est intégré dans le dto
    public static final String LIST_BY_ACTIF = "/{actif}";
    public static final String LIST_BY_FILTER = "/filter";
    public static final String ACCOUNT_ROOT = "/account";
    public static final String AUTHORIZATION_ROOT = "/authorization";
    public static final String CARD_ROOT = "/card";
    public static final String MOVEMENT_ROOT = "/movement";
    public static final String PRODUCT_ROOT = "/product";
    public static final String TRANSACTION_ROOT = "/transaction";
    public static final String INFO = "/{id}";
    public static final String INFO_OLD = "/{id}/info";
    public static final String REFERENCE = "/reference/{reference}";
    public static final String LOGIN = "/login";
    public static final String CARD_GROUP_ROOT = "/cardGroup";
    public static final String COUNTRY_ROOT = "/country";
    public static final String SUPPLIER_ROOT = "/supplier";
    public static final String SALE_POINT_ROOT = SUPPLIER_ROOT + "/{supplierId}/salePoint";
    public static final String USER_ROOT = SUPPLIER_ROOT + "/{supplierId}/user";
    public static final String CUSTOMER_ROOT = SUPPLIER_ROOT + "/{supplierId}/customer";
    public static final String GET_BY_IDENTIFIER = "/identifier/{identifier}";
    public static final String LIST_PRODUCT_BY_SUPPLIER = SUPPLIER_ROOT + "/{supplierId}";
    public static final String PAGE_PRODUCT_BY_SUPPLIER = SUPPLIER_ROOT + "/page/{supplierId}";
    public static final String PAGE_SALE_POINT_BY_SUPPLIER = "/page";
    public static final String PAGE_USER_BY_SUPPLIER = "/page";
    public static final String BONUS_ROOT = "/bonus";
    public static final String CEILING_ROOT = "/ceiling";
    public static final String CUSTOMER_BASE_ROOT = "/customer/{customerId}";
    public static final String CUSTOMER_CARD_GROUP_ROOT = CUSTOMER_BASE_ROOT + CARD_GROUP_ROOT;
    public static final String CUSTOMER_CARD_ROOT = CUSTOMER_BASE_ROOT + CARD_ROOT;
    public static final String CUSTOMER_ACCOUNT_ROOT = CUSTOMER_BASE_ROOT + ACCOUNT_ROOT;
    public static final String UPDATE_CARD = "/card/{cardId}/update";
    public static final String AUTHORIZE = "/authorize";
    public static final String FIND = "/find";
    public static final String CHART = "/chart";
    public static final String DAILY_CHART = "/dailyChart";
}

