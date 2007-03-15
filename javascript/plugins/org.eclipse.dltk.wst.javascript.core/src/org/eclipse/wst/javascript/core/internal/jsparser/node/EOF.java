/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.javascript.core.internal.jsparser.node;



/* This file was generated by SableCC (http://www.sablecc.org/). */

import org.eclipse.wst.javascript.core.internal.jsparser.analysis.Analysis;

public final class EOF extends Token {

	public EOF() {
		setText("");//$NON-NLS-1$
	}

	public EOF(int line, int pos, int lpoffset) {
		setText("");//$NON-NLS-1$
		setLine(line);
		setPos(pos);
		setLPOffset(lpoffset);
		setContainsLineTerminator(false);
	}

	public void apply(Switch sw) {
		((Analysis) sw).caseEOF(this);
	}

	public Object clone() {
		return new EOF(getLine(), getPos(), getLPOffset());
	}
}