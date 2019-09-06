package io.tonlabs.ide.util;

import org.eclipse.che.ide.api.resources.File;

public final class KeyUtil {
  private static final String KEY_FILENAME_SUFFIX = "_key";
  private static final String KEY_FILENAME_EXT = "base64";

  public static boolean isKeyFile(File file) {
    return file.getNameWithoutExtension().endsWith(KEY_FILENAME_SUFFIX)
        && KEY_FILENAME_EXT.equals(file.getExtension());
  }

  public static String abiFileNameFromKey(File file) {
    if (!isKeyFile(file)) {
      throw new IllegalArgumentException("The given file is not a key file!");
    }
    String name = file.getNameWithoutExtension();
    return name.substring(0, name.length() - KEY_FILENAME_SUFFIX.length()) + ".abi";
  }

  public static String keyFileNameFromAbi(File file) {
    return file.getNameWithoutExtension() + KEY_FILENAME_SUFFIX + "." + KEY_FILENAME_EXT;
  }
}
