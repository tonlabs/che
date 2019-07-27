package io.tonlabs.sendmessage.ide.model;

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
}
