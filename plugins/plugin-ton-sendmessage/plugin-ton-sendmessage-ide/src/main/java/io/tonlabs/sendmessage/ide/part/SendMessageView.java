package io.tonlabs.sendmessage.ide.part;

import com.google.inject.ImplementedBy;
import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.api.parts.base.BaseActionDelegate;
import org.eclipse.che.ide.api.resources.File;

@ImplementedBy(SendMessageViewImpl.class)
public interface SendMessageView extends View<SendMessageView.ActionDelegate> {
  void setVisible(boolean visible);

  void updateAbi(File abiFile);

  interface ActionDelegate extends BaseActionDelegate {}
}
