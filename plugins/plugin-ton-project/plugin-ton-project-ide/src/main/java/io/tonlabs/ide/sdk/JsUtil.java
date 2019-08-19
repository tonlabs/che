package io.tonlabs.ide.sdk;

public class JsUtil {
  public static native TonSDK getTonSDK() /*-{
    return top.frames['ide-application-iframe'].tonSdk;
  }-*/;
}
