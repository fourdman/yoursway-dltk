package org.eclipse.dltk.ast.statements;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.DLTKToken;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.utils.CorePrinter;

/**
 * While statement.
 */
public class WhileStatement extends Statement {
	private Expression fCondition;
	private Statement fAction;

	public WhileStatement(DLTKToken token) {
		super(token);
	}

	public WhileStatement(DLTKToken whileToken, Expression condition,
			Statement action) {
		this.setStart(whileToken.getColumn());
		this.fCondition = condition;
		this.fAction = action;
	}

	public void traverse(ASTVisitor pVisitor) throws Exception {
		if (pVisitor.visit(this)) {
			if (fCondition != null) {
				fCondition.traverse(pVisitor);
			}
			if (fAction != null) {
				fAction.traverse(pVisitor);
			}
			pVisitor.endvisit(this);
		}
	}

	public int getKind() {
		return S_WHILE;
	}

	public Expression getCondition() {
		return fCondition;
	}

	public Statement getAction() {
		return fAction;
	}

	public void printNode(CorePrinter output) {
		output.formatPrintLn("while");
		if (this.fCondition != null) {
			this.fCondition.printNode(output);
		}
		if (this.fAction != null) {
			output.indent();
			this.fAction.printNode(output);
			output.dedent();
		}
		output.formatPrint("");
	}
}
