package io.tonlabs.inject;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import io.tonlabs.generator.TonCProjectCreateProjectHandler;
import io.tonlabs.generator.TonSolProjectCreateProjectHandler;
import io.tonlabs.projecttype.TonCProjectType;
import io.tonlabs.projecttype.TonSolProjectType;
import org.eclipse.che.api.project.server.handlers.ProjectHandler;
import org.eclipse.che.api.project.server.type.ProjectTypeDef;
import org.eclipse.che.inject.DynaModule;

/** TON Project Guice module for setting up project type, project wizard and service bindings. */
@DynaModule
public class TonProjectGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    Multibinder<ProjectTypeDef> projectTypeDefMultibinder =
        newSetBinder(this.binder(), ProjectTypeDef.class);
    projectTypeDefMultibinder.addBinding().to(TonCProjectType.class);
    projectTypeDefMultibinder.addBinding().to(TonSolProjectType.class);

    Multibinder<ProjectHandler> projectHandlerMultibinder =
        newSetBinder(this.binder(), ProjectHandler.class);
    projectHandlerMultibinder.addBinding().to(TonCProjectCreateProjectHandler.class);
    projectHandlerMultibinder.addBinding().to(TonSolProjectCreateProjectHandler.class);
  }
}
