package io.tonlabs.sendmessage.ide.model;

import com.google.gwt.core.client.JavaScriptObject;

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
      ѳѳ
  }-*/;

  public final native boolean getSigned() /*-{
      return this.signed;
  }-*/;
}
