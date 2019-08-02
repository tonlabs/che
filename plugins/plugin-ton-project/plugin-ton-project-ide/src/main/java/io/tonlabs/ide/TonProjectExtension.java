/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package io.tonlabs.ide;

import static io.tonlabs.shared.Constants.TON_CATEGORY;

import com.google.inject.Inject;
import io.tonlabs.ide.action.SendMessageAction;
import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.DefaultActionGroup;
import org.eclipse.che.ide.api.action.IdeActions;
import org.eclipse.che.ide.api.extension.Extension;
import org.eclipse.che.ide.api.icon.Icon;
import org.eclipse.che.ide.api.icon.IconRegistry;

/** TON Project extension that registers actions and icons. */
@Extension(title = "TON Project Extension", version = "0.0.1")
public class TonProjectExtension {
  /**
   * Constructor.
   *
   * @param tonProjectResources the resources that contains our icon
   * @param iconRegistry the {@link IconRegistry} that is used to register our icon
   * @param actionManager the {@link ActionManager} that is used to register our actions
   * @param sendMessageAction the action that calls the example server service
   */
  @Inject
  public TonProjectExtension(
      TonProjectResources tonProjectResources,
      IconRegistry iconRegistry,
      ActionManager actionManager,
      SendMessageAction sendMessageAction) {
    iconRegistry.registerIcon(
        new Icon(TON_CATEGORY + ".samples.category.icon", tonProjectResources.tonIcon()));

    actionManager.registerAction("sendMessageAction", sendMessageAction);

    DefaultActionGroup mainContextMenuGroup =
        (DefaultActionGroup) actionManager.getAction(IdeActions.GROUP_MAIN_CONTEXT_MENU);
    mainContextMenuGroup.add(sendMessageAction);
  }
}
