package com.test;

public class Demo {

  static ClassLoader classLoader = Demo.class.getClassLoader();

  public static void main(String... args) throws ClassNotFoundException {
    Class aClass = classLoader.loadClass("com.test.Active");
    System.out.println("The active class from Demo-api: " + aClass.toString());
  }
}
