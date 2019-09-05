package io.tonlabs.ide.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.googlecode.gwt.crypto.bouncycastle.util.encoders.Base64;
import io.tonlabs.ide.event.InputEvent;
import io.tonlabs.ide.model.Abi;
import io.tonlabs.ide.model.AbiFunctionJso;
import io.tonlabs.ide.model.AbiJso;
import io.tonlabs.ide.model.UiFunction;
import io.tonlabs.ide.model.UiParameter;
import io.tonlabs.ide.sdk.TonSdkInitializer;
import io.tonlabs.ide.sdk.jso.TONKeyPairDataJso;
import io.tonlabs.ide.util.Console;
import io.tonlabs.ide.util.HexUtil;
import io.tonlabs.ide.util.KeyUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.eclipse.che.api.core.model.workspace.Runtime;
import org.eclipse.che.api.core.model.workspace.Workspace;
import org.eclipse.che.api.core.model.workspace.runtime.Machine;
import org.eclipse.che.api.core.model.workspace.runtime.Server;
import org.eclipse.che.api.promises.client.Function;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.command.CommandExecutor;
import org.eclipse.che.ide.api.command.CommandImpl;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification.DisplayMode;
import org.eclipse.che.ide.api.notification.StatusNotification.Status;
import org.eclipse.che.ide.api.parts.base.BaseView;
import org.eclipse.che.ide.api.resources.File;
import org.eclipse.che.ide.api.resources.Folder;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.core.AgentURLModifier;

