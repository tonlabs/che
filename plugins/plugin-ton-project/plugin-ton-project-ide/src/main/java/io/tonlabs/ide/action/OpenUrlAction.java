package io.tonlabs.ide.action;

import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.action.BaseAction;
import org.eclipse.che.ide.util.browser.BrowserUtils;
import org.vectomatic.dom.svg.ui.SVGResource;

public class OpenUrlAction extends BaseAction {
  private String url;

  public OpenUrlAction(String text, SVGResource svgResource, String url) {
    super(text, null, svgResource);

    this.url = url;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    BrowserUtils.openInNewTab(this.url);
  }
}
