/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.debug.core.model;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;

public interface IScriptVariable extends IVariable {
	boolean isConstant();

	String getFullName();

	String getTypeString();

	String getValueString();

	String getId();

	boolean hasChildren();

	boolean shouldHasChildren();

	IScriptVariable[] getChildren() throws DebugException;
}
