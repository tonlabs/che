package io.tonlabs.ide.sdk;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class TonSDK {
  public native void init();
}
