package io.tonlabs.ide.sdk.jso;

import com.google.gwt.core.client.JavaScriptObject;

public final class TonSdkJso extends JavaScriptObject {

  protected TonSdkJso() {}

  public static native TonSdkJso fromJso(JavaScriptObject jso) /*-{
    return jso;
  }-*/;

  public final native void init() /*-{
    this.init();
  }-*/;

  public final native void sendMessage() /*-{
    this.sendMessage();
  }-*/;
}
