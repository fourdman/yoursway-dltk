/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/

package org.eclipse.dltk.ui.preferences;

import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

/**
 * Abstract preference page which is used to wrap
 */
public abstract class AbstractConfigurationBlockPreferencePage extends
		PreferencePage implements IWorkbenchPreferencePage {

	private IPreferenceConfigurationBlock fConfigurationBlock;
	private OverlayPreferenceStore fOverlayStore;

	public AbstractConfigurationBlockPreferencePage() {
		setDescription();
		setPreferenceStore();

		fOverlayStore = new OverlayPreferenceStore(getPreferenceStore(),
				new OverlayPreferenceStore.OverlayKey[] {});
		fConfigurationBlock = createConfigurationBlock(fOverlayStore);
	}

	public void init(IWorkbench workbench) {
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
				getHelpId());
	}

	protected Control createContents(Composite parent) {
		fOverlayStore.load();
		fOverlayStore.start();

		Control content = fConfigurationBlock.createControl(parent);

		initialize();

		Dialog.applyDialogFont(content);
		return content;
	}

	private void initialize() {
		fConfigurationBlock.initialize();
	}

	public boolean performOk() {
		fConfigurationBlock.performOk();
		fOverlayStore.propagate();

		DLTKUIPlugin.getDefault().savePluginPreferences();

		return true;
	}

	public void performDefaults() {
		fOverlayStore.loadDefaults();
		fConfigurationBlock.performDefaults();

		super.performDefaults();
	}

	public void dispose() {
		fConfigurationBlock.dispose();

		if (fOverlayStore != null) {
			fOverlayStore.stop();
			fOverlayStore = null;
		}

		super.dispose();
	}

	protected abstract String getHelpId();

	protected abstract void setDescription();

	protected abstract void setPreferenceStore();

	protected abstract IPreferenceConfigurationBlock createConfigurationBlock(
			OverlayPreferenceStore overlayPreferenceStore);
}
