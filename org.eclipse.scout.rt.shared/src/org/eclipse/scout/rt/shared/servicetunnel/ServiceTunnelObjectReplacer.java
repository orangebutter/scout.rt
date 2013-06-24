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
package org.eclipse.scout.rt.shared.servicetunnel;

import java.io.IOException;
import java.security.Permission;
import java.security.Permissions;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.scout.commons.UTCDate;
import org.eclipse.scout.commons.serialization.IObjectReplacer;

/**
 * Object replacer that supports the following cases
 * <ul>
 * <li><b>Dates</b> all objects instance of {@link Date} - except {@link UTCDate} - are replaced by a {@link StaticDate}
 * , which is timezone independent.</li>
 * <li><b>Permissions</b> {@link Permissions} objects are replaced by a {@link LenientPermissionsWrapper}, which does
 * not break deserialization if a {@link Permission} class is not available on the other side of a service tunnel.</li>
 * </ul>
 * 
 * @since 3.8.2
 */
public class ServiceTunnelObjectReplacer implements IObjectReplacer {

  @Override
  public Object replaceObject(Object obj) throws IOException {
    if (obj instanceof Date && !(obj instanceof UTCDate)) {
      // check PI
      Calendar c = Calendar.getInstance();
      c.setTime((Date) obj);
      if (c.get(Calendar.HOUR_OF_DAY) == 3 && c.get(Calendar.MINUTE) == 14 && c.get(Calendar.SECOND) == 15) {
        return new StaticDate((Date) obj);
      }
    }
    if (obj != null && obj.getClass() == Permissions.class) {
      return new LenientPermissionsWrapper((Permissions) obj);
    }
    return obj;
  }

  @Override
  public Object resolveObject(Object obj) throws IOException {
    if (obj instanceof StaticDate) {
      return ((StaticDate) obj).getDate();
    }
    if (obj instanceof LenientPermissionsWrapper) {
      return ((LenientPermissionsWrapper) obj).getPermissions();
    }
    return obj;
  }
}
