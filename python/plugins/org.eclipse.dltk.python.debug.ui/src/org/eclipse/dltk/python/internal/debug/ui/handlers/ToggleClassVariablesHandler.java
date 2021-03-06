package org.eclipse.dltk.python.internal.debug.ui.handlers;

import org.eclipse.dltk.debug.ui.handlers.AbstractToggleClassVariableHandler;
import org.eclipse.dltk.python.internal.debug.PythonDebugConstants;
import org.eclipse.dltk.python.internal.debug.PythonDebugPlugin;
import org.eclipse.dltk.ui.PreferencesAdapter;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Toggles the display of Python class variables in the debug 'Variables' view
 */
public class ToggleClassVariablesHandler extends
		AbstractToggleClassVariableHandler {

	/*
	 * @see org.eclipse.dltk.debug.ui.handlers.AbstractToggleVariableHandler#getModelId()
	 */
	protected String getModelId() {
		return PythonDebugConstants.DEBUG_MODEL_ID;
	}

	/*
	 * @see org.eclipse.dltk.debug.ui.handlers.AbstractToggleVariableHandler#getPreferenceStore()
	 */
	protected IPreferenceStore getPreferenceStore() {
		return new PreferencesAdapter(PythonDebugPlugin.getDefault()
				.getPluginPreferences());
	}
}