public class RunContractViewImpl extends BaseView<RunContractView.ActionDelegate>
    implements RunContractView {

  private static final String SE_NODE_MACHINE = "ws/se-node";
  private static final String SE_NODE_SERVER = "se-node";

  private static final RunContractViewImplUiBinder UI_BINDER =
      GWT.create(RunContractViewImplUiBinder.class);
  @UiField Label inputsHeader;
  @UiField ListBox tvcFileControl;
  @UiField ListBox abiFileControl;
  @UiField ListBox functionControl;
  @UiField Grid inputsControl;
  @UiField Button runButton;
  @UiField TextArea output;
  @Inject private CommandExecutor commandExecutor;
  @Inject private AppContext appContext;
  @Inject private TonSdkInitializer tonSdkInitializer;
  @Inject private NotificationManager notificationManager;
  @Inject private AgentURLModifier agentURLModifier;
  private Map<String, Abi> abiMap;
  private Map<String, File> tvcMap;

  public RunContractViewImpl() {
    this.setContentWidget(UI_BINDER.createAndBindUi(this));
  }

  private static String keyNotFoundErrorMsg(File abiFile) {
    return "Could not find secret key file \""
        + KeyUtil.keyFileNameFromAbi(abiFile)
        + "\". It must be stored in the same directory with the *.abi file.";
  }

  private void populateFunctionList(Abi abi) {
    this.functionControl.clear();

    for (AbiFunctionJso function : abi.getAbiJso().getFunctions()) {
      this.functionControl.addItem(function.getName());
    }
    if (this.functionControl.getItemCount() > 0) {
      this.functionControl.setSelectedIndex(0);
    }

    this.refreshInputsControl();
  }

  private Abi getSelectedAbi() {
    String abiFileName = this.abiFileControl.getSelectedItemText();
    if (abiFileName == null) {
      return null;
    }
    return this.abiMap.get(abiFileName);
  }

  private UiFunction getSelectedFunction() {
    Abi abi = this.getSelectedAbi();
    String functionName = this.functionControl.getSelectedItemText();
    if (abi == null || functionName == null) {
      return null;
    }
    return abi.getFunction(functionName);
  }

  private void refreshAbiControl() {
    this.refreshListControlFromSet(this.abiFileControl, this.abiMap.keySet());
    this.handleAbiFileControlChange();
  }

  private void refreshTvcControl() {
    this.refreshListControlFromSet(this.tvcFileControl, this.tvcMap.keySet());
  }

  private void refreshListControlFromSet(ListBox control, Set<String> items) {
    control.clear();
    for (String item : items) {
      control.addItem(item);
    }

    if (control.getItemCount() > 0) {
      control.setSelectedIndex(0);
    }
  }

  private void refreshInputsControl() {
    UiFunction function = this.getSelectedFunction();
    if (function == null) {
      this.inputsControl.resize(0, 2);
      this.inputsHeader.setVisible(false);
      this.updateSendButton();

      return;
    }

    this.inputsHeader.setVisible(function.getInputs().size() > 0);
    this.inputsControl.resize(function.getInputs().size(), 2);

    int index = 0;
    for (Map.Entry<String, UiParameter> entry : function.getInputs().entrySet()) {
      UiParameter parameter = entry.getValue();
      this.inputsControl.setText(index, 0, parameter.format());

      TextBox valueTextBox = new TextBox();
      valueTextBox.setText(parameter.getValue());

      valueTextBox.addDomHandler(
          event -> {
            TextBox textBox = (TextBox) event.getSource();
            parameter.setValue(textBox.getText());
            this.updateSendButton();
          },
          InputEvent.getType());

      valueTextBox.addKeyPressHandler(
          event -> {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
              this.runContract();
            }
          });

      this.inputsControl.setWidget(index, 1, valueTextBox);

      index++;
    }

    this.updateSendButton();
  }

  @UiHandler("abiFileControl")
  void handleAbiFileControlChange(@SuppressWarnings("unused") ChangeEvent event) {
    this.handleAbiFileControlChange();
  }

  private void handleAbiFileControlChange() {
    String abiFileName = this.abiFileControl.getSelectedValue();
    if (!this.abiMap.containsKey(abiFileName)) {
      return;
    }

    Abi abi = this.abiMap.get(abiFileName);
    if (abi.getAbiFile() == null) {
      return;
    }

    this.updateAbi(abi);
  }

  @UiHandler("functionControl")
  void handleFunctionControlChange(@SuppressWarnings("unused") ChangeEvent event) {
    this.refreshInputsControl();
  }

  @UiHandler("runButton")
  void handleSendButtonClick(@SuppressWarnings("unused") ClickEvent event) {
    this.runContract();
  }

  private void error(String text) {
    this.notificationManager.notify(text, Status.FAIL, DisplayMode.FLOAT_MODE);
  }

  private Server getSeNodeServer() {
    Workspace workspace = this.appContext.getWorkspace();
    Runtime runtime = workspace.getRuntime();
    if (runtime == null) {
      throw new IllegalStateException("Cannot get runtime");
    }

    Machine machine = runtime.getMachines().get(RunContractViewImpl.SE_NODE_MACHINE);
    if (machine == null) {
      throw new IllegalArgumentException(
          "Machine not found: " + RunContractViewImpl.SE_NODE_MACHINE);
    }

    Server server = machine.getServers().get(RunContractViewImpl.SE_NODE_SERVER);
    if (server == null) {
      throw new IllegalArgumentException("Server not found: " + RunContractViewImpl.SE_NODE_SERVER);
    }

    return server;
  }

  private void runContract() {
    Server server;
    try {
      server = this.getSeNodeServer();
    } catch (Exception ex) {
      this.error(ex.getMessage());
      return;
    }
    String sdkEndpoint = this.agentURLModifier.modify(server.getUrl());
    this.runContract(sdkEndpoint);
  }

  private void runContract(@NotNull String wsUrl) {
    UiFunction function = this.getSelectedFunction();
    if (function == null) {
      this.error("No function selected");
      return;
    }
    if (function.hasEmptyParams()) {
      this.error("All function parameters must be set");
      return;
    }

    Abi abi = this.getSelectedAbi();
    if (abi == null) {
      this.error("Could not find contract ABI");
      return;
    }

    if (abi.getKeyFile() == null) {
      this.error(keyNotFoundErrorMsg(abi.getAbiFile()));
      return;
    }

    String address =
        this.tvcMap.get(this.tvcFileControl.getSelectedItemText()).getNameWithoutExtension();

    this.runButton.setEnabled(false);

    abi.getKeyFile()
        .getContent()
        .thenPromise(
            privKeyContentBase64 -> {
              byte[] privKeyContent = Base64.decode(privKeyContentBase64);

              String privateKey = HexUtil.toHex(Arrays.copyOfRange(privKeyContent, 0, 32));
              String publicKey = HexUtil.toHex(Arrays.copyOfRange(privKeyContent, 32, 64));
              return this.tonSdkInitializer
                  .getTonSdk()
                  .runContract(
                      wsUrl,
                      address,
                      this.functionControl.getSelectedItemText(),
                      abi.getAbiJso(),
                      function.paramsToJson(),
                      TONKeyPairDataJso.fromPair(privateKey, publicKey))
                  .then(
                      (JavaScriptObject result) -> {
                        Console.log(result);
                        this.runButton.setEnabled(true);
                        this.notificationManager.notify("Method of contract run successfully!");

                        this.output.setText(result.toString());

                        this.commandExecutor.executeCommand(
                            new CommandImpl(
                                "Run Contract",
                                "echo \"" + new JSONObject(result).get("output").toString() + "\"",
                                "ton-run-contract"));
                      })
                  .catchError(
                      (PromiseError error) -> {
                        this.runButton.setEnabled(true);
                        this.error("Error running contract: " + error.getMessage());
                      });
            })
        .catchError(
            (error) -> {
              this.runButton.setEnabled(true);
              this.error("Error reading secret key file. Error: " + error.getMessage());
            })
        .then(
            (Void) -> {
              this.runButton.setEnabled(true);
              this.notificationManager.notify("Method of contract run successfully!");
            });
  }

  private Abi getOrAddAbi(String fileName) {
    Abi result = this.abiMap.get(fileName);
    if (result == null) {
      result = new Abi();
      this.abiMap.put(fileName, result);
    }

    return result;
  }

  @Override
  public void updateDeploymentFolder(Folder deploymentFolder) {
    this.abiMap = null;
    this.tvcMap = null;

    deploymentFolder
        .getChildren(true)
        .then(
            (Function<Resource[], Object>)
                resources -> {
                  this.tvcMap = new HashMap<>();
                  this.abiMap = new HashMap<>();

                  for (Resource resource : resources) {
                    if (!resource.isFile()) {
                      continue;
                    }

                    File file = resource.asFile();
                    switch (file.getExtension()) {
                      case "tvc":
                        this.tvcMap.put(file.getName(), file);
                        break;
                      case "abi":
                        this.getOrAddAbi(file.getName()).setAbiFile(file);
                        break;
                      default:
                        if (KeyUtil.isKeyFile(file)) {
                          this.getOrAddAbi(KeyUtil.abiFileNameFromKey(file)).setKeyFile(file);
                        }
                    }
                  }

                  this.refreshTvcControl();
                  this.refreshAbiControl();

                  return null;
                });
  }

  private void updateAbi(Abi abi) {
    abi.getAbiFile()
        .getContent()
        .then(
            (Function<String, Object>)
                content -> {
                  AbiJso abiJso = AbiJso.fromJson(content);
                  abi.setAbiJso(abiJso);
                  this.populateFunctionList(abi);

                  return abiJso;
                });
  }

  private void updateSendButton() {
    UiFunction function = this.getSelectedFunction();
    if (function == null) {
      this.runButton.setEnabled(false);
      return;
    }

    for (UiParameter parameter : this.getSelectedFunction().getInputs().values()) {
      if (parameter.getValue() == null || parameter.getValue().length() < 1) {
        this.runButton.setEnabled(false);
        return;
      }
    }

    this.runButton.setEnabled(true);
  }

  interface RunContractViewImplUiBinder extends UiBinder<Widget, RunContractViewImpl> {}
}
