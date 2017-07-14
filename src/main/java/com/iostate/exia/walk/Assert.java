package com.iostate.exia.walk;

public class Assert {
  public static void isTrue(boolean value) {
    if (!value) {
      throw new RuntimeException("Assertion Error! current file: "+ FileWalker.getCurrentFile());
    }
  }
  
  public static void isNotNull(Object o) {
    isTrue(o != null);
  }
}
