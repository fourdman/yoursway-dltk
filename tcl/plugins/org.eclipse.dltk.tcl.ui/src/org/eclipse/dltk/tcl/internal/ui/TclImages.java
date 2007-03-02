package org.eclipse.dltk.tcl.internal.ui;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.ui.ImageUtil;
import org.eclipse.jface.resource.ImageDescriptor;

public class TclImages {
	public static final IPath ICONS_PATH = new Path("/icons");

	private static final ImageUtil util = new ImageUtil(TclUI.getDefault()
			.getBundle(), ICONS_PATH);

	public static final ImageDescriptor DESC_WIZBAN_PROJECT_CREATION = util
			.createUnManaged(ImageUtil.T_WIZBAN, "projectcreate_wiz.png");

	public static final ImageDescriptor DESC_WIZBAN_FILE_CREATION = util
			.createUnManaged(ImageUtil.T_WIZBAN, "filecreate_wiz.png");
}