package io.tonlabs.ide.inject;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.multibindings.GinMultibinder;
import io.tonlabs.ide.project.TonCProjectWizardRegistrar;
import io.tonlabs.ide.project.TonSolProjectWizardRegistrar;
import org.eclipse.che.ide.api.extension.ExtensionGinModule;
import org.eclipse.che.ide.api.project.type.wizard.ProjectWizardRegistrar;

/** TON Project Gin Module for binding the project wizard and helper factories. */
@ExtensionGinModule
public class TonProjectModule extends AbstractGinModule {

  @Override
  protected void configure() {
    GinMultibinder.newSetBinder(this.binder(), ProjectWizardRegistrar.class)
        .addBinding()
        .to(TonCProjectWizardRegistrar.class);

    GinMultibinder.newSetBinder(this.binder(), ProjectWizardRegistrar.class)
        .addBinding()
        .to(TonSolProjectWizardRegistrar.class);
  }
}
