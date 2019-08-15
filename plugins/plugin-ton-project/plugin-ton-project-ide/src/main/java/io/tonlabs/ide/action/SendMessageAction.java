package io.tonlabs.ide.action;

import com.google.inject.Inject;
import io.tonlabs.ide.TonProjectResources;
import io.tonlabs.ide.part.SendMessagePresenter;
import io.tonlabs.ide.sdk.TonSDK;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.parts.PartStackType;
import org.eclipse.che.ide.api.parts.WorkspaceAgent;

public class SendMessageAction extends TonFolderAction {

  private final WorkspaceAgent workspaceAgent;
  private final SendMessagePresenter sendMessagePresenter;

  /**
   * Constructor.
   *
   * @param workspaceAgent the workspace agent
   * @param sendMessagePresenter the presenter of the Send Message part
   */
  @Inject
  public SendMessageAction(
      final WorkspaceAgent workspaceAgent, final SendMessagePresenter sendMessagePresenter) {
    super(
        "Send Message...",
        "Sends message to the specified smart contract",
        TonProjectResources.INSTANCE.tonIcon());
    this.workspaceAgent = workspaceAgent;
    this.sendMessagePresenter = sendMessagePresenter;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    TonSDK sdk = new TonSDK();
    sdk.init();

    this.sendMessagePresenter
        .getSendMessageView()
        .updateDeploymentFolder(this.appContext.get().getResource().asFolder());
    this.workspaceAgent.openPart(this.sendMessagePresenter, PartStackType.TOOLING);
    this.workspaceAgent.setActivePart(this.sendMessagePresenter);
  }
}
