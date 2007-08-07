package org.eclipse.dltk.xotcl.internal.core.parser;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.tcl.ast.TclStatement;
import org.eclipse.dltk.tcl.internal.parsers.raw.TclCommand;
import org.eclipse.dltk.xotcl.core.ITclCommandDetector;
import org.eclipse.dltk.xotcl.core.ITclParser;
import org.eclipse.dltk.xotcl.core.TclParseUtil;
import org.eclipse.dltk.xotcl.core.ast.xotcl.XOTclInstanceVariable;
import org.eclipse.dltk.xotcl.core.ast.xotcl.XOTclObjectDeclaration;
import org.eclipse.dltk.xotcl.internal.core.XOTclKeywords;

public class XOTclCommandDetector implements ITclCommandDetector {
	// Options
	public static boolean INTERPRET_CLASS_UNKNOWN_AS_CREATE = true;

	public XOTclCommandDetector() {
		// TODO Auto-generated constructor stub
	}

	public CommandInfo detectCommand(TclCommand command, int offset,
			ModuleDeclaration module, ITclParser parser, ASTNode parent) {
		TclStatement statement = (TclStatement) parser.processLocal(command,
				offset);
		if (statement.getCount() == 0) {
			return null;
		}
		Expression commandName = statement.getAt(0);
		if (commandName instanceof SimpleReference) {
			String value = ((SimpleReference) commandName).getName();
			if (value.equals("Class")) {
				return checkClass(statement, module, parser, parent);
			} else if (value.equals("Object")) {
				return checkObject(statement, module, parser, parent);
			} else {
				return checkInstanceOperations(module, parent, statement,
						parser);
			}
		}
		return null;
	}

	private CommandInfo checkInstanceOperations(ModuleDeclaration module,
			ASTNode parent, TclStatement statement, ITclParser parser) {
		Expression commandName = statement.getAt(0);
		if (!(commandName instanceof SimpleReference)) {
			// TODO: Add handling of this.
			return null;
		}
		String commandNameValue = ((SimpleReference) commandName).getName();

		TypeDeclaration type = TclParseUtil.findXOTclTypeDeclarationFrom(
				module, parent, commandNameValue);
		Expression arg = statement.getAt(1);
		if (type != null) {
			if (arg instanceof SimpleReference) {
				String value = ((SimpleReference) arg).getName();
				if (!value.equals("create")) {
					CommandInfo info = checkCommands(value, type);
					if (info != null) {
						return info;
					}
				} else {
					return new CommandInfo("#Class#$newInstance", type);
				}

				return new CommandInfo("#Class#$ProcCall", type);
			}
		}

		// Find Object instance
		XOTclObjectDeclaration decl = TclParseUtil.findXOTclObjectInstanceFrom(
				module, parent, commandNameValue);
		if (decl != null) {
			if (arg instanceof SimpleReference) {
				String value = ((SimpleReference) arg).getName();
				if (value.equals("proc")) {
					return new CommandInfo("#Class#proc", decl);
				}
			}
			// Method call
			return new CommandInfo("#Class#$ProcCall", type);
		}
		// Letch check possibly this is method call for existing instance
		// variable.
		XOTclInstanceVariable variable = TclParseUtil
				.findXOTclInstanceVariableDeclarationFrom(module, parent,
						commandNameValue);
		if (variable != null) {
			// Add support of procs etc.
			return new CommandInfo("#Class#$MethodCall", variable);
		}
		return null;
	}

	private CommandInfo checkCommands(String value, TypeDeclaration decl) {
		CommandInfo info = checkClassOperator(decl, value,
				XOTclKeywords.XOTclCommandClassArgs, "#Class#");
		if (info != null) {
			return info;
		}
		info = checkClassOperator(decl, value,
				XOTclKeywords.XOTclCommandObjectArgs, "#Object#");
		if (info != null) {
			return info;
		}
		return null;
	}

	private CommandInfo checkClassOperator(TypeDeclaration type, String value,
			String[] commands, String prefix) {
		for (int q = 0; q < commands.length; q++) {
			if (value.equals(commands[q])) {
				return new CommandInfo(prefix + value, type);
			}
		}
		return null;
	}

	private CommandInfo checkClass(TclStatement statement,
			ModuleDeclaration module, ITclParser parser, ASTNode parent) {

		Expression arg = statement.getAt(1);
		if (arg instanceof SimpleReference) {
			String value = ((SimpleReference) arg).getName();

			for (int i = 0; i < XOTclKeywords.XOTclCommandClassArgs.length; i++) {
				if (value.equals(XOTclKeywords.XOTclCommandClassArgs[i])) {
					return new CommandInfo("#Class#" + value, null);
				}
			}
			CommandInfo info = checkCreateType(statement, parent, arg, value);
			if (info != null) {
				return info;
			}
			// Else unknown command or create command.
			if (INTERPRET_CLASS_UNKNOWN_AS_CREATE) {
				return new CommandInfo("#Class#create", null);
			}

			return null;
		}
		return null;
	}

	private CommandInfo checkObject(TclStatement statement,
			ModuleDeclaration module, ITclParser parser, ASTNode parent) {

		Expression arg = statement.getAt(1);
		if (arg instanceof SimpleReference) {
			String value = ((SimpleReference) arg).getName();

			for (int i = 0; i < XOTclKeywords.XOTclCommandObjectArgs.length; i++) {
				if (value.equals(XOTclKeywords.XOTclCommandObjectArgs[i])) {
					return new CommandInfo("#Object#" + value, null);
				}
			}
			CommandInfo info = checkCreateType(statement, parent, arg, value);
			if (info != null) {
				return info;
			}
			// // Else unknown command or create command.
			// if (INTERPRET_CLASS_UNKNOWN_AS_CREATE) {
			// return new CommandInfo("#Class#create", null);
			// }

			return null;
		}
		return null;
	}

	private CommandInfo checkCreateType(TclStatement statement, ASTNode parent,
			Expression arg, String value) {
		if (value.equals("instproc") || value.equals("proc")
				|| value.equals("set")) {
			TypeDeclaration decl = createTypeAdd(statement, parent, arg, value);
			CommandInfo info = checkCommands(value, decl);
			if (info != null) {
				return info;
			}
		}
		return null;
	}

	private TypeDeclaration createTypeAdd(TclStatement statement,
			ASTNode parent, Expression arg, String value) {
		TypeDeclaration decl = new TypeDeclaration(value,
				arg.sourceStart(), arg.sourceEnd(),
				statement.sourceStart(), statement.sourceEnd());
		TclParseUtil.addToDeclaration(parent, decl);
		return decl;
	}
}
