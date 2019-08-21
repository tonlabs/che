package io.tonlabs.ide.sdk.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class TONKeyPairDataJso extends JavaScriptObject {
  protected TONKeyPairDataJso() {}

  public static native TONKeyPairDataJso fromPair(String secretKey, String publicKey) /*-{
    return {
      secret: secretKey,
      public: publicKey
    };
  }-*/;
}
