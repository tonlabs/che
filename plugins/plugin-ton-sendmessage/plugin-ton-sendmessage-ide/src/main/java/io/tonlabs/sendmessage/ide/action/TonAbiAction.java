package io.tonlabs.sendmessage.ide.action;

import org.eclipse.che.ide.api.action.ActionEvent;
import org.vectomatic.dom.svg.ui.SVGResource;

public abstract class TonAbiAction extends TonProjectAction {
  private static final String ABI_SUFFIX = ".abi";

  public TonAbiAction(String text, String description, SVGResource svgResource) {
    super(text, description, svgResource);
  }

  @Override
  public void updateInPerspective(ActionEvent event) {
    super.updateInPerspective(event);

    if (!event.getPresentation().isEnabled() || !event.getPresentation().isVisible()) {
      return;
    }

    event
        .getPresentation()
        .setEnabledAndVisible(this.appContext.get().getResource().getName().endsWith(ABI_SUFFIX));
  }
}
