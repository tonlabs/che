package io.tonlabs.ide.util;

import com.google.gwt.typedarrays.shared.Uint8Array;

public class HexUtil {
  private static final String HEX = "0123456789abcdef";

  public static String toHex(Uint8Array array) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < array.length(); i++) {
      short value = array.get(i);
      result.append(HEX.charAt((value & 0xFF) >> 4));
      result.append(HEX.charAt(value & 0x0F));
    }

    return result.toString();
  }
}
