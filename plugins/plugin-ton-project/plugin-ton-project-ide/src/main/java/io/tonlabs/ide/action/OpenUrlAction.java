package io.tonlabs.ide.action;

import com.google.gwt.user.client.Window;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.action.BaseAction;
import org.vectomatic.dom.svg.ui.SVGResource;

public class OpenUrlAction extends BaseAction {
  private String url;

  public OpenUrlAction(String text, SVGResource svgResource, String url) {
    super(text, null, svgResource);

    this.url = url;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Window.open(this.url, "_blank", null);
  }
}
