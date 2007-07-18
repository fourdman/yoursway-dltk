/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.internal.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.dltk.core.PriorityClassDLTKExtensionManager;

public class NewInstanceClassBasedDLTKExtensionManager extends
		PriorityClassDLTKExtensionManager {

	public NewInstanceClassBasedDLTKExtensionManager(String extensionPoint) {
		super(extensionPoint);
	}

	public Object getInitObject(ElementInfo ext) {
		try {
			if (ext != null) {
				IConfigurationElement cfg = (IConfigurationElement) ext.config;
				Object object = createObject(cfg);
				return object;
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
