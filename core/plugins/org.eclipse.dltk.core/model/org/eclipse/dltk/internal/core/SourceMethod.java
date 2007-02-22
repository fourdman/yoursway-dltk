package org.eclipse.dltk.internal.core;

import org.eclipse.core.runtime.Assert;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.Signature;
import org.eclipse.dltk.utils.CorePrinter;


public class SourceMethod extends NamedMember implements IMethod {

	public SourceMethod(ModelElement parent, String name) {
		super(parent, name);
	}

	public int getElementType() {
		return METHOD;
	}
	
	public String[] getParameters() throws ModelException {
		SourceMethodElementInfo info = (SourceMethodElementInfo)this.getElementInfo();		
		if( info != null ) {
			return info.getArgumentNames();
		}
		return null;
	}
	public String[] getParameterInitializers() throws ModelException {
		SourceMethodElementInfo info = (SourceMethodElementInfo)this.getElementInfo();		
		if( info != null ) {
			return info.getArgumentInitializers();
		}
		return null;
	}
	public void printNode(CorePrinter output) {		
		output.formatPrint("DLTK Source Method:" + getElementName());
		output.indent();
		try {
			IModelElement modelElements[] = this.getChildren();
			for( int i = 0; i < modelElements.length; ++i ) {
				IModelElement element = modelElements[i];
				if( element instanceof ModelElement ) {
					((ModelElement)element).printNode(output);
				}
				else {
					output.print("Unknown element:" + element );
				}
			}
		}
		catch(ModelException ex ) {
			output.formatPrint(ex.getLocalizedMessage());
		}
		output.dedent();
	}

	/**
	 * @see IMethod
	 */
	public boolean isConstructor() throws ModelException {
		if (!this.getElementName().equals(this.parent.getElementName())) {
			// faster than reaching the info
			return false;
		}
		SourceMethodElementInfo info = (SourceMethodElementInfo) getElementInfo();
		return info.isConstructor();
	}

	protected char getHandleMementoDelimiter() {
		return JEM_METHOD;
	}	
	/**
	 * @see IMethod
	 */
	public String getSignature() throws ModelException {
		//SourceMethodElementInfo info = (SourceMethodElementInfo) getElementInfo();
		return Signature.createMethodSignature(this.getParameters());
	}
	public String getFullyQualifiedName(String enclosingTypeSeparator) {
		try {
			return getFullyQualifiedName(enclosingTypeSeparator, false/*
																		 * don't
																		 * show
																		 * parameters
																		 */);
		} catch (ModelException e) {
			// exception thrown only when showing parameters
			return null;
		}
	}

	public String getFullyQualifiedName() {
		return getFullyQualifiedName("$");
	}	
	public IScriptFolder getScriptFolder() {
		IModelElement parentElement = this.parent;
		while (parentElement != null) {
			if (parentElement.getElementType() == IModelElement.SCRIPT_FOLDER) {
				return (IScriptFolder) parentElement;
			} else {
				parentElement = parentElement.getParent();
			}
		}
		Assert.isTrue(false); // should not happen
		return null;
	}	
}
