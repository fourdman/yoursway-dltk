package org.eclipse.dltk.validators.internal.ui;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.internal.ui.dialogs.StatusInfo;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.dltk.validators.ValidatorConfigurationPage;
import org.eclipse.dltk.validators.core.IValidator;
import org.eclipse.dltk.validators.core.IValidatorType;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class AddValidatorDialog extends StatusDialog {
	
	private IAddValidatorDialogRequestor fRequestor;
	
	private IValidatorType[] fValidatorTypes;
	
	private IValidatorType fSelectedValidatorType;
	
	private ComboDialogField fValidatorTypeCombo;
	
	private IValidator fEditedValidator;
	
	private StringDialogField fValidatorName;

	private IStatus[] fStati;
	private int fPrevIndex = -1;
	
	ValidatorConfigurationPage fConfigurationPage;
	
		
	public AddValidatorDialog(IAddValidatorDialogRequestor requestor, Shell shell,
			IValidatorType[] validatorTypes, IValidator editedValidator) {
		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		fRequestor= requestor;
		fStati= new IStatus[5];
		for (int i= 0; i < fStati.length; i++) {
			fStati[i]= new StatusInfo();
		}
		
		fValidatorTypes= validatorTypes;
		fSelectedValidatorType= editedValidator != null ? editedValidator.getValidatorType() : validatorTypes[0];
		
		fEditedValidator = editedValidator;
	}
	
	/**
	 * @see Windows#configureShell
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		//PlatformUI.getWorkbench().getHelpSystem().setHelp(newShell, IScriptDebugHelpContextIds.EDIT_ValidatorEnvironment_DIALOG);
	}		
	
	protected void createDialogFields() {
		fValidatorTypeCombo= new ComboDialogField(SWT.READ_ONLY);
		fValidatorTypeCombo.setLabelText(ValidatorMessages.addValidatorDialog_ValidatorEnvironmentType); 
		fValidatorTypeCombo.setItems(getValidatorTypeNames());
	
		fValidatorName= new StringDialogField();
		fValidatorName.setLabelText(ValidatorMessages.addValidatorDialog_ValidatorEnvironmentName); 
	}
	
	protected void createFieldListeners() {
		
		fValidatorTypeCombo.setDialogFieldListener(new IDialogFieldListener() {
			public void dialogFieldChanged(DialogField field) {
				updateValidatorType();
			}
		});
		
		fValidatorName.setDialogFieldListener(new IDialogFieldListener() {
			public void dialogFieldChanged(DialogField field) {
				setValidatorNameStatus(validateValidatorName());
				updateStatusLine();
			}
		});
	}
	
	protected String getValidatorName() {
		return fValidatorName.getText();
	}
		
	protected Control createDialogArea(Composite ancestor) {
		createDialogFields();
		Composite parent = (Composite)super.createDialogArea(ancestor);
		((GridLayout)parent.getLayout()).numColumns= 3;
		
		fValidatorTypeCombo.doFillIntoGrid(parent, 3);
		((GridData)fValidatorTypeCombo.getComboControl(null).getLayoutData()).widthHint= convertWidthInCharsToPixels(50);
		
		fValidatorName.doFillIntoGrid(parent, 3);
		
		if( this.fEditedValidator != null ) {
			fValidatorName.setEnabled(!this.fEditedValidator.getValidatorType().isBuiltin());
			if( this.fEditedValidator.getName().equals(this.fEditedValidator.getValidatorType().getName())) {
					
			}
			try {
				fConfigurationPage = ValidatorConfigurationPageManager.getConfigurationPage(fEditedValidator.getValidatorType().getID());
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if( fConfigurationPage != null ) {
				this.fConfigurationPage.setValidator(this.fEditedValidator);
				this.fConfigurationPage.createControl(parent, 3);
			}
		}
		
		initializeFields();
		createFieldListeners();
		applyDialogFont(parent);
		return parent;
	}
	
	private void updateValidatorType() {
		int selIndex= fValidatorTypeCombo.getSelectionIndex();
		if (selIndex == fPrevIndex) {
			return;
		}
		fPrevIndex = selIndex;
		if (selIndex >= 0 && selIndex < fValidatorTypes.length) {
			fSelectedValidatorType= fValidatorTypes[selIndex];
		}
//		setValidatorLocationStatus(validateValidatorLocation());
	
		updateStatusLine();
	}	
	
	public void create() {
		super.create();
		fValidatorName.setFocus();
		selectValidatorType();  
	}
	
	private String[] getValidatorTypeNames() {
		String[] names=  new String[fValidatorTypes.length];
		for (int i= 0; i < fValidatorTypes.length; i++) {
			names[i]= fValidatorTypes[i].getName();
		}
		return names;
	}
	
	private void selectValidatorType() {
		for (int i= 0; i < fValidatorTypes.length; i++) {
			if (fSelectedValidatorType == fValidatorTypes[i]) {
				fValidatorTypeCombo.selectItem(i);
				return;
			}
		}
	}

	private void initializeFields() {
		fValidatorTypeCombo.setItems(getValidatorTypeNames());
		if (fEditedValidator == null) {
			fValidatorName.setText(""); //$NON-NLS-1$
			
		} else {
			fValidatorTypeCombo.setEnabled(false);
			fValidatorName.setText(fEditedValidator.getName());
		}
		setValidatorNameStatus(validateValidatorName());
		updateStatusLine();
	}


	private IValidatorType getValidatorType() {
		return fSelectedValidatorType;
	}
	

	private IStatus validateValidatorName() {
		StatusInfo status= new StatusInfo();
		String name= fValidatorName.getText();
		if (name == null || name.trim().length() == 0) {
			status.setInfo(ValidatorMessages.addValidatorDialog_enterName); 
		} else {
			if (fRequestor.isDuplicateName(name) && (fEditedValidator == null || !name.equals(fEditedValidator.getName()))) {
				status.setError(ValidatorMessages.addValidatorDialog_duplicateName); 
			} 
		}
		return status;
	}
	
	public void updateStatusLine() {
		IStatus max= null;
		for (int i= 0; i < fStati.length; i++) {
			IStatus curr= fStati[i];
			if (curr.matches(IStatus.ERROR)) {
				updateStatus(curr);
				return;
			}
			if (max == null || curr.getSeverity() > max.getSeverity()) {
				max= curr;
			}
		}
		updateStatus(max);
	}
	
	protected void okPressed() {
		doOkPressed();
		super.okPressed();
	}
	
	private void doOkPressed() {
		if (fEditedValidator == null) {
			IValidator Validator = fSelectedValidatorType.createValidator( createUniqueId(fSelectedValidatorType));
			setFieldValuesToValidator(Validator);
			fRequestor.validatorAdded(Validator);
		} else {
			setFieldValuesToValidator(fEditedValidator);
		}
	}
	
	private String createUniqueId(IValidatorType ValidatorType) {
		String id= null;
		do {
			id= String.valueOf(System.currentTimeMillis());
		} while (ValidatorType.findValidator(id) != null);
		return id;
	}
	
	protected void setFieldValuesToValidator(IValidator validator) {
		validator.setName(fValidatorName.getText());
		if( this.fConfigurationPage != null ) {
			this.fConfigurationPage.applyChanges();
		}
	}
	
	protected File getAbsoluteFileOrEmpty(String path) {
		if (path == null || path.length() == 0) {
			return new File(""); //$NON-NLS-1$
		}
		return new File(path).getAbsoluteFile();
	}
	
	private void setValidatorNameStatus(IStatus status) {
		fStati[0]= status;
	}
	
	private void setValidatorLocationStatus(IStatus status) {
		fStati[1]= status;
	}
	
	protected IStatus getSystemLibraryStatus() {
		return fStati[3];
	}
	
	public void setSystemLibraryStatus(IStatus status) {
		fStati[3]= status;
	}
	
	/**
	 * Updates the status of the ok button to reflect the given status.
	 * Subclasses may override this method to update additional buttons.
	 * @param status the status.
	 */
	protected void updateButtonsEnableState(IStatus status) {
		Button ok = getButton(IDialogConstants.OK_ID);
		if (ok != null && !ok.isDisposed())
			ok.setEnabled(status.getSeverity() == IStatus.OK);
	}	
	
	/**
	 * @see org.eclipse.jface.dialogs.Dialog#setButtonLayoutData(org.eclipse.swt.widgets.Button)
	 */
	public void setButtonLayoutData(Button button) {
		super.setButtonLayoutData(button);
	}
	
	/**
	 * Returns the name of the section that this dialog stores its settings in
	 * 
	 * @return String
	 */
	protected String getDialogSettingsSectionName() {
		return "ADD_Validator_DIALOG_SECTION"; //$NON-NLS-1$
	}
	
	 /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#getDialogBoundsSettings()
     */
//    protected IDialogSettings getDialogBoundsSettings() {
//    	 IDialogSettings settings = ValidatorsUI.getDefault().getDialogSettings();
//         IDialogSettings section = settings.getSection(getDialogSettingsSectionName());
//         if (section == null) {
//             section = settings.addNewSection(getDialogSettingsSectionName());
//         } 
//         return section;
//    }
}