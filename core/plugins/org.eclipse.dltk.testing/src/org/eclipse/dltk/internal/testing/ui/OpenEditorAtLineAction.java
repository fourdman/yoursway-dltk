/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Sebastian Davids: sdavids@gmx.de bug 37333 Failure Trace cannot 
 * 			navigate to non-public class in CU throwing Exception
 *******************************************************************************/
package org.eclipse.dltk.internal.testing.ui;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptProject;

/**
 * Open a test in the Java editor and reveal a given line
 */
public class OpenEditorAtLineAction extends OpenEditorAction {

	private int fLineNumber;
	
	public OpenEditorAtLineAction(TestRunnerViewPart testRunner, String cuName, String className, int line) {
		super(testRunner, className);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IDLTKTestingHelpContextIds.OPENEDITORATLINE_ACTION);
		fLineNumber= line;
	}
		
	protected void reveal(ITextEditor textEditor) {
		if (fLineNumber >= 0) {
			try {
				IDocument document= textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
				textEditor.selectAndReveal(document.getLineOffset(fLineNumber-1), document.getLineLength(fLineNumber-1));
			} catch (BadLocationException x) {
				// marker refers to invalid text position -> do nothing
			}
		}
	}
	
	protected IModelElement findElement(IScriptProject project, String className) throws CoreException {
		return findType(project, className);
	}

}
