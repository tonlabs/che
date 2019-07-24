/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package io.tonlabs.sendmessage.ide.action;

import com.google.inject.Inject;
import io.tonlabs.sendmessage.ide.part.SendMessagePresenter;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.action.BaseAction;
import org.eclipse.che.ide.api.parts.PartStackType;
import org.eclipse.che.ide.api.parts.WorkspaceAgent;

public class SendMessageAction extends BaseAction {

  private final WorkspaceAgent workspaceAgent;
  private final SendMessagePresenter sendMessagePresenter;

  /**
   * Constructor.
   *
   * @param workspaceAgent the workspace agent
   * @param sendMessagePresenter the presenter of the Send Message part
   */
  @Inject
  public SendMessageAction(
      final WorkspaceAgent workspaceAgent, final SendMessagePresenter sendMessagePresenter) {
    super("Send Message...", "Sends a message to the specified smart contract");
    this.workspaceAgent = workspaceAgent;
    this.sendMessagePresenter = sendMessagePresenter;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.workspaceAgent.openPart(this.sendMessagePresenter, PartStackType.EDITING);
    this.workspaceAgent.setActivePart(this.sendMessagePresenter);
  }
}
