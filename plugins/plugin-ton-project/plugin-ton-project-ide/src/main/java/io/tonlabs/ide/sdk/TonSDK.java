package io.tonlabs.ide.sdk;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public final class TonSDK {
  private static TonSDK instance = null;

  private TonSDK() {}

  public static TonSDK getInstance() {
    if (instance == null) {
      insertTonJs();
      instance = new TonSDK();
      instance.init();
    }

    return instance;
  }

  private static void insertTonJs() {
    Element script = DOM.createElement("script");
    script.setAttribute("src", GWT.getModuleBaseForStaticFiles() + "ton.js");

    Document.get().getHead().appendChild(script);
  }

  private native void init();
}
