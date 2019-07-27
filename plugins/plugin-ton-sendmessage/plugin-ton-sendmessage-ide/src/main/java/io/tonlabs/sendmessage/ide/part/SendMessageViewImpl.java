package io.tonlabs.sendmessage.ide.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Grid;
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
import org.eclipse.che.api.promises.client.Function;
import org.eclipse.che.ide.api.parts.base.BaseView;
import org.eclipse.che.ide.api.resources.File;

public class SendMessageViewImpl extends BaseView<SendMessageView.ActionDelegate>
    implements SendMessageView {

  private static final SendMessageViewImplUiBinder UI_BINDER =
      GWT.create(SendMessageViewImplUiBinder.class);

  private Abi abi;
  private Map<String, UiFunction> functions;

  @UiField ListBox functionControl;
  @UiField Grid inputsControl;

  @Inject
  public SendMessageViewImpl() {
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

  private void refreshInputsControl() {
    String functionName = this.functionControl.getSelectedItemText();
    if (functionName == null) {
      this.inputsControl.resize(0, 2);
    }

    UiFunction function = this.functions.get(functionName);
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

  @UiHandler("functionControl")
  void handleChange(ChangeEvent event) {
    this.refreshInputsControl();
  }

  @Override
  public void updateAbi(File abiFile) {
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

  interface SendMessageViewImplUiBinder extends UiBinder<Widget, SendMessageViewImpl> {}
}
