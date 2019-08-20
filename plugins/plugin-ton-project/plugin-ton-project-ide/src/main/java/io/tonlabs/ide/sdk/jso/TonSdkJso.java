package io.tonlabs.ide.sdk.jso;

import com.google.gwt.core.client.JavaScriptObject;
import org.eclipse.che.api.promises.client.Promise;

public final class TonSdkJso extends JavaScriptObject {
  protected TonSdkJso() {}

  public final native Promise<Void> initApp() /*-{
    return this.initApp();
  }-*/;

  public final native void sendMessage() /*-{
    this.sendMessage();
  }-*/;
}
