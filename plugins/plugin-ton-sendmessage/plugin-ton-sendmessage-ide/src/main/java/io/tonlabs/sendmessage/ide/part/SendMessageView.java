package io.tonlabs.sendmessage.ide.part;

import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.api.parts.base.BaseActionDelegate;

public interface SendMessageView extends View<SendMessageView.ActionDelegate> {
  void setVisible(boolean visible);

  interface ActionDelegate extends BaseActionDelegate {}
}
