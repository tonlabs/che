package io.tonlabs.sendmessage.ide;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import org.vectomatic.dom.svg.ui.SVGResource;

public interface SendMessageResources extends ClientBundle {

  SendMessageResources INSTANCE = GWT.create(SendMessageResources.class);

  @Source("svg/ton.svg")
  SVGResource tonIcon();
}
