package io.tonlabs.ide.part;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import io.tonlabs.ide.TonProjectResources;
import org.eclipse.che.ide.api.parts.base.BasePresenter;
import org.vectomatic.dom.svg.ui.SVGResource;

public class RunContractPresenter extends BasePresenter implements RunContractView.ActionDelegate {
  private RunContractView view;

  @Inject
  private RunContractPresenter(RunContractView view) {
    this.view = view;
  }

  @Override
  public String getTitle() {
    return "Run Contract";
  }

  @Override
  public SVGResource getTitleImage() {
    return (TonProjectResources.INSTANCE.tonIcon());
  }

  @Override
  public IsWidget getView() {
    return this.view;
  }

  @Override
  public String getTitleToolTip() {
    return "Sends message to the contract";
  }

  public void setVisible(boolean visible) {
    this.view.setVisible(visible);
  }

  public RunContractView getRunContractView() {
    return this.view;
  }

  @Override
  public void go(AcceptsOneWidget container) {
    container.setWidget(this.getView());
    this.setVisible(true);
  }
}
