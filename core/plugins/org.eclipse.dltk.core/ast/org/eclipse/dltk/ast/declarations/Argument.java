/*
 * (c) 2002, 2005 xored software and others all rights reserved. http://www.xored.com
 */

package org.eclipse.dltk.ast.declarations;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.DLTKToken;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.utils.CorePrinter;

public class Argument extends Declaration {

	protected Expression initialization;

	public Argument(DLTKToken name, int start, int end, Expression init) {
		super(name, start, end);
		this.initialization = init;
	}

	public Argument(SimpleReference name, int start, Expression init, int mods) {
		super(start, 0);

		if (name != null) {
			this.setName(name.getName());
			this.setEnd(start + name.getName().length());
		}
		this.modifiers = mods;
		this.initialization = init;
		if (init != null) {
			this.setEnd(init.sourceEnd());
		}
	}

	public Argument() {
		super();
		this.setStart(0);
		this.setEnd(-1);
	}

	public int getKind() {
		return D_ARGUMENT;
	}

	/**
	 * Please don't use this function. Helper method for initializing Argument
	 * 
	 */
	public final void set(SimpleReference mn, Expression initialization) {
		this.initialization = initialization;
		this.setName(mn.getName());
		this.setStart(mn.sourceStart());
		this.setEnd(mn.sourceEnd());
	}

	public final Expression getInitialization() {
		return initialization;
	}

	public final void setInitializationExpression(Expression initialization) {
		this.initialization = initialization;
	}

	public void traverse(ASTVisitor visitor) throws Exception {
		if (visitor.visit(this)) {
			if (initialization != null) {
				initialization.traverse(visitor);
			}
			visitor.endvisit(this);
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(getName());
		if (initialization != null) {
			sb.append('=');
			sb.append(initialization);
		}
		return sb.toString();
	}

	public void printNode(CorePrinter output) {
		output.formatPrint("Argument" + this.getSourceRange().toString() + ":");
		output.formatPrintLn(super.toString());
	}

	public void setArgumentName(String name) {
		this.setName(name);
	}
}
