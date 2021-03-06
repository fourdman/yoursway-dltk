/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.core.search;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IParent;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.search.IDLTKSearchScope;
import org.eclipse.dltk.core.search.SearchPattern;
import org.eclipse.dltk.core.search.SearchRequestor;
import org.eclipse.dltk.core.search.matching.MatchLocator;
import org.eclipse.dltk.internal.core.BuiltinSourceModule;
import org.eclipse.dltk.internal.core.ExternalSourceModule;
import org.eclipse.dltk.internal.core.Openable;
import org.eclipse.dltk.internal.core.SourceMethod;
import org.eclipse.dltk.internal.core.SourceModule;

public class TclMatchLocator extends MatchLocator {

	public TclMatchLocator(SearchPattern pattern, SearchRequestor requestor,
			IDLTKSearchScope scope, IProgressMonitor progressMonitor) {
		super(pattern, requestor, scope, progressMonitor);
	}

	/*
	 * Create method handle. Store occurences for create handle to retrieve
	 * possible duplicate ones.
	 */
	protected IModelElement createMethodHandle(ISourceModule module,
			String methodName) {
		IMethod methodHandle = null;
		if (methodName.indexOf("::") != -1) {
			int pos = methodName.lastIndexOf("::");
			String cName = methodName.substring(0, pos);
			String name = methodName.substring(pos + 2);

			if (!cName.startsWith("$")) {
				cName = "$" + cName;
			}

			cName = cName.replaceAll("::", "\\$");
			if (!cName.equals("$")) {
				IType type = null;
				try {
					type = findTypeFrom(module.getChildren(), "", cName, '$');
				} catch (ModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (type != null) {
					methodHandle = type.getMethod(name);
				}
			} else {
				methodHandle = module.getMethod(methodName);
			}
		} else {
			methodHandle = module.getMethod(methodName);
		}

		// TODO: make this more correctly without using SourceMethod from
		// internal package!!!
		if (methodHandle instanceof SourceMethod) {
			while (this.methodHandles.contains(methodHandle)) {
				((SourceMethod) methodHandle).occurrenceCount++;
			}
		}
		this.methodHandles.add(methodHandle);
		return methodHandle;
	}

	/**
	 * Creates an IMethod from the given method declaration and type.
	 */
	protected IModelElement createHandle(MethodDeclaration method,
			IModelElement parent) {
		// if (!(parent instanceof IType)) return parent;
		if (parent instanceof IType) {
			IType type = (IType) parent;
			return createMethodHandle(type, new String(method.getName()));
		} else if (parent instanceof ISourceModule) {
			if (method.getDeclaringTypeName() != null) {
				return createMethodHandle((ISourceModule) parent, method
						.getDeclaringTypeName()
						+ "::" + method.getName());
			} else {
				return createMethodHandle((ISourceModule) parent, method
						.getName());
			}
		}
		return null;
	}
	protected IModelElement createTypeHandle(IType parent, String name) {
		if( name.indexOf( "::") != -1 ) {
			String[] split = name.split("::");
			IType e = parent;
			for (int i = 0; i < split.length; i++) {
				e = e.getType(split[i]);
				if( e == null ) {
					return null;
				}
			}
			if( e != null) {
				return e;
			}
		}
		return super.createTypeHandle(parent, name);
	}
	protected IType createTypeHandle(String name) {
		Openable openable = this.currentPossibleMatch.openable;
		if (openable instanceof SourceModule
				|| openable instanceof ExternalSourceModule
				|| openable instanceof BuiltinSourceModule) {
			IParent e = ((IParent) openable);
			if( name.indexOf( "::") != -1 ) {
				String[] split = name.split("::");
				for (int i = 0; i < split.length; i++) {
					if( e instanceof ISourceModule) {
						e = ((ISourceModule)e).getType(split[i]);	
					}
					else if( e instanceof IType ) {
						e = ((IType)e).getType(split[i]);
					}
					else {
						e = null;
					}
					if( e == null ) {
						return null;
					}
				}
				if( e != null && e instanceof IType ) {
					return (IType)e;
				}
			}
		}
		return super.createTypeHandle(name);
	}
}
