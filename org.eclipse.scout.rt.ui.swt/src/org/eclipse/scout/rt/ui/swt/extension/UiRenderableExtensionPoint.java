/*******************************************************************************
 * Copyright (c) 2010 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipse.scout.rt.ui.swt.extension;

import java.util.ArrayList;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.ui.swt.Activator;
import org.eclipse.scout.rt.ui.swt.extension.internal.IUiRendererExtension;
import org.eclipse.scout.rt.ui.swt.extension.internal.UiRendererExtension;

/**
 *
 */
public class UiRenderableExtensionPoint {

  private static IScoutLogger LOG = ScoutLogManager.getLogger(UiRenderableExtensionPoint.class);

  private static final String EXTENSION_POINT_ID = "swtUiRenderer";
  private static final String ATTRIBUTE_RENDERER_NAME = "name";
  private static final String ATTRIBUTE_RENDERER_ACTIVE = "active";
  private static final String ATTRIBUTE_RENDERER_SCOPE = "scope";
  private static final String ATTRIBUTE_RENDERER_MODEL_CLASS = "modelClass";
  private static final String ATTRIBUTE_RENDERER_FACTORY = "rendererFactory";
  private static final String ATTRIBUTE_RENDERER_DIRECT_RENDERER = "directRenderer";
  private static final String ATTRIBUTE_RENDERER_PRIORITY = "priority";

  private UiRenderableExtensionPoint() {
  }

  public static IUiRendererExtension[] getUiRendererExtensions() {
    ArrayList<IUiRendererExtension> formFieldExtensionList = new ArrayList<IUiRendererExtension>();
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IExtensionPoint xp = reg.getExtensionPoint(Activator.PLUGIN_ID, EXTENSION_POINT_ID);
    IExtension[] extensions = xp.getExtensions();
    for (IExtension extension : extensions) {
      IConfigurationElement[] elements = extension.getConfigurationElements();
      for (IConfigurationElement element : elements) {
        String name = element.getAttribute(ATTRIBUTE_RENDERER_NAME);
        boolean active = Boolean.parseBoolean(element.getAttribute(ATTRIBUTE_RENDERER_ACTIVE));
        UiRendererExtension rendererExtension = new UiRendererExtension(name);
        rendererExtension.setContibuterBundleId(extension.getContributor().getName());
        rendererExtension.setActive(active);
        rendererExtension.setScope(getScopePriority(element.getAttribute(ATTRIBUTE_RENDERER_SCOPE)));
        rendererExtension.setModelClassName(element.getAttribute(ATTRIBUTE_RENDERER_MODEL_CLASS));
        double prio = -1;
        try {
          prio = Double.parseDouble(element.getAttribute(ATTRIBUTE_RENDERER_PRIORITY));
        }
        catch (NumberFormatException ex) {
          LOG.warn("Could not parse priority of ui renderer '" + name + "' in '" + extension.getContributor().getName() + "'.");
        }
        rendererExtension.setPriority(prio);
        rendererExtension.setFactoryClassName(getClassName(element.getChildren(ATTRIBUTE_RENDERER_FACTORY), "class"));
        rendererExtension.setUiClassName(getClassName(element.getChildren(ATTRIBUTE_RENDERER_DIRECT_RENDERER), "class"));
        formFieldExtensionList.add(rendererExtension);
      }
    }
    return formFieldExtensionList.toArray(new IUiRendererExtension[formFieldExtensionList.size()]);
  }

  private static String getClassName(IConfigurationElement[] elements, String attribute) {
    String clazzName = null;
    if (elements != null && elements.length == 1) {
      clazzName = elements[0].getAttribute(attribute);

    }
    return clazzName;
  }

  private static int getScopePriority(String scope) {
    int prio = IFormFieldExtension.SCOPE_DEFAULT;
    if (StringUtility.isNullOrEmpty(scope) || scope.equalsIgnoreCase("default")) {
      prio = IFormFieldExtension.SCOPE_DEFAULT;
    }
    else if (scope.equalsIgnoreCase("global")) {
      prio = IFormFieldExtension.SCOPE_GLOBAL;
    }
    else if (scope.equalsIgnoreCase("local")) {
      prio = IFormFieldExtension.SCOPE_LOCAL;
    }
    return prio;
  }

}
