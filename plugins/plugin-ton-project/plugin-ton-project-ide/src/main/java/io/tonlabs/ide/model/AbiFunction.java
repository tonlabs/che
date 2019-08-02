package io.tonlabs.ide.model;

import com.google.gwt.core.client.JavaScriptObject;

@SuppressWarnings({"unused", "JSUnresolvedVariable"})
public class AbiFunction extends JavaScriptObject {
  protected AbiFunction() {}

  public final native AbiParameter[] getInputs() /*-{
      return this.inputs;
  }-*/;

  public final native String getName() /*-{
      return this.name;
  }-*/;

  public final native AbiParameter[] getOutputs() /*-{
      return this.outputs;
  }-*/;

  public final native boolean getSigned() /*-{
      return this.signed;
  }-*/;
}
