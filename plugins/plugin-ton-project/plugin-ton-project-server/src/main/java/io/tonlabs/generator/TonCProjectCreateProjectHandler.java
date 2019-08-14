package io.tonlabs.generator;

import static io.tonlabs.shared.Constants.TON_C_PROJECT_TYPE_ID;
import static org.eclipse.che.api.fs.server.WsPathUtils.resolve;

import com.google.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.fs.server.FsManager;
import org.eclipse.che.api.fs.server.WsPathUtils;
import org.eclipse.che.api.project.server.handlers.CreateProjectHandler;
import org.eclipse.che.api.project.server.type.AttributeValue;

/**
 * Generates a new project which contains a package.json with default content and a default
 * person.json file within an myJsonFiles folder.
 */
public class TonCProjectCreateProjectHandler implements CreateProjectHandler {

  @Inject private FsManager fsManager;

  private static final String TEMPLATE_DIR = "templates/c/";

  private static final String C_FILE_NAME = "hello_world.c";
  private static final String ABI_FILE_NAME = "hello_world.abi";

  private void createFromTemplate(String projectPath, String fileName)
      throws ConflictException, ServerException {

    try (InputStream inputStream =
        this.getClass().getClassLoader().getResourceAsStream(TEMPLATE_DIR + fileName)) {
      String projectWsPath = WsPathUtils.absolutize(projectPath);
      this.fsManager.createFile(resolve(projectWsPath, fileName), inputStream);
    } catch (IOException | NotFoundException e) {
      throw new ServerException(e.getLocalizedMessage(), e);
    }
  }

  @Override
  public void onCreateProject(
      String projectPath, Map<String, AttributeValue> attributes, Map<String, String> options)
      throws ConflictException, ServerException {

    this.createFromTemplate(projectPath, C_FILE_NAME);
    this.createFromTemplate(projectPath, ABI_FILE_NAME);
  }

  @Override
  public String getProjectType() {
    return TON_C_PROJECT_TYPE_ID;
  }
}
