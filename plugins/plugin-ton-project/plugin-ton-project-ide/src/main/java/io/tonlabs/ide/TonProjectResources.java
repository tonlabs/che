package io.tonlabs.ide;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import org.vectomatic.dom.svg.ui.SVGResource;

public interface TonProjectResources extends ClientBundle {

  TonProjectResources INSTANCE = GWT.create(TonProjectResources.class);

  @Source("svg/blank.svg")
  SVGResource blankIcon();

  @Source("svg/deploy.svg")
  SVGResource deployIcon();

  @Source("svg/run.svg")
  SVGResource runIcon();

  @Source("svg/samples.svg")
  SVGResource samplesIcon();

  @Source("svg/ton.svg")
  SVGResource tonIcon();

  @Source("svg/tondev.svg")
  SVGResource tonDevIcon();

  @Source("svg/tonlabs.svg")
  SVGResource tonLabsIcon();
}
