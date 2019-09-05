package io.tonlabs.ide;

import static io.tonlabs.shared.Constants.TON_CATEGORY;

import com.google.inject.Inject;
import io.tonlabs.ide.action.OpenUrlAction;
import io.tonlabs.ide.action.RunContractAction;
import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.DefaultActionGroup;
import org.eclipse.che.ide.api.action.IdeActions;
import org.eclipse.che.ide.api.extension.Extension;
import org.eclipse.che.ide.api.icon.Icon;
import org.eclipse.che.ide.api.icon.IconRegistry;
import org.vectomatic.dom.svg.ui.SVGResource;

// import io.tonlabs.ide.action.AccountStateTvcAction;

/** TON Project extension that registers actions and icons. */
@Extension(title = "TON Project Extension", version = "0.0.1")
public class TonProjectExtension {
  private final ActionManager actionManager;
  private final RunContractAction runContractAction;
  //  private final AccountStateTvcAction accountStateTvcAction;

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
      ActionManager actionManager,
      RunContractAction runContractAction /*,
      AccountStateTvcAction accountStateTvcAction*/) {
    this.actionManager = actionManager;
    this.runContractAction = runContractAction;
    //    this.accountStateTvcAction = accountStateTvcAction;

    iconRegistry.registerIcon(
        new Icon(TON_CATEGORY + ".ton.category.icon", tonProjectResources.tonIcon()));

    this.registerActions();
  }

  private void registerActions() {
    DefaultActionGroup mainContextMenuGroup =
        (DefaultActionGroup) this.actionManager.getAction(IdeActions.GROUP_MAIN_CONTEXT_MENU);

    this.actionManager.registerAction("runContractAction", this.runContractAction);
    mainContextMenuGroup.add(this.runContractAction);

    //    this.actionManager.registerAction("accountStateTvcAction", this.accountStateTvcAction);
    //    mainContextMenuGroup.add(this.accountStateTvcAction);

    DefaultActionGroup helpMenu =
        (DefaultActionGroup) this.actionManager.getAction(IdeActions.GROUP_HELP);

    this.registerAction(
        helpMenu,
        "ideQuickStart",
        "Che IDE Quick Start",
        TonProjectResources.INSTANCE.tonDevIcon(),
        "https://ton.dev/quickstart");

    helpMenu.addSeparator();

    DefaultActionGroup cLangGroup =
        new DefaultActionGroup("Development Using C Language", true, this.actionManager);
    helpMenu.add(cLangGroup);

    this.registerAction(
        cLangGroup,
        "cCompiler",
        "LLVM C Compiler Features & Limitations",
        TonProjectResources.INSTANCE.tonDevIcon(),
        "https://ton.dev/guides?section=c-compiler");
    this.registerAction(
        cLangGroup,
        "cCompilerTips",
        "Online LLVM Compiler Tips",
        TonProjectResources.INSTANCE.tonDevIcon(),
        "https://ton.dev/guides?section=online-c-compiler");
    this.registerAction(
        cLangGroup,
        "tvmCLanguage",
        "C Language for TVM",
        TonProjectResources.INSTANCE.tonDevIcon(),
        "https://ton.dev/guides?section=c");

    DefaultActionGroup solLangGroup =
        new DefaultActionGroup("Development Using Solidity Language", true, this.actionManager);
    helpMenu.add(solLangGroup);

    this.registerAction(
        solLangGroup,
        "solCompiler",
        "Solidity Compiler Features & Limitations",
        TonProjectResources.INSTANCE.tonDevIcon(),
        "https://ton.dev/guides?section=solidity-compiler");
    this.registerAction(
        solLangGroup,
        "solCompilerTips",
        "Online Solidity Compiler Tips",
        TonProjectResources.INSTANCE.tonDevIcon(),
        "https://ton.dev/guides?section=online-solidity-compiler");
    this.registerAction(
        solLangGroup,
        "tvmSolLanguage",
        "Solidity Language for TVM",
        TonProjectResources.INSTANCE.tonDevIcon(),
        "https://ton.dev/guides?section=solidity");

    helpMenu.addSeparator();

    this.registerAction(
        helpMenu,
        "tvmLinker",
        "Linker Command Line Options",
        TonProjectResources.INSTANCE.tonDevIcon(),
        "https://ton.dev/guides?section=tvm-linker");

    helpMenu.addSeparator();

    this.registerAction(
        helpMenu,
        "tonDev",
        "Visit the TON Dev Website",
        TonProjectResources.INSTANCE.tonDevIcon(),
        "https://ton.dev");
    this.registerAction(
        helpMenu,
        "tonLabs",
        "Visit the TON Labs Website",
        TonProjectResources.INSTANCE.tonLabsIcon(),
        "https://tonlabs.io");
  }

  private void registerAction(
      DefaultActionGroup group, String name, String text, SVGResource svgResource, String url) {

    OpenUrlAction action = new OpenUrlAction(text, svgResource, url);

    this.actionManager.registerAction(name, action);
    group.add(action);
  }
}
