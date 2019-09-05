package io.tonlabs.ide.model;

import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class UiParameter {
  private final AbiParameterJso abiParameterJso;
  private String value;

  UiParameter(AbiParameterJso abiParameterJso) {
    this.abiParameterJso = abiParameterJso;
  }

  public String getName() {
    return this.abiParameterJso.getName();
  }

  public String getType() {
    return this.abiParameterJso.getType();
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
      return JSONNull.getInstance();
    }

    if (this.getType() != null && this.getType().matches("^(u?int|bits)\\d+$")) {
      if (this.getValue().matches("^[a-fA-F0-9]+$") && !this.getValue().matches("^\\d+$")) {
        return new JSONString("0x" + this.getValue());
      } else {
        return new JSONNumber(Double.parseDouble(this.getValue()));
      }
    }

    return new JSONString(this.getValue());
  }
}
