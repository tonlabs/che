package io.tonlabs.ide.sdk.jso;

import com.google.gwt.core.client.JavaScriptObject;
import io.tonlabs.ide.model.AbiJso;
import org.eclipse.che.api.promises.client.Promise;

public final class TonSdkJso extends JavaScriptObject {
  protected TonSdkJso() {}

  public final native Promise<Void> setup() /*-{
    return this.setup();
  }-*/;

  public final native Promise<Void> runContract(
      String address,
      String functionName,
      AbiJso abiJso,
      JavaScriptObject input,
      TONKeyPairDataJso keyPair) /*-{
    return this.runContract(address, functionName, abiJso, input, keyPair);
  }-*/;
}
