package org.eclipse.scout.rt.spec.example.ui.swing.form;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.IDNDSupport;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractLinkButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.checkbox.AbstractCheckBox;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.imagebox.AbstractImageField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.ui.UserAgentUtility;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.CancelButton;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.DetailBox;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.DetailBox.TabBox;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.DetailBox.TabBox.NoteBox;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.DetailBox.TabBox.NoteBox.NotesField;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.DetailBox.TabBox.SocialMediaBox;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.DetailBox.TabBox.SocialMediaBox.NewNameButton;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.DetailBox.TabBox.SocialMediaBox.SocialMediaField;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.GroupBox;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.GroupBox.AnonymousField;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.GroupBox.CompanyField;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.GroupBox.FirstNameField;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.GroupBox.NameField;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.GroupBox.PortraitField;
import org.eclipse.scout.rt.spec.example.ui.swing.form.TestPersonForm.MainBox.OkButton;

public class TestPersonForm extends AbstractForm {

  public TestPersonForm() throws ProcessingException {
    this(true);
  }

  protected TestPersonForm(boolean callInitializer) throws ProcessingException {
    super(callInitializer);
  }

  @Override
  protected String getConfiguredCancelVerificationText() {
    return TEXTS.get("FormSaveChangesQuestion");
  }

  @Override
  protected String getConfiguredDoc() {
    return TEXTS.get("PersonForm");
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Person");
  }

  public void startNew() throws ProcessingException {
    startInternal(new TestPersonForm.NewHandler());
  }

