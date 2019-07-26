package io.tonlabs.sendmessage.ide.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import io.tonlabs.sendmessage.ide.model.Parameter;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.che.ide.api.parts.base.BaseView;
import org.eclipse.che.ide.ui.listbox.CustomComboBox;

public class SendMessageViewImpl extends BaseView<SendMessageView.ActionDelegate>
    implements SendMessageView {

  private static final SendMessageViewImplUiBinder UI_BINDER =
      GWT.create(SendMessageViewImplUiBinder.class);

  @UiField CustomComboBox functionControl;

  @UiField Grid inputsControl;

  private final List<Parameter> parameters;

  @Inject
  public SendMessageViewImpl() {
    this.parameters = new ArrayList<>();

    this.setContentWidget(UI_BINDER.createAndBindUi(this));

    this.populateFunctionList();
    this.populateParameterList();
  }

  private void populateFunctionList() {
    this.functionControl.clear();
    this.functionControl.addItem("");
    this.functionControl.addItem("compute0");
  }

  private void populateParameterList() {
    this.parameters.clear();
    this.parameters.add(new Parameter("Param1"));

    this.refreshParameterList();
  }

  private void refreshParameterList() {
    this.inputsControl.resize(this.parameters.size(), 2);
    for (int i = 0; i < this.parameters.size(); i++) {
      this.inputsControl.setText(i, 0, this.parameters.get(i).getName());

      TextBox valueTextBox = new TextBox();
      valueTextBox.setValue(this.parameters.get(i).getValue());
      this.inputsControl.setWidget(i, 1, valueTextBox);
    }
  }

  @UiHandler("functionControl")
  void handleChange(ChangeEvent event) {
    this.populateParameterList();
  }

  interface SendMessageViewImplUiBinder extends UiBinder<Widget, SendMessageViewImpl> {}
}
