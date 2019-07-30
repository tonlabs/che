package io.tonlabs.sendmessage.ide;

import com.google.inject.Inject;
import io.tonlabs.sendmessage.ide.action.SendMessageAction;
import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.DefaultActionGroup;
import org.eclipse.che.ide.api.action.IdeActions;
import org.eclipse.che.ide.api.extension.Extension;

@Extension(title = "TON Send Message Extension", version = "0.0.1")
public class SendMessageExtension {

  /**
   * Constructor.
   *
   * @param actionManager the {@link ActionManager} that is used to register our actions
   * @param sendMessageAction the action that calls the example server service
   */
  @Inject
  public SendMessageExtension(ActionManager actionManager, SendMessageAction sendMessageAction) {

    actionManager.registerAction("sendMessageAction", sendMessageAction);

    DefaultActionGroup mainContextMenuGroup =
        (DefaultActionGroup) actionManager.getAction(IdeActions.GROUP_MAIN_CONTEXT_MENU);
    mainContextMenuGroup.add(sendMessageAction);
  }
}
