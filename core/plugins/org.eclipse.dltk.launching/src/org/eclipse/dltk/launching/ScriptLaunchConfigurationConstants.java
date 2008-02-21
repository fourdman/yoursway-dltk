/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.launching;


/**
 * Constants for launch configurations
 * 
 * @author fourdman
 */
public class ScriptLaunchConfigurationConstants {
	protected ScriptLaunchConfigurationConstants() {

	}

	// Attributes
	public static final String ATTR_USE_DLTK_OUTPUT = "use_dtltk"; //$NON-NLS-1$

	public static final String ATTR_DLTK_CONSOLE_ID = "console_id"; //$NON-NLS-1$

	public static final String ATTR_DLTK_DBGP_REMOTE = "dbgp_remote"; //$NON-NLS-1$
	public static final String ATTR_DLTK_DBGP_SESSION_ID = "dbgp_session_id"; //$NON-NLS-1$
	public static final String ATTR_DLTK_DBGP_PORT = "dbpg_port"; //$NON-NLS-1$
	public static final String ATTR_DLTK_DBGP_WAITING_TIMEOUT = "dbpg_waiting_timeout"; //$NON-NLS-1$

	/**
	 * Project associated with a launch configuration.
	 */
	public static final String ATTR_PROJECT_NAME = "project"; //$NON-NLS-1$

	/**
	 * Script to launch.
	 */
	public static final String ATTR_MAIN_SCRIPT_NAME = "mainScript"; //$NON-NLS-1$

	/**
	 * Working directory for executed script
	 */
	public static final String ATTR_WORKING_DIRECTORY = "workingDir"; //$NON-NLS-1$

	/**
	 * Arguments for script
	 */
	public static final String ATTR_SCRIPT_ARGUMENTS = "scriptArguments"; //$NON-NLS-1$

	/**
	 * Launch configuration attribute key. The value is a Map of attributes
	 * specific to a particular interpreter install type, used when launching a
	 * local script application. The map is passed to a
	 * <code>InterpreterRunner</code> via a
	 * <code>InterpreterRunnerConfiguration</code> when launching a
	 * interpreter. The attributes in the map are implementation dependent and
	 * are limited to String keys and values.
	 */
	public static final String ATTR_INTERPRETER_INSTALL_TYPE_SPECIFIC_ATTRS_MAP = "interpreterTypeSpecificAttrs"; //$NON-NLS-1$

	/**
	 * Launch configuration attribute key. The value is a boolean specifying
	 * whether a default buildpath should be used when launching a local sctipt
	 * application. When <code>false</code>, a buildpath must be specified
	 * via the <code>ATTR_BUILDPATH</code> attribute. When <code>true</code>
	 * or unspecified, a buildpath is computed by the buildpath provider
	 * associated with a launch configuration.
	 */
	public static final String ATTR_DEFAULT_BUILDPATH = "defaultBuildpath"; //$NON-NLS-1$

	public static final String ATTR_DEFAULT_SOURCEPATH = "defaultSourcePath";
	
	public static final String ATTR_SOURCEPATH = "sourcePath";
	
	/**
	 * Launch configuration attribute key. The attribute value is an ordered
	 * list of strings which are mementos for runtime build path entries. When
	 * unspecified, a default buildpath is generated by the buildpath provider
	 * associated with a launch configuration (via the
	 * <code>ATTR_BUILDPATH_PROVIDER</code> attribute).
	 */
	public static final String ATTR_BUILDPATH = "buildpath"; //$NON-NLS-1$

	/**
	 * Launch configuration attribute key. The value is a path identifying the
	 * interpreter used when launching a local interpreter. The path is a
	 * buildpath container corresponding to the
	 * <code>ScriptRuntime.INTERPRETER_CONTAINER</code> buildpath container.
	 * <p>
	 * When unspecified the default interpreter for a launch configuration is
	 * used (which is the interpreter associated with the project being
	 * launched, or the workspace default InterpreterEnvironment when no project
	 * is associated with a configuration). The default interpreter buildpath
	 * container refers explicitly to the workspace default interpreter.
	 * </p>
	 */
	public static final String ATTR_CONTAINER_PATH = ScriptRuntime.INTERPRETER_CONTAINER;

	/**
	 * Launch configuration attribute key. The value is an identifier for a
	 * buildpath provider extension used to compute the buildpath for a launch
	 * configuration. When unspecified, the default buildpath provider is used -
	 * <code>StandardBuildpathProvider</code>.
	 */
	public static final String ATTR_BUILDPATH_PROVIDER = "buildpathProvider"; //$NON-NLS-1$
	
	public static final String ATTR_SOURCEPATH_PROVIDER = "sourcepathProvider";

	public static final String ATTR_DEBUG_CONNECTOR = "debugConnector"; //$NON-NLS-1$

	public static final String ATTR_SCRIPT_NATURE = "nature"; //$NON-NLS-1$

	public static final String ATTR_INTERPRETER_ARGUMENTS = "interpreterArguments"; //$NON-NLS-1$

	/**
	 * Launch configuration attribute key. This value is an identifier for the
	 * working directory of a remote debugging session, and is used in an
	 * attempt to map the remote source file to a project in the workspace.
	 */
	public static final String ATTR_DLTK_DBGP_REMOTE_WORKING_DIR = "remoteWorkingDir";
	
	public static final String ID_SCRIPT_PROCESS_TYPE = "script"; //$NON-NLS-1$

	public static final String ENABLE_BREAK_ON_FIRST_LINE = "enableBreakOnFirstLine";
	public static final String ENABLE_DBGP_LOGGING = "enableDbgpLogging";
	
	// Errors
	public static final int ERR_INTERNAL_ERROR = 100;

	public static final int ERR_PROJECT_CLOSED = 101;

	public static final int ERR_NOT_A_SCRIPT_PROJECT = 102;

	// public static final int ERR_UNSPECIFIED_INTERPRETER_INSTALL = 104;

	public static final int ERR_INTERPRETER_INSTALL_DOES_NOT_EXIST = 105;

	public static final int ERR_UNSPECIFIED_MAIN_SCRIPT = 106;

	public static final int ERR_INTERPRETER_RUNNER_DOES_NOT_EXIST = 107;

	public static final int ERR_WORKING_DIRECTORY_DOES_NOT_EXIST = 108;

	public static final int ERR_UNSPECIFIED_PROJECT = 109;

	public static final int ERR_NO_DEFAULT_INTERPRETER_INSTALL = 167;

	// Debugging engine errors
	public static final int ERR_DEBUGGING_ENGINE_NOT_CONFIGURED = 500;

	public static final int ERR_NO_DEFAULT_DEBUGGING_ENGINE = 510;

}
