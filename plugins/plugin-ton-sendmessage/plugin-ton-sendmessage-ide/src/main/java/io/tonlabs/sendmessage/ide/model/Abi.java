package io.tonlabs.sendmessage.ide.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;

public class Abi extends JavaScriptObject {
  protected Abi() {}

  public static Abi fromJson(String json) {
    return (Abi) JsonUtils.safeEval(json);
  }

  public final native AbiFunction[] getFunctions() /*-{
      return this.functions;
  }-*/;

  public final native int getVersion() /*-{
      return this["ABI version"];
  }-*/;
}
