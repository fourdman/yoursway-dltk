package org.eclipse.dltk.xotcl.internal.core.parser.processors.xotcl;

import org.eclipse.dltk.ast.ASTListNode;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.tcl.ast.TclStatement;
import org.eclipse.dltk.tcl.internal.parsers.raw.TclCommand;
import org.eclipse.dltk.xotcl.core.AbstractTclCommandProcessor;
import org.eclipse.dltk.xotcl.core.ITclParser;
import org.eclipse.dltk.xotcl.core.TclParseUtil;
import org.eclipse.dltk.xotcl.core.ast.TclVariableDeclaration;
import org.eclipse.dltk.xotcl.core.ast.xotcl.XOTclMethodDeclaration;

public class XOTclClassMyProcessor extends AbstractTclCommandProcessor {

	public XOTclClassMyProcessor() {
	}

	public ASTNode process(TclCommand command, ITclParser parser, int offset,
			ASTNode parent) {
		TclStatement statement = (TclStatement) parser.processLocal(
				command, offset, parent);
		ModuleDeclaration moduleDeclaration = this.getModuleDeclaration();
		ASTNode scopeParent = TclParseUtil.getScopeParent(moduleDeclaration, parent);
		if (scopeParent instanceof XOTclMethodDeclaration) {
			if (statement.getCount() >= 3) {
				String operation = TclParseUtil.getNameFromNode(statement
						.getAt(1));
				if (operation.equals("instvar")) {
					ASTListNode vars = new ASTListNode();
					for (int i = 2; i < statement.getCount(); i++) {
						SimpleReference ref = (SimpleReference) statement.getAt(i);
						TclVariableDeclaration decl = new TclVariableDeclaration(ref,null, ref.sourceStart(), ref.sourceEnd());
						this.addToParent(parent, decl);
						vars.addNode(decl);
					}
					return vars;
				}
			}
		}
		return null;
	}
}