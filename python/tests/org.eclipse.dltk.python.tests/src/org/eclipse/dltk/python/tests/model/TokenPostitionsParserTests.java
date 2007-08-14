package org.eclipse.dltk.python.tests.model;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.core.tests.model.SuiteOfTestCases;
import org.eclipse.dltk.core.tests.model.SuiteOfTestCases.Suite;
import org.eclipse.dltk.python.internal.core.parser.PythonSourceParser;
import org.eclipse.dltk.python.parser.ast.PythonWhileStatement;
import org.eclipse.dltk.python.parser.ast.expressions.PrintExpression;
import org.eclipse.dltk.python.parser.ast.expressions.PythonTestListExpression;

public class TokenPostitionsParserTests extends SuiteOfTestCases {

	private static final String whileScript = "a=1; while a>0 : a=a-1;";
	private static final String whileElseScript = "a=1; while a>0 : a=a-1; else : a = 1;";
	private static final String testExprScript = "print \"Hello,\", \"World!\"";
	
	private static final String msg = "Invalid token displacement";

	public static Test suite() {
		return new Suite(TokenPostitionsParserTests.class);
	}
	
	public TokenPostitionsParserTests() {
		super("Token positions parser test case");
	}
	private static void testWhileStatements(String script) throws Exception
	{
		PythonSourceParser parser = new PythonSourceParser();
		ModuleDeclaration module = parser.parse(null, script.toCharArray(), null);
		List children = ((ASTNode)module.getChilds().iterator().next()).getChilds();
		Iterator iter = children.iterator();
		while (iter.hasNext())
		{
			ASTNode node = (ASTNode)iter.next();
			if (node instanceof PythonWhileStatement)
			{
				PythonWhileStatement whileStmt = (PythonWhileStatement)node;
				if (null != whileStmt.getElseStatement())
				{
					assertTrue(msg,whileStmt.sourceStart() < whileStmt.sourceEnd());
					assertTrue(msg, whileStmt.sourceEnd() == whileStmt.getElseStatement().sourceEnd());
					return;
				}
				else
				{
					Iterator j = whileStmt.getChilds().iterator();
					while (j.hasNext())
					{
						ASTNode child = (ASTNode)j.next();
						if (child instanceof Block) {
							Block block = (Block) child;
							assertTrue(msg,whileStmt.sourceStart() < whileStmt.sourceEnd());
							assertTrue(msg, whileStmt.sourceEnd() == block.sourceEnd());  
						}
					}
					return;
				}
			}
		}
		throw new Exception("Ths test is invalid.");
	}
	public void testWhileStatement() throws Exception
	{
		testWhileStatements(whileScript);
	}
	public void testWhileEsleStatement() throws Exception
	{
		testWhileStatements(whileElseScript);
	}
	public void testTestListExpr()
	{
		PythonSourceParser parser = new PythonSourceParser();
		ModuleDeclaration module = parser.parse(null, testExprScript.toCharArray(), null);
		List children = ((ASTNode)module.getChilds().iterator().next()).getChilds();
		PrintExpression printExpr = (PrintExpression)children.get(0);
		PythonTestListExpression testListExpr = (PythonTestListExpression) printExpr.getChilds().get(0);
		assertTrue(msg, testListExpr.sourceEnd() > testListExpr.sourceStart() && testListExpr.sourceStart() > printExpr.sourceStart());
	}
}
