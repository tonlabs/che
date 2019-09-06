/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.requirejs;

import com.google.gwt.core.client.JavaScriptObject;
import java.util.HashMap;
import java.util.Map;

/**
 * A store of reference to javascript objects.
 *
 * @author "Mickaël Leduque"
 */
public class ModuleHolder {

  private Map<String, RequirejsModule> modules = new HashMap<>();

  public JavaScriptObject getModule(final String key) {
    if (!this.hasModule(key)) {
      throw new IllegalArgumentException("Module not found: " + key);
    }
    return this.modules.get(key);
  }

  public boolean hasModule(final String key) {
    return this.modules.containsKey(key);
  }

  void setModule(final String key, final RequirejsModule module) {
    this.modules.put(key, module);
  }
}
