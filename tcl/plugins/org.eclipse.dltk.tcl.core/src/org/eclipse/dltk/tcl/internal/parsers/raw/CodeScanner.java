/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.parsers.raw;

public class CodeScanner {

	public static final int EOF = -1;

	private char[] content;
	private int pos;

	public CodeScanner(String content) {
		if (content != null) {
			this.content = content.toCharArray();
		} else {
			this.content = null;
		}
		pos = 0;
	}

	public int read() {
		if (isEOF())
			return EOF;
		char c = content[pos];
		pos++;
		return c;
	}

	public boolean isEOF() {
		return pos >= content.length;
	}

	public void unread() {
		pos--;
	}

	public int getPosition() {
		if (isEOF()) {
			return content.length - 1;
		} else {
			return pos;
		}
	}

}