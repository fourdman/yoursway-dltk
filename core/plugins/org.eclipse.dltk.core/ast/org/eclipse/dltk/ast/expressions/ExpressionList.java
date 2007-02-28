/*
 * (c) 2002, 2005 xored software and others all rights reserved. http://www.xored.com
 */
package org.eclipse.dltk.ast.expressions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.utils.CorePrinter;

/**
 * Holds list of expressions.
 */
public class ExpressionList extends Expression {
	private List fExpressions = new ArrayList();

	/**
	 * Construct from position bindings.
	 */
	protected ExpressionList(int start, int end) {
		super(start, end);
	}

	/**
	 * Construct without position bindings. And without elements. By default
	 * expression list is initialized. So you can call getExpressions after
	 * creation.
	 * 
	 */
	public ExpressionList() {

	}

	public void traverse(ASTVisitor pVisitor) throws Exception {
		if (pVisitor.visit(this)) {
			Iterator i = fExpressions.iterator();
			while (i.hasNext()) {
				Expression e = (Expression) i.next();
				e.traverse(pVisitor);
			}
			pVisitor.endvisit(this);
		}
	}

	public int getKind() {
		return E_EXPRESSION_LIST;
	}

	public void setExpresssions(List exs) {
		Iterator i = exs.iterator();
		while (i.hasNext()) {
			Expression e = (Expression) i.next();
			this.addExpression(e);
		}
	}

	/**
	 * Add expression to current list. If expressions list is null, then is is
	 * created.
	 */
	public void addExpression(Expression ex) {
		if (this.fExpressions == null) {
			this.fExpressions = new ArrayList/* < Expression > */();
		}
		if (ex != null) {
			this.fExpressions.add(ex);
		}
	}

	public List getExpressions() {
		return fExpressions;
	}

	/**
	 * Testing purposes only. Prints all expressions.
	 */
	public void printNode(CorePrinter output) {
		if (this.fExpressions != null) {
			int index = 0;
			Iterator i = fExpressions.iterator();
			while (i.hasNext()) {
				Expression expr = (Expression) i.next();

				expr.printNode(output);

				if (index != this.fExpressions.size() - 1) {
					output.formatPrintLn(", ");
				}
				index += 1;
			}
		}
	}
}
