package io.tonlabs.ide.model;

import com.google.gwt.core.client.JavaScriptObject;

public class AbiParameter extends JavaScriptObject {
  protected AbiParameter() {}

  public final native String getName() /*-{
      return this.name;
  }-*/;

  public final native String getType() /*-{
      return this.type;
  }-*/;
}
