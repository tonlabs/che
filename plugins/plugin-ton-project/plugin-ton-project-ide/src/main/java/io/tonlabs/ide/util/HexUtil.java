package io.tonlabs.ide.util;

public class HexUtil {
  private static final String HEX = "0123456789abcdef";

  public static String toHex(byte[] array) {
    StringBuilder result = new StringBuilder();
    for (byte value : array) {
      result.append(HEX.charAt((value & 0xFF) >> 4));
      result.append(HEX.charAt(value & 0x0F));
    }

    return result.toString();
  }
}
