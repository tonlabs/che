package io.tonlabs.ide.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONObject;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class UiFunction {
  private final AbiFunctionJso abiFunctionJso;
  private final Map<String, UiParameter> inputs;
  private final Map<String, UiParameter> outputs;

  public UiFunction(AbiFunctionJso abiFunctionJso) {
    this.abiFunctionJso = abiFunctionJso;
    this.inputs = new HashMap<>();
    this.outputs = new HashMap<>();

    for (AbiParameterJso parameter : abiFunctionJso.getInputs()) {
      this.inputs.put(parameter.getName(), new UiParameter(parameter));
    }

    for (AbiParameterJso parameter : abiFunctionJso.getOutputs()) {
      this.outputs.put(parameter.getName(), new UiParameter(parameter));
    }
  }

  public AbiFunctionJso getAbiFunctionJso() {
    return this.abiFunctionJso;
  }

  public Map<String, UiParameter> getInputs() {
    return this.inputs;
  }

  public Map<String, UiParameter> getOutputs() {
    return this.outputs;
  }

  public boolean hasEmptyParams() {
    for (UiParameter parameter : this.inputs.values()) {
      if (parameter.getValue() == null || parameter.getValue().length() < 1) {
        return true;
      }
    }

    return false;
  }

  public JavaScriptObject paramsToJson() {
    JSONObject result = new JSONObject();
    for (UiParameter parameter : this.inputs.values()) {
      result.put(parameter.getName(), parameter.getValueAsJson());
    }

    return JsonUtils.safeEval(result.toString());
  }
}
