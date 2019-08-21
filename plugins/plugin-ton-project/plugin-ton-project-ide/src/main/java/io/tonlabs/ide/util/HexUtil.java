package io.tonlabs.ide.util;

import java.nio.charset.StandardCharsets;

public class HexUtil {
  private static final String HEX = "0123456789abcdef";

  public static String strToHex(String str) {
    byte[] bytes = str.getBytes(StandardCharsets.UTF_8);

    StringBuilder result = new StringBuilder();
    for (byte b : bytes) {
      result.append(HEX.charAt(b & 0xFF >> 4));
      result.append(HEX.charAt(b & 0x0F));
    }

    return result.toString();
  }
}