  public AnonymousField getAnonymousField() {
    return getFieldByClass(AnonymousField.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public CompanyField getCompanyField() {
    return getFieldByClass(CompanyField.class);
  }

  public DetailBox getDetailBox() {
    return getFieldByClass(DetailBox.class);
  }

  public FirstNameField getFirstNameField() {
    return getFieldByClass(FirstNameField.class);
  }

  public GroupBox getGroupBox() {
    return getFieldByClass(GroupBox.class);
  }

  public MainBox getMainBox() {
    return (MainBox) getRootGroupBox();
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  public NewNameButton getNewNameButton() {
    return getFieldByClass(NewNameButton.class);
  }

  public NoteBox getNoteBox() {
    return getFieldByClass(NoteBox.class);
  }

  public NotesField getNotesField() {
    return getFieldByClass(NotesField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public PortraitField getPortraitField() {
    return getFieldByClass(PortraitField.class);
  }

  public SocialMediaBox getSocialMediaBox() {
    return getFieldByClass(SocialMediaBox.class);
  }

  public SocialMediaField getSocialMediaField() {
    return getFieldByClass(SocialMediaField.class);
  }

  public TabBox getTabBox() {
    return getFieldByClass(TabBox.class);
  }

  @Order(10.0f)
  public class MainBox extends AbstractGroupBox {

    @Order(20.0)
    public class GroupBox extends AbstractGroupBox {

      @Order(10.0)
      public class NameField extends AbstractStringField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Name");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return 60;
        }
      }

      @Order(20.0)
      public class FirstNameField extends AbstractStringField {

        @Override
        protected String getConfiguredDoc() {
          return TEXTS.get("PersonForm.MainBox.GroupBox.FirstNameField");
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("FirstName");
        }

        @Override
        protected int getConfiguredMaxLength() {
          return 60;
        }
      }

      @Order(30.0)
      public class CompanyField extends AbstractSmartField<Long> {

        @Override
        protected String getConfiguredDoc() {
          return TEXTS.get("PersonForm.MainBox.GroupBox.CompanyField");
        }

        @Order(50.0f)
        public class SeparatorMenu extends AbstractMenu {

          @Override
          protected boolean getConfiguredSeparator() {
            return true;
          }
        }

        @Order(60.0f)
        public class NewMenu extends AbstractMenu {

          @Override
          protected String getConfiguredDoc() {
            return TEXTS.get("PersonForm.MainBox.GroupBox.CompanyField.NewMenu");
          }

        }
      }

      @Order(40.0)
      public class AnonymousField extends AbstractCheckBox {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("MakeAnonymous");
        }
      }

      @Order(60.0)
      public class PortraitField extends AbstractImageField {

        @Override
        protected boolean getConfiguredAutoFit() {
          return true;
        }

        @Override
        protected int getConfiguredDragType() {
          return IDNDSupport.TYPE_FILE_TRANSFER;
        }

        @Override
        protected int getConfiguredDropType() {
          return IDNDSupport.TYPE_FILE_TRANSFER;
        }

        @Override
        protected boolean getConfiguredFocusVisible() {
          return false;
        }

        @Override
        protected boolean getConfiguredFocusable() {
          return false;
        }

        @Override
        protected int getConfiguredGridH() {
          return 7;
        }

        @Override
        protected double getConfiguredGridWeightY() {
          return 0;
        }

        @Override
        protected int getConfiguredHorizontalAlignment() {
          return 1;
        }

        @Override
        protected boolean getConfiguredLabelVisible() {
          return false;
        }

        @Override
        protected int getConfiguredVerticalAlignment() {
          return -1;
        }

        @Override
        protected void execInitField() throws ProcessingException {
          if (!UserAgentUtility.isDesktopDevice()) {
            setVisibleGranted(false);
          }
        }

        public void insertImage(byte[] content, String path)
            throws ProcessingException {
        }

        public void updateImage() throws ProcessingException {
        }

        public void updateImageLater() {
        }

        @Order(10)
        public class AddMenu extends AbstractMenu {

          @Override
          protected String getConfiguredText() {
            return TEXTS.get("AddPortaitMenu_");
          }

          @Override
          protected void execPrepareAction()
              throws ProcessingException {
            setVisible(getPortraitField().isEnabled());
          }
        }

        @Order(20)
        public class DeleteMenu extends AbstractMenu {

          @Override
          protected String getConfiguredText() {
            return TEXTS.get("Delete_");
          }

          @Override
          protected void execPrepareAction() throws ProcessingException {
            setVisible(getPortraitField().isEnabled() && getPortraitField().getImage() != null);
          }
        }
      }
    }

    @Order(30.0)
    public class DetailBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("InformationOnPerson");
      }

      @Order(40.0)
      public class TabBox extends AbstractTabBox {

        @Order(20.0)
        public class NoteBox extends AbstractGroupBox {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Notes");
          }

          @Order(10.0f)
          public class NotesField extends AbstractStringField {

            @Override
            protected int getConfiguredGridH() {
              return 7;
            }

            @Override
            protected int getConfiguredGridW() {
              return 2;
            }

            @Override
            protected boolean getConfiguredLabelVisible() {
              return false;
            }

            @Override
            protected int getConfiguredMaxLength() {
              return 4000;
            }

            @Override
            protected boolean getConfiguredMultilineText() {
              return true;
            }

            @Override
            protected boolean getConfiguredWrapText() {
              return true;
            }
          }
        }

        @Order(40.0)
        public class SocialMediaBox extends AbstractGroupBox {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("SocialMedia");
          }

          @Order(10.0)
          public class SocialMediaField extends AbstractTableField<SocialMediaField.Table> {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("SocialMedia");
            }

            @Override
            protected boolean getConfiguredLabelVisible() {
              return false;
            }

            @Order(10.0)
            public class Table extends AbstractTable {

              @Order(10.0)
              public class ServiceColumn extends
                  AbstractSmartColumn<Long> {

                @Override
                protected Class<? extends ICodeType<Long>> getConfiguredCodeType() {
                  return LanguageCodeType.class;
                }

                @Override
                protected boolean getConfiguredEditable() {
                  return true;
                }

                @Override
                protected String getConfiguredHeaderText() {
                  return TEXTS.get("Service");
                }

                @Override
                protected boolean getConfiguredMandatory() {
                  return true;
                }

                @Override
                protected boolean getConfiguredPrimaryKey() {
                  return true;
                }

                @Override
                protected int getConfiguredWidth() {
                  return 100;
                }
              }

              @Order(20.0)
              public class NameColumn extends
                  AbstractStringColumn {

                @Override
                protected boolean getConfiguredEditable() {
                  return true;
                }

                @Override
                protected String getConfiguredHeaderText() {
                  return TEXTS.get("Name");
                }

                @Override
                protected boolean getConfiguredMandatory() {
                  return true;
                }

                @Override
                protected int getConfiguredMaxLength() {
                  return 250;
                }

                @Override
                protected boolean getConfiguredPrimaryKey() {
                  return true;
                }

                @Override
                protected int getConfiguredWidth() {
                  return 200;
                }
              }

              @Order(30.0)
              public class OldNameColumn extends AbstractStringColumn {

                @Override
                protected boolean getConfiguredDisplayable() {
                  return false;
                }
              }

              @Order(10.0)
              public class OpenInBrowserMenu extends AbstractMenu {

                @Override
                protected String getConfiguredText() {
                  return TEXTS.get("OpenInBrowser");
                }
              }

              @Order(20.0)
              public class DeleteMenu extends AbstractMenu {

                @Override
                protected String getConfiguredKeyStroke() {
                  return "delete";
                }

                @Override
                protected boolean getConfiguredMultiSelectionAction() {
                  return true;
                }

                @Override
                protected String getConfiguredText() {
                  return TEXTS.get("DeleteName");
                }

                @Override
                protected void execAction()
                    throws ProcessingException {
                  for (ITableRow r : getSelectedRows()) {
                    deleteRow(r);
                  }
                }
              }
            }
          }

          @Order(20.0)
          public class NewNameButton extends AbstractLinkButton {

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("NewName_");
            }
          }
        }
      }
    }

    @Order(60.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(70.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class NewHandler extends AbstractFormHandler {
  }
}
