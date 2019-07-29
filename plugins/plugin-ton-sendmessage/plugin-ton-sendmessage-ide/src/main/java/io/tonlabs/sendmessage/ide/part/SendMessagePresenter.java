package io.tonlabs.sendmessage.ide.part;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.tonlabs.sendmessage.ide.SendMessageResources;
import org.eclipse.che.ide.api.parts.base.BasePresenter;
import org.vectomatic.dom.svg.ui.SVGResource;

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
  public SVGResource getTitleImage() {
    return (SendMessageResources.INSTANCE.tonIcon());
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

  public SendMessageView getSendMessageView() {
    return this.view;
  }

  @Override
  public void go(AcceptsOneWidget container) {
    container.setWidget(this.getView());
    this.setVisible(true);
  }
}
