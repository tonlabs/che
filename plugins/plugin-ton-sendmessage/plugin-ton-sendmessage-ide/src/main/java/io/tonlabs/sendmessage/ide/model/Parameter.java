package io.tonlabs.sendmessage.ide.model;

public class Parameter {
  private String name;
  private String value;

  public Parameter(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public Parameter(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
