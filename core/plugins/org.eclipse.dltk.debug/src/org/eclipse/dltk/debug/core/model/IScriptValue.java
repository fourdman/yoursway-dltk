/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.debug.core.model;

import org.eclipse.debug.core.model.IValue;
import org.eclipse.dltk.debug.core.eval.IScriptEvaluationCommand;

public interface IScriptValue extends IValue {
	String getInstanceId();
	IScriptType getType();
	String getEvalName();
	String getRawValue();
	
	IScriptEvaluationCommand createEvaluationCommand(String messageTemplate,
			IScriptThread thread);
}
