package org.eclipse.dltk.xotcl.core.tests;

import org.eclipse.dltk.xotcl.core.tests.parser.XOTclComandProcessorTests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.ecipse.dltk.xotcl.core.tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(XOTclComandProcessorTests.class);
		//$JUnit-END$
		return suite;
	}

}
