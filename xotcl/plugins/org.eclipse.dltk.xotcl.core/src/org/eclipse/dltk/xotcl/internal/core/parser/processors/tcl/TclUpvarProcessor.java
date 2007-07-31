package org.eclipse.dltk.xotcl.internal.core.parser.processors.tcl;

import org.eclipse.dltk.ast.ASTListNode;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.tcl.ast.TclStatement;
import org.eclipse.dltk.tcl.internal.parsers.raw.TclCommand;
import org.eclipse.dltk.xotcl.core.AbstractTclCommandProcessor;
import org.eclipse.dltk.xotcl.core.ITclParser;
import org.eclipse.dltk.xotcl.core.TclParseUtil;
import org.eclipse.dltk.xotcl.core.ast.TclUpvarVariableDeclaration;

public class TclUpvarProcessor extends AbstractTclCommandProcessor {

	public TclUpvarProcessor() {
	}

	public ASTNode process(TclCommand command, ITclParser parser, int offset, ASTNode parent) {
		TclStatement statement = (TclStatement) parser.processLocal(command, offset);
		int statementsCount = statement.getCount();
		if (statementsCount < 2) {
			// TODO: Add error reporting here.
			return null;
		}

		Expression level = statement.getAt(1);

		int startIndex = 1;

		if (level instanceof SimpleReference) {
			SimpleReference sLevel = (SimpleReference) level;
			String str = sLevel.getName();
			if (str == null || str.length() == 0) {
				// TODO: Add error reporting here
				return null;
			}
			if (str.startsWith("#") || str.startsWith("\\#")
					|| Character.isDigit(str.charAt(0))) {
				// first arg is a level
				startIndex++;
			}
		}

		Expression variableName = null;
		ASTNode ret = null;
		for (int i = startIndex; i < statementsCount; i += 2) {
			Expression at = statement.getAt(i + 1);
			variableName = at;
			if (variableName == null) {
				// throw new RuntimeException("empty variable name");
//				if (DLTKCore.DEBUG) {
//					System.out.println("Incorrect upvar variable declaration");
//				}
				return null;
			}
			SimpleReference variable = TclParseUtil.makeVariable(variableName);
			TclUpvarVariableDeclaration var = new TclUpvarVariableDeclaration(variable, null, at.sourceStart(), at.sourceEnd());
			if( ret == null ) {
				ret = var;
			}
			else {
				if(!( ret instanceof ASTListNode  )) {
					ASTListNode list = new ASTListNode();
					list.addNode(ret);
					list.setStart(ret.sourceStart());
					list.setEnd(ret.sourceEnd());
					ret = list;
				}
				((ASTListNode)ret).addNode(var);
				((ASTListNode)ret).setEnd(var.sourceEnd());
			}
		}
		if( ret != null ) {
			this.addToParent(parent, ret);
		}
		return ret;
	}
}