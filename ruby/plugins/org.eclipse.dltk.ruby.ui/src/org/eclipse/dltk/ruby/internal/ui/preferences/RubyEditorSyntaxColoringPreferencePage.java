package org.eclipse.dltk.ruby.internal.ui.preferences;

import org.eclipse.dltk.ruby.internal.ui.RubyUI;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPreferencePage;
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.preferences.PreferencesMessages;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class RubyEditorSyntaxColoringPreferencePage extends
		AbstractConfigurationBlockPreferencePage {

	/*
	 * @see org.eclipse.ui.internal.editors.text.AbstractConfigureationBlockPreferencePage#getHelpId()
	 */
	protected String getHelpId() {
		return "";
	}

	/*
	 * @see org.eclipse.ui.internal.editors.text.AbstractConfigurationBlockPreferencePage#setDescription()
	 */
	protected void setDescription() {
		String description = PreferencesMessages.DLTKEditorPreferencePage_colors;
		setDescription(description);
	}

	protected Label createDescriptionLabel(Composite parent) {
		return null;
	}

	/*
	 * @see org.org.eclipse.ui.internal.editors.text.AbstractConfigurationBlockPreferencePage#setPreferenceStore()
	 */
	protected void setPreferenceStore() {
		setPreferenceStore(RubyUI.getDefault().getPreferenceStore());
	}

	/*
	 * @see org.eclipse.ui.internal.editors.text.AbstractConfigureationBlockPreferencePage#createConfigurationBlock(org.eclipse.ui.internal.editors.text.OverlayPreferenceStore)
	 */
	protected IPreferenceConfigurationBlock createConfigurationBlock(
			OverlayPreferenceStore overlayPreferenceStore) {
		return new RubyEditorColoringConfigurationBlock(overlayPreferenceStore);
	}
}
