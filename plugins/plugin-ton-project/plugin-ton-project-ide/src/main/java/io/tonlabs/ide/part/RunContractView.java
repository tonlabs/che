package io.tonlabs.ide.part;

import com.google.inject.ImplementedBy;
import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.api.parts.base.BaseActionDelegate;
import org.eclipse.che.ide.api.resources.Folder;

@ImplementedBy(RunContractViewImpl.class)
public interface RunContractView extends View<RunContractView.ActionDelegate> {
  void setVisible(boolean visible);

  void updateDeploymentFolder(Folder deploymentFolder);

  interface ActionDelegate extends BaseActionDelegate {}
}
