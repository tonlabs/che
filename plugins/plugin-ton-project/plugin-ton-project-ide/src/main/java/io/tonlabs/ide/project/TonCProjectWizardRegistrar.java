package io.tonlabs.ide.project;

import com.google.inject.Provider;
import io.tonlabs.shared.Constants;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.eclipse.che.ide.api.project.MutableProjectConfig;
import org.eclipse.che.ide.api.project.type.wizard.ProjectWizardRegistrar;
import org.eclipse.che.ide.api.wizard.WizardPage;

/** The project wizard for creating a new TON C project. */
public class TonCProjectWizardRegistrar implements ProjectWizardRegistrar {

  private final List<Provider<? extends WizardPage<MutableProjectConfig>>> wizardPagesProviders;

  /** Constructor for TON C Project wizard. */
  public TonCProjectWizardRegistrar() {
    this.wizardPagesProviders = new ArrayList<>();
  }

  @Override
  @NotNull
  public String getProjectTypeId() {
    return Constants.TON_C_PROJECT_TYPE_ID;
  }

  @Override
  @NotNull
  public String getCategory() {
    return Constants.TON_CATEGORY;
  }

  @Override
  @NotNull
  public List<Provider<? extends WizardPage<MutableProjectConfig>>> getWizardPages() {
    return this.wizardPagesProviders;
  }
}
