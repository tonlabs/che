package io.tonlabs.sendmessage.ide.action;

import java.util.Collections;
import javax.validation.constraints.NotNull;
import org.eclipse.che.commons.annotation.Nullable;
import org.eclipse.che.ide.api.action.AbstractPerspectiveAction;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.resources.Project;
import org.eclipse.che.ide.api.resources.Resource;
import org.eclipse.che.ide.part.perspectives.project.ProjectPerspective;
import org.vectomatic.dom.svg.ui.SVGResource;

public abstract class TonProjectAction extends AbstractPerspectiveAction {
  private static final String SUPPORTED_PROJECT_TYPES[] = {
    "ton-c-project", "ton-solidity-project",
  };

  public TonProjectAction(
      @NotNull String text, @NotNull String description, @Nullable SVGResource svgResource) {
    super(
        Collections.singletonList(ProjectPerspective.PROJECT_PERSPECTIVE_ID),
        text,
        description,
        svgResource);
  }

  @Override
  public void updateInPerspective(ActionEvent event) {
    event
        .getPresentation()
        .setEnabledAndVisible(hasSupportedProject(this.appContext.get().getResources()));
  }

  private static boolean hasSupportedProject(Resource[] resources) {
    if (resources == null || resources.length < 1) {
      return false;
    }

    for (Resource resource : resources) {
      if (resource.getProject().exists() && isProjectTypeSupported(resource.getProject())) {
        return true;
      }
    }

    return false;
  }

  private static boolean isProjectTypeSupported(Project project) {
    for (String projectType : SUPPORTED_PROJECT_TYPES) {
      if (project.isTypeOf(projectType)) {
        return true;
      }
    }
    return false;
  }
}
