/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package org.eclipse.dltk.evaluation.types;

import org.eclipse.dltk.ti.types.IEvaluatedType;

public class AmbiguousType implements IEvaluatedType {

	private final IEvaluatedType[] possibleTypes;

	public AmbiguousType(IEvaluatedType[] possibleTypes) {
		this.possibleTypes = possibleTypes;
	}

	public String getTypeName() {
		StringBuffer result = new StringBuffer();
		result.append("Ambigous <"); //$NON-NLS-1$
		for (int i = 0; i < possibleTypes.length; i++) {
			IEvaluatedType type = possibleTypes[i];
			if (i > 0) {
				result.append(", "); //$NON-NLS-1$
			}
			result.append(type.getTypeName());
		}
		result.append(">"); //$NON-NLS-1$
		return result.toString();
	}

	public IEvaluatedType[] getPossibleTypes() {
		return possibleTypes;
	}

	public boolean subtypeOf(IEvaluatedType type) {
		// TODO Auto-generated method stub
		return false;
	}

}
