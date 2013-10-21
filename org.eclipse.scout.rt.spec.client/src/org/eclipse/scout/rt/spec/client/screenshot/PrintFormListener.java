package org.eclipse.scout.rt.spec.client.screenshot;

import java.io.File;
import java.util.HashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.client.ClientSyncJob;
import org.eclipse.scout.rt.client.ui.form.FormEvent;
import org.eclipse.scout.rt.client.ui.form.FormListener;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.PrintDevice;

public class PrintFormListener implements FormListener {
  private static final IScoutLogger LOG = ScoutLogManager.getLogger(PrintFormListener.class);

  private final String m_contentType;
  private final File m_destinationFolder;

  /**
   *
   */
  public PrintFormListener(String contentType, File destinationFolder) {
    m_contentType = contentType;
    m_destinationFolder = destinationFolder;
  }

  public PrintFormListener(File destinationFolder) {
    this("image/jpg", destinationFolder);
  }

  public File getDestinationFolder() {
    return m_destinationFolder;
  }

  public String getContentType() {
    return m_contentType;
  }

  @Override
  public void formChanged(FormEvent e) throws ProcessingException {
    if (e.getType() == FormEvent.TYPE_ACTIVATED) {
      schedulePrintJob(e.getForm());
    }
    else if (e.getType() == FormEvent.TYPE_PRINTED) {
      e.getForm().doClose();
    }
  }

  private void schedulePrintJob(final IForm f) {
    new ClientSyncJob("print " + f.getClass().getSimpleName(), ClientSyncJob.getCurrentSession()) {

      @Override
      protected void runVoid(IProgressMonitor monitor) {
        printForm(f);
      }
    }.schedule();
  }

  protected void printForm(IForm f) {
    getDestinationFolder().mkdirs();
    String name = f.getClass().getName();
    String ext = getContentType().substring(getContentType().lastIndexOf("/") + 1);
    File out = new File(getDestinationFolder(), name + "." + ext);
    HashMap<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("file", out);
    parameters.put("contentType", getContentType());
    LOG.info("Printing form: {}", out.getPath());
    f.printForm(PrintDevice.File, parameters);
  }
}
