package com.teknokote.cm.core.model;

public enum EnumCounterType
{
   CEILING(CounterType.CEILING),
   BONUS(CounterType.BONUS);

   EnumCounterType(String val) {
      // Forces same name and value of enum
      if (!this.name().equals(val))
         throw new IllegalArgumentException("Incorrect use of EnumCounterType");
   }

   public static class CounterType {
      public static final String CEILING = "CEILING";
      public static final String BONUS = "BONUS";

      private CounterType() {
         throw new IllegalStateException("Utility class");
      }
   }
}
