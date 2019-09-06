package io.tonlabs.ide.model;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.che.ide.api.resources.File;

public class Abi {
  private AbiJso abiJso;
  private File abiFile;
  private File keyFile;

  Map<String, UiFunction> functions;

  public Abi() {}

  public AbiJso getAbiJso() {
    return this.abiJso;
  }

  public void setAbiJso(AbiJso abiJso) {
    this.abiJso = abiJso;
    this.functions = extractFunctionsFromAbi(abiJso);
  }

  public File getAbiFile() {
    return this.abiFile;
  }

  public void setAbiFile(File abiFile) {
    this.abiFile = abiFile;
  }

  public File getKeyFile() {
    return this.keyFile;
  }

  public void setKeyFile(File keyFile) {
    this.keyFile = keyFile;
  }

  public UiFunction getFunction(String name) {
    return this.functions.get(name);
  }

  private static Map<String, UiFunction> extractFunctionsFromAbi(AbiJso abiJso) {
    Map<String, UiFunction> result = new HashMap<>();
    for (AbiFunctionJso abiFunctionJso : abiJso.getFunctions()) {
      result.put(abiFunctionJso.getName(), new UiFunction(abiFunctionJso));
    }

    return result;
  }
}
