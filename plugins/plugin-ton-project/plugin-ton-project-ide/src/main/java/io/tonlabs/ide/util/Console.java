package io.tonlabs.ide.util;

import com.google.gwt.core.client.JavaScriptObject;

public final class Console {
  public static native void log(JavaScriptObject obj) /*-{
    console.log(obj);
  }-*/;

  public static native void log(String message) /*-{
    console.log(message);
  }-*/;
}
