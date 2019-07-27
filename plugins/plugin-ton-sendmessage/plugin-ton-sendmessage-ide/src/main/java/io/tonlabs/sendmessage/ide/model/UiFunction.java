package io.tonlabs.sendmessage.ide.model;

import java.util.HashMap;
import java.util.Map;

public class UiFunction {
  private final AbiFunction abiFunction;
  private final Map<String, UiParameter> inputs;
  private final Map<String, UiParameter> outputs;

  public UiFunction(AbiFunction abiFunction) {
    this.abiFunction = abiFunction;
    this.inputs = new HashMap<>();
    this.outputs = new HashMap<>();

    for (AbiParameter parameter : abiFunction.getInputs()) {
      this.inputs.put(parameter.getName(), new UiParameter(parameter));
    }

    for (AbiParameter parameter : abiFunction.getOutputs()) {
      this.outputs.put(parameter.getName(), new UiParameter(parameter));
    }
  }

  public AbiFunction getAbiFunction() {
    return this.abiFunction;
  }

  public Map<String, UiParameter> getInputs() {
    return this.inputs;
  }

  public Map<String, UiParameter> getOutputs() {
    return this.outputs;
  }
}
