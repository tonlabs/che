package io.tonlabs.ide.action;

import org.eclipse.che.ide.api.action.ActionEvent;
import org.vectomatic.dom.svg.ui.SVGResource;

public abstract class TonFolderAction extends TonProjectAction {
  TonFolderAction(String text, String description, SVGResource svgResource) {
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
