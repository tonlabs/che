package io.tonlabs.sendmessage.ide.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import io.tonlabs.sendmessage.ide.model.Abi;
import io.tonlabs.sendmessage.ide.model.AbiFunction;
import io.tonlabs.sendmessage.ide.model.UiFunction;
import io.tonlabs.sendmessage.ide.model.UiParameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.che.api.promises.client.Function;
import org.eclipse.che.ide.api.parts.base.BaseView;
import org.eclipse.che.ide.api.resources.File;
import org.eclipse.che.ide.api.resources.Folder;
import org.eclipse.che.ide.api.resources.SearchItemReference;
import org.eclipse.che.ide.api.resources.SearchResult;
import org.eclipse.che.ide.api.vcs.VcsStatus;
import org.eclipse.che.ide.resource.Path;
import org.eclipse.che.ide.resources.impl.ResourceManager;

public class SendMessageViewImpl extends BaseView<SendMessageView.ActionDelegate>
    implements SendMessageView {

  private static final SendMessageViewImplUiBinder UI_BINDER =
      GWT.create(SendMessageViewImplUiBinder.class);

  private ResourceManager resourceManager;
  private ResourceManager.ResourceFactory resourceFactory;

  private Folder deploymentFolder;
  private Path abiPath;
  private Abi abi;
  private Map<String, File> abiMap;
  private Map<String, File> tvcMap;
  private Map<String, UiFunction> functions;

  @UiField Label inputsHeader;
  @UiField ListBox tvcFileControl;
  @UiField ListBox abiFileControl;
  @UiField ListBox functionControl;
  @UiField Grid inputsControl;

  @Inject
  public SendMessageViewImpl(
      ResourceManager resourceManager, ResourceManager.ResourceFactory resourceFactory) {
    this.resourceManager = resourceManager;
    this.resourceFactory = resourceFactory;

    this.setContentWidget(UI_BINDER.createAndBindUi(this));
  }

  private static Map<String, UiFunction> extractFunctionsFromAbi(Abi abi) {
    Map<String, UiFunction> result = new HashMap<>();
    for (AbiFunction abiFunction : abi.getFunctions()) {
      result.put(abiFunction.getName(), new UiFunction(abiFunction));
    }

    return result;
  }

  private void populateFunctionList() {
    this.functionControl.clear();

    for (AbiFunction function : this.abi.getFunctions()) {
      this.functionControl.addItem(function.getName());
    }
    if (this.functionControl.getItemCount() > 0) {
      this.functionControl.setSelectedIndex(0);
    }
    this.refreshInputsControl();
  }

  private UiFunction getCurrentFunction() {
    String functionName = this.functionControl.getSelectedItemText();
    if (functionName == null) {
      return null;
    }
    return this.functions.get(functionName);
  }

  private void refreshAbiControl() {
    this.refreshListControlFromSet(this.abiFileControl, this.abiMap.keySet());
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
    UiFunction function = this.getCurrentFunction();
    if (function == null) {
      this.inputsControl.resize(0, 2);
      this.inputsHeader.setVisible(false);

      return;
    }

    this.inputsHeader.setVisible(function.getInputs().size() > 0);
    this.inputsControl.resize(function.getInputs().size(), 2);

    int index = 0;
    for (Map.Entry<String, UiParameter> entry : function.getInputs().entrySet()) {
      UiParameter parameter = entry.getValue();
      this.inputsControl.setText(index, 0, parameter.format());

      TextBox valueTextBox = new TextBox();
      valueTextBox.setName(parameter.getName());
      valueTextBox.setValue(parameter.getValue());
      this.inputsControl.setWidget(index, 1, valueTextBox);

      index++;
    }
  }

  @UiHandler("abiFileControl")
  void handleAbiFileControlChange(ChangeEvent event) {
    String abiFileName = this.abiFileControl.getSelectedValue();
    if (!this.abiMap.containsKey(abiFileName)) {
      return;
    }

    this.updateAbi(this.abiMap.get(abiFileName));
  }

  @UiHandler("functionControl")
  void handleFunctionControlChange(ChangeEvent event) {
    this.refreshInputsControl();
  }

  @UiHandler("sendButton")
  void handleSendButtonClick(ClickEvent event) {
    UiFunction function = this.getCurrentFunction();
    if (function == null) {
      return;
    }

    String paramsJson = function.paramsToJson();

    // TODO:
  }

  @Override
  public void updateDeploymentFolder(Folder deploymentFolder) {
    this.deploymentFolder = deploymentFolder;
    this.abiMap = null;
    this.tvcMap = null;

    deploymentFolder
        .search("*.abi", "")
        .then(
            (Function<SearchResult, Object>)
                result -> {
                  SendMessageViewImpl.this.abiMap =
                      SendMessageViewImpl.this.searchResultToMap(result);
                  SendMessageViewImpl.this.refreshAbiControl();

                  return SendMessageViewImpl.this.abiMap;
                });

    deploymentFolder
        .search("*.tvc", "")
        .then(
            (Function<SearchResult, Object>)
                result -> {
                  SendMessageViewImpl.this.tvcMap =
                      SendMessageViewImpl.this.searchResultToMap(result);
                  SendMessageViewImpl.this.refreshTvcControl();

                  return SendMessageViewImpl.this.tvcMap;
                });
  }

  private void updateAbi(File abiFile) {
    this.abiPath = abiFile.getLocation();
    abiFile
        .getContent()
        .then(
            (Function<String, Object>)
                content -> {
                  Abi abi = Abi.fromJson(content);
                  SendMessageViewImpl.this.abi = abi;
                  SendMessageViewImpl.this.functions =
                      SendMessageViewImpl.extractFunctionsFromAbi(abi);

                  SendMessageViewImpl.this.populateFunctionList();

                  return SendMessageViewImpl.this.abi;
                });
  }

  private Map<String, File> searchResultToMap(SearchResult searchResult) {
    Map<String, File> result = new HashMap<>();

    for (SearchItemReference item : searchResult.getItemReferences()) {
      File file =
          this.resourceFactory.newFileImpl(
              Path.valueOf(item.getPath()),
              item.getContentUrl(),
              this.resourceManager,
              VcsStatus.UNTRACKED);
      result.put(item.getName(), file);
    }

    return result;
  }

  interface SendMessageViewImplUiBinder extends UiBinder<Widget, SendMessageViewImpl> {}
}
