package com.teknokote.cm.controller;

public class EndPoints
{
   public static final String ADD = "/add";
   public static final String DEACTIVATE = "/deactivate/{id}";
   public static final String ACTIVATE = "/activate/{id}";// id de l'entité cible de l'activation
   public static final String UPDATE = "/update"; // id de l'entité cible est intégré dans le dto
   public static final String GENERATE = "/generate";
   public static final String RESET = "/reset";
   public static final String START = "/start";
   public static final String STOP = "/stop";
   public static final String LIST_BY_ACTIF = "/{actif}";
   public static final String LIST_BY_FILTER = "/filter";
   public static final String SEARCH = "/search";
   public static final String ACCOUNT_ROOT = "/account";
   public static final String AUTHORIZATION_ROOT = "/authorization";
   public static final String CARD_ROOT = "/card";
   public static final String CUSTOMER_ROOT = "/customer";
   public static final String MOVEMENT_ROOT = "/movement";
   public static final String PRODUCT_ROOT = "/product";
   public static final String TRANSACTION_ROOT = "/transaction";
   public static final String USER_ROOT = "/user";
   public static final String INFO = "/{id}";
   public static final String REFERENCE = "/{reference}";
   public static final String LOGIN = "/login";
   public static final String CARD_GROUP_ROOT = "/cardGroup";
   public static final String COUNTRY_ROOT = "/country";
   public static final String SALE_POINT_ROOT = "/salePoint";
   public static final String SUPPLIER_ROOT = "/supplier";
   public static final String BONUS_ROOT = "/bonus" ;
   public static final String CEILING_ROOT = "/ceiling";
}

