/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.debug.internal.core.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.dltk.dbgp.IDbgpProperty;
import org.eclipse.dltk.dbgp.IDbgpStackLevel;
import org.eclipse.dltk.dbgp.commands.IDbgpCoreCommands;
import org.eclipse.dltk.dbgp.exceptions.DbgpException;
import org.eclipse.dltk.debug.core.model.IScriptStackFrame;
import org.eclipse.dltk.debug.core.model.IScriptThread;
import org.eclipse.dltk.debug.core.model.IScriptVariable;

public class ScriptStackFrame extends ScriptDebugElement implements IScriptStackFrame {

	private int stackDepth;

	private IScriptThread thread;

	private IDbgpStackLevel stackLevel;

	private IScriptVariable[] variables;

	private String name;

	protected ScriptVariable[] readVariables(int stackDepth, IDbgpCoreCommands core) throws DbgpException {
		List variables = new ArrayList();
		
		List properties = core.getContextProperties(stackDepth);
		Iterator it = properties.iterator();
		while (it.hasNext()) {
			IDbgpProperty property = (IDbgpProperty) it.next();
			variables.add(new ScriptVariable(getDebugTarget(), stackDepth, property, core));
		}

		return (ScriptVariable[]) variables.toArray(new ScriptVariable[variables.size()]);

	}

	public ScriptStackFrame(IScriptThread thread, IDbgpStackLevel stackLevel, IDbgpCoreCommands coreCommands, int stackDepth) throws DbgpException {

		if (thread == null || stackLevel == null || coreCommands == null) {
			throw new IllegalArgumentException("thread cannot be null");
		}

		this.thread = thread;
		this.stackLevel = stackLevel;
		this.name = stackLevel.getWhere();
		this.variables = readVariables(stackLevel.getLevel(), coreCommands);
		this.stackDepth = stackDepth;
	}

	public URI getFileName() {
		return stackLevel.getFileURI();
	}

	public int getCharStart() throws DebugException {
		return -1;
	}

	public int getCharEnd() throws DebugException {
		return -1;
	}

	public int getLineNumber() throws DebugException {
		return stackLevel.getLineNumber();
	}

	public String getName() throws DebugException {
		if (name == null || name.length() == 0)
			return "Stack Frame, level = " + stackLevel.getLevel();
		else
			return name + "(" + stackLevel.getFileURI() + ")";
	}

	public IRegisterGroup[] getRegisterGroups() throws DebugException {
		return null;
	}

	public IThread getThread() {
		return thread;
	}

	public boolean hasVariables() throws DebugException {
		if (variables == null) {
			return false;
		}

		return variables.length > 0;
	}

	public IVariable[] getVariables() throws DebugException {
		return variables;
	}

	public boolean hasRegisterGroups() throws DebugException {
		return false;
	}

	// IStep
	public boolean canStepInto() {
		return thread.canStepInto();
	}

	public boolean canStepOver() {
		return thread.canStepOver();
	}

	public boolean canStepReturn() {
		return thread.canStepReturn();
	}

	public boolean isStepping() {
		return thread.isStepping();
	}

	public void stepInto() throws DebugException {
		thread.stepInto();
	}

	public void stepOver() throws DebugException {
		thread.stepOver();
	}

	public void stepReturn() throws DebugException {
		thread.stepReturn();
	}

	// ISuspenResume
	public boolean canResume() {
		return thread.canResume();
	}

	public boolean canSuspend() {
		return thread.canSuspend();
	}

	public boolean isSuspended() {
		return thread.isTerminated();
	}

	public void resume() throws DebugException {
		thread.resume();
	}

	public void suspend() throws DebugException {
		thread.suspend();
	}

	// ITerminate
	public boolean canTerminate() {
		return thread.canTerminate();
	}

	public boolean isTerminated() {
		return thread.isTerminated();
	}

	public void terminate() throws DebugException {
		thread.terminate();
	}

	public boolean equals(Object obj) {
		if (obj instanceof ScriptStackFrame) {
			ScriptStackFrame sf = (ScriptStackFrame) obj;

			if (stackLevel.getLineNumber() != sf.stackLevel.getLineNumber()) {
				return false;
			}

			if (variables.length != sf.variables.length) {
				return false;
			}

			for (int i = 0; i < variables.length; ++i) {
				if (!variables[i].equals(sf.variables[i])) {
					return false;
				}
			}

			return true;
		}

		return false;
	}

	public int hashCode() {
		return stackLevel.hashCode();
	}

	public String toString() {
		return "Stack frame (level: " + (stackDepth - stackLevel.getLevel()) + ")";
	}

	public IScriptVariable findVariable(String varName) throws DebugException {
		for (int a = 0; a < variables.length; a++) {
			if (variables[a].getName().equals(varName))
				return variables[a];
		}
		return null;
	}

	// IDebugElement
	public IDebugTarget getDebugTarget() {
		return thread.getDebugTarget();
	}
}
