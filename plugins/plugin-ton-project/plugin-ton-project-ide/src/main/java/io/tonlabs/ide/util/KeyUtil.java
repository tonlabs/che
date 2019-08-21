package io.tonlabs.ide.util;

import org.eclipse.che.ide.api.resources.File;

public final class KeyUtil {
  private static final String KEY_FILENAME_SUFFIX = "_key";

  private static boolean isKey(File file) {
    return file.getNameWithoutExtension().endsWith(KEY_FILENAME_SUFFIX);
  }

  public static boolean isPublicKey(File file) {
    return isKey(file) && file.getExtension() != null && file.getExtension().equals("pub");
  }

  public static boolean isPrivateKey(File file) {
    return isKey(file) && file.getExtension() == null;
  }

  public static String abiFileNameFromKey(File file) {
    if (!isKey(file)) {
      throw new IllegalArgumentException("The given file is not a key file!");
    }
    String name = file.getNameWithoutExtension();
    return name.substring(0, name.length() - KEY_FILENAME_SUFFIX.length()) + ".abi";
  }

  public static String keyFileNameFromAbi(File file) {
    return file.getNameWithoutExtension() + KEY_FILENAME_SUFFIX;
  }

  public static String publicKeyFileNameFromAbi(File file) {
    return keyFileNameFromAbi(file) + ".pub";
  }
}
