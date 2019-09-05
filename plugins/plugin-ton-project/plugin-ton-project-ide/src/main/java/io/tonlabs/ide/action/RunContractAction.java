package io.tonlabs.ide.action;

import com.google.inject.Inject;
import io.tonlabs.ide.TonProjectResources;
import io.tonlabs.ide.part.RunContractPresenter;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.parts.PartStackType;
import org.eclipse.che.ide.api.parts.WorkspaceAgent;

public class RunContractAction extends TonFolderAction {

  private final WorkspaceAgent workspaceAgent;
  private final RunContractPresenter runContractPresenter;

  /**
   * Constructor.
   *
   * @param workspaceAgent the workspace agent
   * @param runContractPresenter the presenter of the Run Contract part
   */
  @Inject
  public RunContractAction(
      final WorkspaceAgent workspaceAgent, final RunContractPresenter runContractPresenter) {
    super(
        "Run Contract...",
        "Runs method of the specified smart contract",
        TonProjectResources.INSTANCE.tonIcon());
    this.workspaceAgent = workspaceAgent;
    this.runContractPresenter = runContractPresenter;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.runContractPresenter
        .getRunContractView()
        .updateDeploymentFolder(this.appContext.get().getResource().asFolder());
    this.workspaceAgent.openPart(this.runContractPresenter, PartStackType.TOOLING);
    this.workspaceAgent.setActivePart(this.runContractPresenter);
  }
}
