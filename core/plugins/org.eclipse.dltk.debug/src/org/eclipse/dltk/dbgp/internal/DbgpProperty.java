/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.dbgp.internal;

import org.eclipse.dltk.dbgp.IDbgpProperty;

public class DbgpProperty implements IDbgpProperty {

	private final String name;

	private final String fullName;

	private final String type;

	private final String value;

	private final boolean constant;

	private final int size;

	private final int childrenCount;

	private final IDbgpProperty[] availableChildren;

	private final boolean hasChildren;

	private final String key;

	public DbgpProperty(String name, String fullName, String type,
			String value, int size, int childrenCount, boolean hasChildren,
			boolean constant, String key, IDbgpProperty[] availableChildren) {
		this.name = name;
		this.fullName = fullName;
		this.type = type;
		this.value = value;
		this.size = size;
		this.childrenCount = childrenCount;
		this.availableChildren = availableChildren;
		this.hasChildren = hasChildren;
		this.constant = constant;
		this.key = key;
	}

	public String getFullName() {
		return fullName;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public int getSize() {
		return size;
	}

	public boolean hasChildren() {
		return hasChildren;
	}

	public int getChildrenCount() {
		return childrenCount;
	}

	public IDbgpProperty[] getAvailableChildren() {
		return (IDbgpProperty[]) availableChildren.clone();
	}

	public boolean isConstant() {
		return constant;
	}

	public String toString() {
		return "DbgpProperty (Name: " + name + "; Full name: " + fullName
				+ "; Type: " + type + "; Value: " + value + ")";
	}

	public String getKey() {
		return key;
	}
}
