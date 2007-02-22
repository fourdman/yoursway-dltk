/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.dltk.internal.debug.ui.interpreters;

import org.eclipse.dltk.debug.ui.messages.DLTKLaunchMessages;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;

import com.ibm.icu.text.MessageFormat;

/**
 * Interpreter Descriptor used for the Interpreter container wizard page.
 */
public class BuildInterpreterDescriptor extends InterpreterDescriptor {
	
	private String fNature;
	
	public BuildInterpreterDescriptor(String nature) {
		fNature = nature;
	}
		
	public String getDescription() {
		String name = DLTKLaunchMessages.InterpreterTab_7;
		IInterpreterInstall Interpreter = ScriptRuntime.getDefaultInterpreterInstall(fNature);
		if (Interpreter != null) {
			name = Interpreter.getName();
		}
		return MessageFormat.format(DLTKLaunchMessages.InterpreterTab_8, new String[] {
			name
		});
	}
}
