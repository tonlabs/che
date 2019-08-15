package io.tonlabs.ide.sdk;

public class TonSDK {
  public static native void init() /*-{
    initApp().then(() => {
      alert('init ok');
    });
  }-*/;
}
