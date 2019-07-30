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
import io.tonlabs.ide.action.OpenUrlAction;
import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.DefaultActionGroup;
import org.eclipse.che.ide.api.action.IdeActions;
import org.eclipse.che.ide.api.extension.Extension;
import org.eclipse.che.ide.api.icon.Icon;
import org.eclipse.che.ide.api.icon.IconRegistry;
import org.vectomatic.dom.svg.ui.SVGResource;

/** TON Project extension that registers actions and icons. */
@Extension(title = "TON Project Extension", version = "0.0.1")
public class TonProjectExtension {
  private final ActionManager actionManager;

  /**
   * Constructor.
   *
   * @param tonProjectResources the resources that contains our icon
   * @param iconRegistry the {@link IconRegistry} that is used to register our icon
   */
  @Inject
  public TonProjectExtension(
      TonProjectResources tonProjectResources,
      IconRegistry iconRegistry,
      ActionManager actionManager) {

    this.actionManager = actionManager;
    iconRegistry.registerIcon(
        new Icon(TON_CATEGORY + ".samples.category.icon", tonProjectResources.tonIcon()));
  }

  private void registerActions() {
    DefaultActionGroup helpMenu =
        (DefaultActionGroup) this.actionManager.getAction(IdeActions.GROUP_HELP);

    this.registerAction(
        helpMenu, "ideQuickStart", "Che IDE Quick Start", null, "https://ton.dev/quickstart");

    helpMenu.addSeparator();

    this.registerAction(
        helpMenu,
        "cCompiler",
        "LLVM C Compiler Features & Limitations",
        null,
        "https://ton.dev/guides?section=c-compiler");
    this.registerAction(
        helpMenu,
        "cCompilerTips",
        "Online LLVM Compiler Tips",
        null,
        "https://ton.dev/guides?section=online-c-compiler");
    this.registerAction(
        helpMenu, "tvmCLanguage", "С Language for TVM", null, "https://ton.dev/guides?section=c");

    helpMenu.addSeparator();

    this.registerAction(
        helpMenu,
        "solCompiler",
        "Solidity Compiler Features & Limitations",
        null,
        "https://ton.dev/guides?section=solidity-compiler");
    this.registerAction(
        helpMenu,
        "solCompilerTips",
        "Online Solidity Compiler Tips",
        null,
        "https://ton.dev/guides?section=online-solidity-compiler");
    this.registerAction(
        helpMenu,
        "tvmSolLanguage",
        "Solidity Language for TVM",
        null,
        "https://ton.dev/guides?section=solidity");

    helpMenu.addSeparator();

    this.registerAction(
        helpMenu,
        "tvmLinker",
        "Linker Command Line Options",
        null,
        "https://ton.dev/guides?section=tvm-linker");

    helpMenu.addSeparator();

    this.registerAction(helpMenu, "tonDev", "Visit TON Dev site", null, "https://ton.dev");
    this.registerAction(helpMenu, "tonDev", "Visit TON Labs site", null, "https://tonlabs.io");
  }

  private void registerAction(
      DefaultActionGroup group, String name, String text, SVGResource svgResource, String url) {
    OpenUrlAction action = new OpenUrlAction(text, svgResource, url);

    this.actionManager.registerAction(name, action);
    group.add(action, this.actionManager);
  }
}
