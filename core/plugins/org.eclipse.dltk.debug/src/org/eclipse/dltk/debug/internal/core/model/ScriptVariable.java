/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.debug.internal.core.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.dltk.dbgp.IDbgpProperty;
import org.eclipse.dltk.dbgp.commands.IDbgpCoreCommands;
import org.eclipse.dltk.dbgp.exceptions.DbgpException;
import org.eclipse.dltk.debug.core.model.IScriptVariable;

public class ScriptVariable extends ScriptDebugElement implements IScriptVariable {

	private int stackLevel;

	private IDbgpProperty property;

	private String newValue;

	private IDbgpCoreCommands core;

	private IDebugTarget target;
	
	protected ScriptVariable(IDebugTarget target, int stackLevel, IDbgpProperty property, IDbgpCoreCommands core) {
		this.target = target;

		this.stackLevel = stackLevel;
		this.property = property;

		this.newValue = null;

		this.core = core;
	}

	public IDebugTarget getDebugTarget() {
		 return target;
	}

	public String getName() throws DebugException {
		return property.getName();
	}

	public String getReferenceTypeName() throws DebugException {
		return property.getType();
	}

	public IValue getValue() throws DebugException {
		return new ScriptValue(this);
	}

	public boolean hasValueChanged() throws DebugException {
		return false;
	}

	public void setValue(String expression) throws DebugException {
		newValue = expression;
		try {
			// TODO: Set current value of IValue !!!
			core.setProperty(property.getFullName(), stackLevel, expression);
			property.setValue(expression);
			DebugEventHelper.fireChangeEvent(this);
		} catch (DbgpException e) {

			e.printStackTrace();
		}
	}

	public void setValue(IValue value) throws DebugException {
		setValue(value.getValueString());
	}

	public boolean supportsValueModification() {
		return !hasChildren() && !property.isConstant();
	}

	public boolean verifyValue(String expression) throws DebugException {
		return expression != null;

	}

	public boolean verifyValue(IValue value) throws DebugException {
		return verifyValue(value.getValueString());
	}

	public boolean isConstant() throws DebugException {
		return property.isConstant();
	}

	public boolean hasChildren() {
		return property.hasChildren();
	}
	
	protected ScriptVariable[] readVariablesByKey(IDbgpCoreCommands core) throws DbgpException {
		IDbgpProperty p = core.getPropertyByKey(property.getName(), property.getKey());
		IDbgpProperty[] props = p.getAvailableChildren();
		
		List variables  = new ArrayList();
					
		for (int i = 0; i < props.length; ++i) {
			variables.add(new ScriptVariable(getDebugTarget(), stackLevel, props[i], core));
		}

		return (ScriptVariable[]) variables.toArray(new ScriptVariable[variables.size()]);
	}

	public synchronized IScriptVariable[] getChildren() {
		if (property.getKey() != null) {
			try {
				return readVariablesByKey(core);
			} catch (DbgpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new IScriptVariable[0];
			} 
		}
		
		IDbgpProperty[] properties = property.getAvailableChildren();

		if (properties.length != property.getChildrenCount()) {
			try {
				property = core.getProperty(property.getFullName(), stackLevel);
				return getChildren();
			} catch (DbgpException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		IScriptVariable[] variables = new IScriptVariable[properties.length];
		for (int i = 0; i < properties.length; ++i) {
			variables[i] = new ScriptVariable(getDebugTarget(), stackLevel, (IDbgpProperty) properties[i], core);
		}
		return variables;
	}

	public String getTypeString() {
		return property.getType();
	}

	public String getValueString() {
		if (newValue != null) {
			return newValue;
		} else {
			return property.getValue();
		}
	}

	public String toString() {
		return property.getName();
	}

	public String getFullName() {
		return property.getFullName();
	}
}
