package io.tonlabs.ide.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.typedarrays.shared.Uint8Array;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import io.tonlabs.ide.event.InputEvent;
import io.tonlabs.ide.model.Abi;
import io.tonlabs.ide.model.AbiFunctionJso;
import io.tonlabs.ide.model.AbiJso;
import io.tonlabs.ide.model.UiFunction;
import io.tonlabs.ide.model.UiParameter;
import io.tonlabs.ide.sdk.TonSdkInitializer;
import io.tonlabs.ide.sdk.jso.TONKeyPairDataJso;
import io.tonlabs.ide.sdk.jso.TonSdkJso;
import io.tonlabs.ide.util.HexUtil;
import io.tonlabs.ide.util.HttpUtil;
import io.tonlabs.ide.util.KeyUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.che.api.promises.client.Function;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification.DisplayMode;
import org.eclipse.che.ide.api.notification.StatusNotification.Status;
import org.eclipse.che.ide.api.parts.base.BaseView;
import org.eclipse.che.ide.api.resources.File;
import org.eclipse.che.ide.api.resources.Folder;
import org.eclipse.che.ide.api.resources.Resource;

public class SendMessageViewImpl extends BaseView<SendMessageView.ActionDelegate>
    implements SendMessageView {

  private static final SendMessageViewImplUiBinder UI_BINDER =
      GWT.create(SendMessageViewImplUiBinder.class);

  @UiField Label inputsHeader;
  @UiField ListBox tvcFileControl;
  @UiField ListBox abiFileControl;
  @UiField ListBox functionControl;
  @UiField Grid inputsControl;
  @UiField Button sendButton;

  @Inject private TonSdkInitializer tonSdkInitializer;
  @Inject private NotificationManager notificationManager;

  private Map<String, Abi> abiMap;
  private Map<String, File> tvcMap;

  public SendMessageViewImpl() {
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
              this.sendMessage();
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

  @UiHandler("sendButton")
  void handleSendButtonClick(@SuppressWarnings("unused") ClickEvent event) {
    this.sendMessage();
  }

  private void error(String text) {
    this.notificationManager.notify(text, Status.FAIL, DisplayMode.FLOAT_MODE);
  }

  private void sendMessage() {
    UiFunction function = this.getSelectedFunction();
    if (function == null || function.hasEmptyParams()) {
      return;
    }

    Abi abi = this.getSelectedAbi();
    if (abi == null) {
      return;
    }

    if (abi.getKeyFile() == null) {
      this.error(keyNotFoundErrorMsg(abi.getAbiFile()));
      return;
    }

    String address =
        this.tvcMap.get(this.tvcFileControl.getSelectedItemText()).getNameWithoutExtension();

    this.sendButton.setEnabled(false);

    String[] privateKey = {null};
    String[] publicKey = {null};

    HttpUtil.getFileContent(abi.getKeyFile())
        .thenPromise(
            (Uint8Array privKeyContent) -> {
              privateKey[0] = HexUtil.toHex(privKeyContent.subarray(0, 32));
              publicKey[0] = HexUtil.toHex(privKeyContent.subarray(32, 64));
              return this.tonSdkInitializer
                  .getTonSdk()
                  .thenPromise(
                      (TonSdkJso sdk) ->
                          sdk.runContract(
                              address,
                              this.functionControl.getSelectedItemText(),
                              abi.getAbiJso(),
                              function.paramsToJson(),
                              TONKeyPairDataJso.fromPair(privateKey[0], publicKey[0])))
                  .then((Void nothing) -> this.sendButton.setEnabled(true))
                  .catchError(
                      (PromiseError error) -> {
                        this.sendButton.setEnabled(true);
                        this.error("Error running contract: " + error.getMessage());
                      });
            })
        .catchError(
            (error) -> {
              this.sendButton.setEnabled(true);
              this.error("Error reading secret key file. Error: " + error.getMessage());
            })
        .then(
            (Void) -> {
              this.sendButton.setEnabled(true);
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
      this.sendButton.setEnabled(false);
      return;
    }

    for (UiParameter parameter : this.getSelectedFunction().getInputs().values()) {
      if (parameter.getValue() == null || parameter.getValue().length() < 1) {
        this.sendButton.setEnabled(false);
        return;
      }
    }

    this.sendButton.setEnabled(true);
  }

  interface SendMessageViewImplUiBinder extends UiBinder<Widget, SendMessageViewImpl> {}
}
