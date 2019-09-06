package io.tonlabs.ide.model;

import com.google.gwt.core.client.JavaScriptObject;

@SuppressWarnings({"unused", "JSUnresolvedVariable"})
public class AbiFunctionJso extends JavaScriptObject {
  protected AbiFunctionJso() {}

  public final native AbiParameterJso[] getInputs() /*-{
      return this.inputs;
  }-*/;

  public final native String getName() /*-{
      return this.name;
  }-*/;

  public final native AbiParameterJso[] getOutputs() /*-{
      return this.outputs;
  }-*/;

  public final native boolean getSigned() /*-{
      return this.signed;
  }-*/;
}
