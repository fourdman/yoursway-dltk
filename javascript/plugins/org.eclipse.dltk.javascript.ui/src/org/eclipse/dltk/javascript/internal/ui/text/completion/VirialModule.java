/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.javascript.internal.ui.text.completion;

import java.util.HashMap;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.codeassist.ICompletionEngine;
import org.eclipse.dltk.compiler.env.ISourceModule;
import org.eclipse.dltk.core.CompletionRequestor;
import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.core.IBuffer;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.IField;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IPackageDeclaration;
import org.eclipse.dltk.core.IProblemRequestor;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.WorkingCopyOwner;
import org.eclipse.dltk.internal.core.ModelElement;
import org.eclipse.dltk.internal.core.ProjectFragment;
import org.eclipse.dltk.internal.core.ScriptFolder;
import org.eclipse.dltk.internal.core.ScriptProject;
import org.eclipse.dltk.internal.core.SearchableEnvironment;
import org.eclipse.dltk.internal.core.util.MementoTokenizer;
import org.eclipse.dltk.javascript.core.JavaScriptNature;
import org.eclipse.dltk.utils.CorePrinter;

public class VirialModule extends ModelElement implements ISourceModule,
		org.eclipse.dltk.core.ISourceModule {

	private IResource    resource;
	private String contents;

	public  VirialModule(IScriptProject parent,IResource res,String contents) throws IllegalArgumentException {
		super(new ScriptFolder(new ProjectFragment(parent.getResource(),(ScriptProject)parent){},new Path("")){			
		});
		this.resource=res;
		this.contents=contents;
	}

	protected void closing(Object info) throws ModelException {
		// TODO Auto-generated method stub

	}

	protected Object createElementInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void generateInfos(Object info, HashMap newElements,
			IProgressMonitor pm) throws ModelException {
		// TODO Auto-generated method stub

	}

	public IModelElement getHandleFromMemento(String token,
			MementoTokenizer memento, WorkingCopyOwner owner) {
		// TODO Auto-generated method stub
		return null;
	}

	protected char getHandleMementoDelimiter() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void printNode(CorePrinter output) {
		// TODO Auto-generated method stub

	}

	public IModelElement getModelElement() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPath getScriptFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSourceContents() {
		return contents;
	}

	public char[] getFileName() {
		return new char[0];
	}

	public IResource getCorrespondingResource() throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getElementType() {
		// TODO Auto-generated method stub
		return IModelElement.SOURCE_MODULE;
	}

	public IPath getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public IResource getResource() {
		return resource;
	}

	public IResource getUnderlyingResource() throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isStructureKnown() throws ModelException {
		// TODO Auto-generated method stub
		return false;
	}

	public void becomeWorkingCopy(IProblemRequestor problemRequestor,
			IProgressMonitor monitor) throws ModelException {
		// TODO Auto-generated method stub

	}

	public void commitWorkingCopy(boolean force, IProgressMonitor monitor)
			throws ModelException {
		// TODO Auto-generated method stub

	}

	public void discardWorkingCopy() throws ModelException {
		// TODO Auto-generated method stub

	}

	public IType[] getAllTypes() throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public IModelElement getElementAt(int position) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public IField getField(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public IField[] getFields() throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMethod getMethod(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public WorkingCopyOwner getOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPackageDeclaration getPackageDeclaration(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public IPackageDeclaration[] getPackageDeclarations() throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public org.eclipse.dltk.core.ISourceModule getPrimary() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSource() throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public IType getType(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public IType[] getTypes() throws ModelException {
		return new IType[0];
	}

	public org.eclipse.dltk.core.ISourceModule getWorkingCopy(
			IProgressMonitor monitor) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public org.eclipse.dltk.core.ISourceModule getWorkingCopy(
			WorkingCopyOwner owner, IProblemRequestor problemRequestor,
			IProgressMonitor monitor) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isPrimary() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isWorkingCopy() {
		// TODO Auto-generated method stub
		return false;
	}

	public void reconcile(boolean forceProblemDetection,
			WorkingCopyOwner owner, IProgressMonitor monitor)
			throws ModelException {
		// TODO Auto-generated method stub

	}

	public IBuffer getBuffer() throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasUnsavedChanges() throws ModelException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isConsistent() throws ModelException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	public void makeConsistent(IProgressMonitor progress) throws ModelException {
		// TODO Auto-generated method stub

	}

	public void open(IProgressMonitor progress) throws ModelException {
		// TODO Auto-generated method stub

	}

	public void save(IProgressMonitor progress, boolean force)
			throws ModelException {
		// TODO Auto-generated method stub

	}

	public ISourceRange getSourceRange() throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public void copy(IModelElement container, IModelElement sibling,
			String rename, boolean replace, IProgressMonitor monitor)
			throws ModelException {
		// TODO Auto-generated method stub

	}

	public void delete(boolean force, IProgressMonitor monitor)
			throws ModelException {
		// TODO Auto-generated method stub

	}

	public void move(IModelElement container, IModelElement sibling,
			String rename, boolean replace, IProgressMonitor monitor)
			throws ModelException {
		// TODO Auto-generated method stub

	}

	public void rename(String name, boolean replace, IProgressMonitor monitor)
			throws ModelException {
		// TODO Auto-generated method stub

	}

	public void codeComplete(int offset, CompletionRequestor requestor)
			throws ModelException {

		ScriptProject project = (ScriptProject) getScriptProject();
		// TODO. Add searchable environment support.
		SearchableEnvironment environment = (SearchableEnvironment)project.newSearchableNameEnvironment(new org.eclipse.dltk.core.ISourceModule[] { this });
		IDLTKLanguageToolkit toolkit = null;
		toolkit = DLTKLanguageManager.getLanguageToolkit(this);
		if (toolkit == null) {
			toolkit = DLTKLanguageManager.findToolkit(this.getResource());
			if (toolkit == null) {
				return;
			}
		}
		// code complete
		ICompletionEngine engine = null;
		engine = DLTKLanguageManager.getCompletionEngine(JavaScriptNature.NATURE_ID);
		
		if (engine != null) {
//			engine.setEnvironment(environment);
			engine.setRequestor(requestor);
			engine.setOptions(project.getOptions(true));
			engine.setProject(project);
			engine.complete(this, offset, 0);
		}
	}

	public void codeComplete(int offset, CompletionRequestor requestor,
			WorkingCopyOwner owner) throws ModelException {
		// TODO Auto-generated method stub

	}

	public IModelElement[] codeSelect(int offset, int length)
			throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public IModelElement[] codeSelect(int offset, int length,
			WorkingCopyOwner owner) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public char[] getSourceAsCharArray() throws ModelException {
		return null;	
	}

	public boolean isBuiltin() {
		// TODO Auto-generated method stub
		return false;
	}

}
