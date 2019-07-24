package io.tonlabs.sendmessage.ide.part;

import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import org.eclipse.che.ide.api.parts.base.BaseView;

public class SendMessageViewImpl extends BaseView<SendMessageView.ActionDelegate>
    implements SendMessageView {

  @Inject
  public SendMessageViewImpl() {
    Label label = new Label("Hello World");
    this.setContentWidget(label);
  }
}
