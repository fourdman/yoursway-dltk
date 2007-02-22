/*
 * Created on 06.06.2004
 * 
 * TODO To change the template for this generated file go to Window - Preferences - Script - Code Style - Code Templates
 */
package org.eclipse.dltk.ast.expressions;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.DLTKToken;
import org.eclipse.dltk.utils.CorePrinter;

/**
 * Unary expression.
 * 
 * Such as ++x, --x, +x, -x and other.
 * 
 */
public class UnaryExpression extends Expression {

	/**
	 * Expression.
	 */
	protected Expression expression;

	/**
	 * Unary kind.
	 */
	protected int kind;

	/**
	 * Construct from position information, kind and expression.
	 * 
	 * @param start -
	 *            start position.
	 * @param end -
	 *            end position.
	 * @param knd -
	 *            unary kind.
	 * @see ExpressionConstants.
	 * @param expression -
	 *            expression.
	 */
	public UnaryExpression(int start, int end, int knd, Expression expression) {
		super(start, end);
		this.kind = knd;
		this.expression = expression;
	}

	/**
	 * Construct from ANTLR token, kind and expression.
	 * 
	 * @param t -
	 *            ANTLR token.
	 * @param knd -
	 *            unary expression lind.
	 * @see ExpresionConstants.
	 * @param expression -
	 *            expression.
	 */
	public UnaryExpression(DLTKToken t, int knd, Expression expression) {
		super(t);
		if (expression != null) {
			this.setEnd(expression.sourceEnd());
		}
		this.kind = knd;
		this.expression = expression;
	}

	/**
	 * Traverse.
	 */
	public void traverse(ASTVisitor pVisitor) throws Exception {
		if (pVisitor.visit(this)) {
			if (expression != null) {
				expression.traverse(pVisitor);
			}
			pVisitor.endvisit(this);
		}
	}

	/**
	 * Return unary expression kind.
	 */

	public int getKind() {
		return kind;
	}

	/**
	 * Return expression.
	 * 
	 * @return
	 */
	public Expression getExpression() {
		return this.expression;
	}

	/**
	 * Testing purposes only. Used to print unary expression.
	 */
	public void printNode(CorePrinter output) {
		output.formatPrintLn(this.getOperator());
		if (this.expression != null) {
			this.expression.printNode(output);
		}
	}
}
