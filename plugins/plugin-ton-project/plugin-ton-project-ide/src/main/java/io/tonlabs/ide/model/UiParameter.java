package io.tonlabs.ide.model;

import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

@SuppressWarnings("WeakerAccess")
public class UiParameter {
  private final AbiParameter abiParameter;
  private String value;

  UiParameter(AbiParameter abiParameter) {
    this.abiParameter = abiParameter;
  }

  public String getName() {
    return this.abiParameter.getName();
  }

  public String getType() {
    return this.abiParameter.getType();
  }

  public String format() {
    return this.getName() + ": " + this.getType();
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  JSONValue getValueAsJson() {
    if (this.getValue() == null) {
      return new JSONString("");
    }

    if (this.getType() != null
        && this.getType().matches("^uint\\d+$")
        && this.getValue().matches("^[a-fA-F0-9]+$")) {
      return new JSONString("0x" + this.getValue());
    }

    return new JSONString(this.getValue());
  }
}
