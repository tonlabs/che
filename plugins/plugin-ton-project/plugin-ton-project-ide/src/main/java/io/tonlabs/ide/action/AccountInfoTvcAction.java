package io.tonlabs.ide.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.tonlabs.ide.TonProjectResources;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.command.CommandExecutor;
import org.eclipse.che.ide.api.command.CommandImpl;
import org.eclipse.che.ide.api.resources.File;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.resource.Path;

@Singleton
public class AccountInfoTvcAction extends TonProjectAction {
  private CommandExecutor commandExecutor;
  private File tvcFile;

  @Inject
  public AccountInfoTvcAction(CommandExecutor commandExecutor) {
    super(
        "Get Account Info",
        "Retrieve selected smart contract information from the TON blockchain",
        TonProjectResources.INSTANCE.tonIcon());

    this.commandExecutor = commandExecutor;
  }

  @Override
  public void updateInPerspective(ActionEvent event) {
    super.updateInPerspective(event);

    this.tvcFile = null;

    if (!event.getPresentation().isEnabled() || !event.getPresentation().isVisible()) {
      return;
    }

    Resource resource = this.appContext.get().getResource();
    if (!resource.isFile() || !resource.asFile().getExtension().equals("tvc")) {
      event.getPresentation().setEnabledAndVisible(false);

      return;
    }

    this.tvcFile = resource.asFile();

    event.getPresentation().setEnabledAndVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    @SuppressWarnings("StringBufferReplaceableByString")
    StringBuilder commandLine = new StringBuilder("cd ");
    commandLine.append(
        this.tvcFile.getParent().asFolder().getLocation().makeRelativeTo(Path.ROOT).toString());
    commandLine.append(" && ");
    commandLine.append("cat | ./test-lite-client -a 0:");
    commandLine.append(this.tvcFile.getNameWithoutExtension());
    commandLine.append(" -C ton-global.json");

    this.commandExecutor.executeCommand(
        new CommandImpl("Account Info", commandLine.toString(), "ton-account-info"));
  }
}
