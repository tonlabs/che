package io.tonlabs.sendmessage.ide.action;

import org.eclipse.che.ide.api.action.ActionEvent;
import org.vectomatic.dom.svg.ui.SVGResource;

public abstract class TonDeployedContractDirAction extends TonProjectAction {
  TonDeployedContractDirAction(String text, String description, SVGResource svgResource) {
    super(text, description, svgResource);
  }

  @Override
  public void updateInPerspective(ActionEvent event) {
    super.updateInPerspective(event);

    event
        .getPresentation()
        .setEnabledAndVisible(
            event.getPresentation().isEnabled()
                && event.getPresentation().isVisible()
                && this.appContext.get().getResource().isFolder());
  }
}
