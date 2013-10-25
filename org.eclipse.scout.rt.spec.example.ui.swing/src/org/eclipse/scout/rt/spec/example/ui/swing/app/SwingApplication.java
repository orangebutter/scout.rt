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
package org.eclipse.scout.rt.spec.example.ui.swing.app;

import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.client.services.common.session.IClientSessionRegistryService;
import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.testing.ui.swing.JUnitSwingJob;
import org.eclipse.scout.rt.ui.swing.AbstractSwingApplication;
import org.eclipse.scout.rt.ui.swing.ISwingEnvironment;
import org.eclipse.scout.service.SERVICES;
import org.eclipse.scout.testing.client.TestingClientSessionRegistryService;
import org.eclipse.scout.testing.client.runner.ScoutClientTestRunner;

import com.bsiag.scout.rt.ui.swing.rayo.RayoSwingEnvironment;

/**
 * Dynamic swing application to build an ad-hoc application for testing
 */
public class SwingApplication extends AbstractSwingApplication {
  private static IScoutLogger logger = ScoutLogManager.getLogger(SwingApplication.class);
  private TestingClientSessionRegistryService m_testingClientSessionRegistryService = null;

  @Override
  protected Object startInSubject(IApplicationContext context) throws Exception {
    ScoutClientTestRunner.setDefaultClientSessionClass(TestEnvironmentClientSession.class);
    m_testingClientSessionRegistryService = TestingClientSessionRegistryService.registerTestingClientSessionRegistryService();
    new JUnitSwingJob(TestEnvironmentClientSession.class).schedule(200);
    return super.startInSubject(context);
  }

  @Override
  protected ISwingEnvironment createSwingEnvironment() {
    return new RayoSwingEnvironment();
  }

  @Override
  protected IClientSession getClientSession() {
    return SERVICES.getService(IClientSessionRegistryService.class).newClientSession(TestEnvironmentClientSession.class, initUserAgent());
  }

  @Override
  public void stop() {
    super.stop();
    TestingClientSessionRegistryService.unregisterTestingClientSessionRegistryService(m_testingClientSessionRegistryService);
  }
}
