package io.tonlabs.sendmessage.ide.part;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.ide.api.parts.base.BasePresenter;

@Singleton
public class SendMessagePresenter extends BasePresenter implements SendMessageView.ActionDelegate {
  private SendMessageView view;

  @Inject
  private SendMessagePresenter(SendMessageView view) {
    this.view = view;
  }

  @Override
  public String getTitle() {
    return "Send Message";
  }

  @Override
  public IsWidget getView() {
    return this.view;
  }

  @Override
  public String getTitleToolTip() {
    return "Sends a message to the contract";
  }

  public void setVisible(boolean visible) {
    this.view.setVisible(visible);
  }

  @Override
  public void go(AcceptsOneWidget container) {
    container.setWidget(this.getView());
  }
}
