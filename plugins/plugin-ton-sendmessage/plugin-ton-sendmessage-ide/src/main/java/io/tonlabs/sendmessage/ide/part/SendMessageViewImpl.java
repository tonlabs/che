package io.tonlabs.sendmessage.ide.part;

import com.google.gwt.user.client.ui.Label;
import org.eclipse.che.ide.api.parts.base.BaseView;

public class SendMessageViewImpl extends BaseView<SendMessageView.ActionDelegate>
    implements SendMessageView {
  public SendMessageViewImpl() {
    Label label = new Label("Hello World");
    this.setContentWidget(label);
  }
}
