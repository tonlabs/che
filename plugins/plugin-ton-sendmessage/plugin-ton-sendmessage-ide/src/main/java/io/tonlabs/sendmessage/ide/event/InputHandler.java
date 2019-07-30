package io.tonlabs.sendmessage.ide.event;

import com.google.gwt.event.shared.EventHandler;

public interface InputHandler extends EventHandler {
  void onInput(InputEvent event);
}
