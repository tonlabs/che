package io.tonlabs.ide.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;

public class AbiJso extends JavaScriptObject {
  protected AbiJso() {}

  public static AbiJso fromJson(String json) {
    return (AbiJso) JsonUtils.safeEval(json);
  }

  public final native AbiFunctionJso[] getFunctions() /*-{
      return this.functions;
  }-*/;

  public final native int getVersion() /*-{
      return this["ABI version"];
  }-*/;
}
