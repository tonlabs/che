package io.tonlabs.sendmessage.ide.inject;

import com.google.gwt.inject.client.AbstractGinModule;
import io.tonlabs.sendmessage.ide.part.SendMessageView;
import io.tonlabs.sendmessage.ide.part.SendMessageViewImpl;
import org.eclipse.che.ide.api.extension.ExtensionGinModule;

@ExtensionGinModule
public class SendMessageModule extends AbstractGinModule {
  @Override
  protected void configure() {
    this.bind(SendMessageView.class).to(SendMessageViewImpl.class);
  }
}
