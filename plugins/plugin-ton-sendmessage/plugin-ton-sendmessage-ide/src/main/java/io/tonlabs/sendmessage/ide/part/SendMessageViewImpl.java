package io.tonlabs.sendmessage.ide.part;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.DockLayoutPanel;
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

  private final DockLayoutPanel rootElement;

  @UiField CustomComboBox functionControl;

  @UiField CellTable<Parameter> parametersControl;

  private final List<Parameter> parameters;

  @Inject
  public SendMessageViewImpl() {
    this.parameters = new ArrayList<>();

    this.rootElement = this.initUi();

    this.populateFunctionList();
    this.populateParameterList();
  }

  private DockLayoutPanel initUi() {
    DockLayoutPanel rootElement = UI_BINDER.createAndBindUi(this);
    this.setupParameterTable(this.parametersControl);

    return rootElement;
  }

  private void setupParameterTable(CellTable<Parameter> cellTable) {
    cellTable.addColumn(
        new TextColumn<Parameter>() {
          @Override
          public String getValue(Parameter parameter) {
            return parameter.getName();
          }
        },
        "Parameter");

    cellTable.addColumn(
        new Column<Parameter, String>(new EditTextCell()) {
          @Override
          public String getValue(Parameter parameter) {
            return parameter.getValue();
          }
        },
        "Value");
  }

  private void populateFunctionList() {
    this.functionControl.addItem("");
    this.functionControl.addItem("compute0");
  }

  private void populateParameterList() {
    this.parameters.add(new Parameter("Param1"));
    this.parametersControl.setRowData(this.parameters);
  }

  @UiHandler("functionControl")
  void handleChange(ChangeEvent event) {
    this.populateParameterList();
  }

  interface SendMessageViewImplUiBinder extends UiBinder<DockLayoutPanel, SendMessageViewImpl> {}
}
