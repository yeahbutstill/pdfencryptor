package com.gulteking.pdfencryptor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestObjects {

  @Test
  void testString() {
    String s = "saya";
    String s1 = " ";
    String s2 = "  siapa yaaaaa  ";

    System.out.println(s2.length());
    System.out.println(s2.trim().length());
    System.out.println(s2.substring(10));

    Assertions.assertNotNull(s);
    Assertions.assertEquals(" ", s1);
  }
}
