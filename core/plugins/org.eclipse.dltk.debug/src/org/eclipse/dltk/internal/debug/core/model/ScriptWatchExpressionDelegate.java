/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.internal.debug.core.model;

import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.core.model.IWatchExpressionDelegate;
import org.eclipse.debug.core.model.IWatchExpressionListener;
import org.eclipse.dltk.debug.core.eval.IScriptEvaluationEngine;
import org.eclipse.dltk.debug.core.eval.IScriptEvaluationListener;
import org.eclipse.dltk.debug.core.eval.IScriptEvaluationResult;
import org.eclipse.dltk.debug.core.model.IScriptStackFrame;
import org.eclipse.dltk.debug.core.model.IScriptThread;

public class ScriptWatchExpressionDelegate implements IWatchExpressionDelegate {
	private static class ListenerAdpater implements IScriptEvaluationListener {
		private final IWatchExpressionListener listener;

		public ListenerAdpater(IWatchExpressionListener listener) {
			this.listener = listener;
		}

		public void evaluationComplete(IScriptEvaluationResult result) {
			listener.watchEvaluationFinished(new ScriptWatchExpressionResult(
					result));
		}
	}

	protected static IScriptThread getScriptThread(Object context) {
		if (context instanceof IScriptThread) {
			return (IScriptThread) context;
		} else if (context instanceof IScriptStackFrame) {
			return (IScriptThread) ((IScriptStackFrame) context).getThread();
		}

		return null;
	}

	public void evaluateExpression(String expression, IDebugElement context,
			IWatchExpressionListener listener) {

		IScriptThread thread = getScriptThread(context);
		if (thread != null) {
			IScriptEvaluationEngine engine = thread.getEvaluationEngine();
			if (engine != null) {
				engine.asyncEvaluate(expression, null, new ListenerAdpater(
						listener));
			}
		}
	}

	protected String prepareExpression(String expression) {
		return expression;
	}
}
