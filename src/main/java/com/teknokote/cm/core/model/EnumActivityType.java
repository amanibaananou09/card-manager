package com.teknokote.cm.core.model;

public enum EnumActivityType
{
   POST,
   PUT,
   DELETE,
   PATCH,
   GET;

   public static EnumActivityType of(String type){
      return EnumActivityType.valueOf(type);
   }
}
