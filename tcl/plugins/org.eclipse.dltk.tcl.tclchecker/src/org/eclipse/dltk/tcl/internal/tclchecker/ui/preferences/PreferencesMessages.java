/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import org.eclipse.osgi.util.NLS;

public class PreferencesMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences.messages"; //$NON-NLS-1$

	public static String TclChecker_path;
	public static String TclChecker_pcxPath;

	public static String TclChecker_path_configureTitle;

	public static String TclChecker_path_configureMessage;

	public static String TclChecker_path_isempty;

	public static String TclChecker_path_isinvalid;

	public static String TclChecker_path_notexists;

	public static String TclChecker_path_notlookslike;

	public static String TclChecker_suppressProblems;

	public static String TclChecker_browse;

	public static String TclChecker_selectAll;

	public static String TclChecker_clearSelection;

	public static String TclChecker_invertSelection;

	public static String TclChecker_mode;

	public static String TclChecker_mode_errors;

	public static String TclChecker_mode_errorsAndUsageWarnings;

	public static String TclChecker_mode_all;

	public static String TclChecker_work_name;
	
	public static String TclChecker_error;
	
	public static String TclChecker_warning;

	static {
		NLS.initializeMessages(BUNDLE_NAME, PreferencesMessages.class);
	}
}
