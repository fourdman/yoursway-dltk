/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.compiler.problem;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;

public interface IProblemReporter {
	IMarker reportProblem(IProblem problem) throws CoreException;
	// Clear all existing markes from file
	void clearMarkers();
	boolean isMarkersCleaned();
}
