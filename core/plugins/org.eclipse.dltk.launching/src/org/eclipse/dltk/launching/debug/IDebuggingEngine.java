package org.eclipse.dltk.launching.debug;

import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterRunner;

public interface IDebuggingEngine {
	String getId();

	String getModelId();

	String getNatureId();

	String getName();

	String getDescription();

	int getPriority();

	IInterpreterRunner getRunner(IInterpreterInstall install);
}