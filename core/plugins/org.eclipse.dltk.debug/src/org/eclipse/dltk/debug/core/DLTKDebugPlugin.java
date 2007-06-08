/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.debug.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.dltk.internal.debug.core.model.DbgpService;
import org.osgi.framework.BundleContext;

public class DLTKDebugPlugin extends Plugin {

	public static final String PLUGIN_ID = "org.eclipse.dltk.debug";

	public static final int INTERNAL_ERROR = 120;

	private static DLTKDebugPlugin fgPlugin;

	public static DLTKDebugPlugin getDefault() {
		return fgPlugin;
	}

	public DLTKDebugPlugin() {
		super();
		fgPlugin = this;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		
		//TODO: stop
	}

	public IDbgpService createDbgpService() throws Exception {
		return DbgpService.getService();
	}

	public IDbgpService creaeDbgpService(int port) throws Exception {
		return DbgpService.getService(port);
	}
	
	public IDbgpService createDbgpService(int portBegin, int portEnd) throws Exception {
		return DbgpService.getService(portBegin, portEnd);
	}

	// Logging
	public static void log(Throwable t) {
		Throwable top = t;
		if (t instanceof DebugException) {
			Throwable throwable = ((DebugException) t).getStatus()
					.getException();
			if (throwable != null) {
				top = throwable;
			}
		}

		log(new Status(IStatus.ERROR, PLUGIN_ID, INTERNAL_ERROR,
				"Internal error logged from DLTKDebugPlugin: ", top));
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}
}
