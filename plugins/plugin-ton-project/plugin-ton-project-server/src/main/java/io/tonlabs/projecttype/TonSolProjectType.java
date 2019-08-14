package io.tonlabs.projecttype;

import com.google.inject.Inject;
import io.tonlabs.shared.Constants;
import org.eclipse.che.api.project.server.type.ProjectTypeDef;

/** The TON C project type. */
public class TonSolProjectType extends ProjectTypeDef {

  /** Constructor for the JSON example project type. */
  @Inject
  public TonSolProjectType() {
    super(Constants.TON_SOL_PROJECT_TYPE_ID, Constants.TON_SOL_PROJECT_CAPTION, true, false);
    this.addConstantDefinition(Constants.LANGUAGE, Constants.LANGUAGE, Constants.TON_SOL_LANG);
  }
}
