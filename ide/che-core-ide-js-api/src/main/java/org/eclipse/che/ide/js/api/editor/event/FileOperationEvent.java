/*
 * Copyright (c) 2012-2017 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */

package org.eclipse.che.ide.js.api.editor.event;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;
import org.eclipse.che.ide.api.editor.events.FileEvent.FileOperation;
import org.eclipse.che.ide.js.api.resources.VirtualFile;

/** @author Yevhen Vydolob */
@JsType
public class FileOperationEvent {

  @JsIgnore private final VirtualFile file;
  @JsIgnore private final FileOperation operation;

  @JsIgnore
  public FileOperationEvent(VirtualFile file, FileOperation operation) {
    this.file = file;
    this.operation = operation;
  }

  /** @return {@link VirtualFile} that represents an affected file */
  public VirtualFile getFile() {
    return file;
  }

  /** @return the type of operation performed with file */
  public FileOperation getOperationType() {
    return operation;
  }
}
