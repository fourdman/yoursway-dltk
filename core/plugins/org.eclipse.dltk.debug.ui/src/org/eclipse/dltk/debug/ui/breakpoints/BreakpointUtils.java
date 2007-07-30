/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.debug.ui.breakpoints;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.debug.core.DLTKDebugPlugin;
import org.eclipse.dltk.debug.core.ScriptDebugManager;
import org.eclipse.dltk.debug.core.model.IScriptBreakpoint;
import org.eclipse.dltk.internal.debug.core.model.ScriptDebugModel;
import org.eclipse.dltk.ui.DLTKUILanguageManager;
import org.eclipse.dltk.ui.IDLTKUILanguageToolkit;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.texteditor.ITextEditor;

public class BreakpointUtils {
	public static String getNatureId(IScriptBreakpoint breakpoint) {
		ScriptDebugManager manager = ScriptDebugManager.getInstance();
		return manager.getNatureByDebugModel(breakpoint.getModelIdentifier());
	}

	public static IDLTKLanguageToolkit getLanguageToolkit(
			IScriptBreakpoint breakpoint) {
		try {
			return DLTKLanguageManager
					.getLanguageToolkit(getNatureId(breakpoint));
		} catch (CoreException e) {
			return null;
		}
	}

	public static IDLTKUILanguageToolkit getUILanguageToolkit(
			IScriptBreakpoint breakpoint) {
		return DLTKUILanguageManager
				.getLanguageToolkit(getNatureId(breakpoint));
	}

	public static void addLineBreakpoint(ITextEditor textEditor, int lineNumber)
			throws CoreException {
		IDocument document = textEditor.getDocumentProvider().getDocument(
				textEditor.getEditorInput());

		IResource resource = (IResource) textEditor.getEditorInput()
				.getAdapter(IResource.class);
		if (resource != null) {
			try {
				IRegion line = document.getLineInformation(lineNumber - 1);
				int start = line.getOffset();
				int end = start + line.getLength() - 1;

				/* ILineBreakpoint b = */ScriptDebugModel
						.createLineBreakpoint(resource, lineNumber, start, end,
								true, null);
			} catch (BadLocationException e) {
				DLTKDebugPlugin.log(e);
			}
		}
	}

	public static void addMethodEntryBreakpoint(ITextEditor textEditor,
			int lineNumber, String methodName) throws CoreException {
		IDocument document = textEditor.getDocumentProvider().getDocument(
				textEditor.getEditorInput());

		IResource resource = (IResource) textEditor.getEditorInput()
				.getAdapter(IResource.class);
		if (resource != null) {
			try {
				IRegion line = document.getLineInformation(lineNumber - 1);
				int start = line.getOffset();
				int end = start + line.getLength() - 1;
				/* ILineBreakpoint b = */ScriptDebugModel
						.createMethodEntryBreakpoint(resource, lineNumber,
								start, end, true, null, methodName);
			} catch (BadLocationException e) {
				DebugPlugin.log(e);
			}
		}
	}

	public static void addWatchPoint(ITextEditor textEditor, int lineNumber,
			String fieldName) throws CoreException {
		IDocument document = textEditor.getDocumentProvider().getDocument(
				textEditor.getEditorInput());

		IResource resource = (IResource) textEditor.getEditorInput()
				.getAdapter(IResource.class);
		if (resource != null) {
			try {
				IRegion line = document.getLineInformation(lineNumber - 1);
				int start = line.getOffset();
				int end = start + line.getLength() - 1;
				/* ILineBreakpoint b = */ScriptDebugModel.createWatchPoint(
						resource, lineNumber, start, end, fieldName);
			} catch (BadLocationException e) {
				DebugPlugin.log(e);
			}
		}
	}
}
