package io.tonlabs.sendmessage.ide.part;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import io.tonlabs.sendmessage.ide.model.Parameter;
import org.eclipse.che.ide.api.parts.base.BaseView;

import java.util.ArrayList;
import java.util.List;

public class SendMessageViewImpl extends BaseView<SendMessageView.ActionDelegate>
    implements SendMessageView
{
  private final ListBox functionControl;
  private final CellTable<Parameter> parametersControl;
  private final List<Parameter> parameters;

  @Inject
  public SendMessageViewImpl() {
    this.parameters = new ArrayList<Parameter>();

    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.add(new Label("Function"));

    this.functionControl = this.createFunctionListBox()
    verticalPanel.add(this.functionControl);

    this.parametersControl = this.createParameterTable();
    verticalPanel.add(this.parametersControl);

    this.setContentWidget(verticalPanel);

    this.populateFunctionList();
    this.populateParameterList();
  }

  private ListBox createFunctionListBox() {
    ListBox result = new ListBox();
    result.setVisibleItemCount(1);
    result.addChangeHandler(event -> {
      SendMessageViewImpl.this.populateParameterList();
    });

    return result;
  }

  private CellTable<Parameter> createParameterTable() {
    CellTable<Parameter> result = new CellTable<>();

    result.addColumn(new TextColumn<Parameter>() {
      @Override
      public String getValue(Parameter parameter) {
        return parameter.getName();
      }
    }, "Parameter");

    result.addColumn(new Column<Parameter, String>(new EditTextCell()) {
      @Override
      public String getValue(Parameter parameter) {
        return parameter.getValue();
      }
    }, "Value");

    return result;
  }

  private void populateFunctionList() {
    this.functionControl.addItem("");
    this.functionControl.addItem("compute0");
  }

  private void populateParameterList() {
    this.parameters.add(new Parameter("Param1"));
  }
}
