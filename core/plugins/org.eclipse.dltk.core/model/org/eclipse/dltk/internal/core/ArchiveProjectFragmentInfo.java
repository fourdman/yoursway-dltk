/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.internal.core;

import org.eclipse.core.resources.IResource;
import org.eclipse.dltk.core.IScriptProject;


class ArchiveProjectFragmentInfo extends ProjectFragmentInfo {
	
	public Object[] getForeignResources(IScriptProject scriptProject, IResource resource, ProjectFragment fragment) {
		return getForeignResources();
	}
	public Object[] getForeignResources() {
		foreignResources = NO_NON_SCRIPT_RESOURCES;
		return foreignResources;
	}
}
