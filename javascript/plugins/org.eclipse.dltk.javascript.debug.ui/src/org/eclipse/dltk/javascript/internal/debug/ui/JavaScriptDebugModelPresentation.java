package org.eclipse.dltk.javascript.internal.debug.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.dltk.debug.core.DLTKDebugPlugin;
import org.eclipse.dltk.debug.core.model.IScriptBreakpoint;
import org.eclipse.dltk.debug.core.model.IScriptMethodEntryBreakpoint;
import org.eclipse.dltk.debug.core.model.IScriptValue;
import org.eclipse.dltk.debug.core.model.IScriptVariable;
import org.eclipse.dltk.debug.core.model.IScriptWatchpoint;
import org.eclipse.dltk.debug.ui.ScriptDebugImageDescriptor;
import org.eclipse.dltk.debug.ui.ScriptDebugImages;
import org.eclipse.dltk.debug.ui.ScriptDebugModelPresentation;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;

public class JavaScriptDebugModelPresentation extends
		ScriptDebugModelPresentation {
	private static final String JS_EDITOR_ID = "org.eclipse.dltk.javascript.ui.editor.JavascriptEditor";

	static ImageRegistry registry = new ImageRegistry(Display.getDefault());

	static {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {

				DLTKPluginImages
						.get(ScriptDebugImages.IMG_OBJS_CONTENDED_MONITOR);
			}

		});
	}

	public JavaScriptDebugModelPresentation() {

	}

	protected Image getBreakpointImage(IScriptBreakpoint breakpoint) {
		if (breakpoint instanceof IScriptWatchpoint) {
			IScriptWatchpoint w = (IScriptWatchpoint) breakpoint;
			try {
				if (w.isEnabled()) {
					return DebugUITools
							.getImage(IDebugUIConstants.IMG_OBJS_WATCHPOINT);
				}
			} catch (CoreException e) {
				DLTKDebugPlugin.log(e);
			}
			return DebugUITools
					.getImage(IDebugUIConstants.IMG_OBJS_WATCHPOINT_DISABLED);
		}
		if (breakpoint instanceof IScriptMethodEntryBreakpoint) {
			IScriptMethodEntryBreakpoint ll = (IScriptMethodEntryBreakpoint) breakpoint;
			int flags = 0;

			try {
				if (ll.breakOnEntry())
					flags |= ScriptDebugImageDescriptor.ENTRY;
				if (ll.breakOnExit())
					flags |= ScriptDebugImageDescriptor.EXIT;

				if (flags == 0)
					return DebugUITools
							.getImage(IDebugUIConstants.IMG_OBJS_BREAKPOINT_DISABLED);
				if (ll.isEnabled()) {
					String key = flags + "enabled";
					Image image = registry.get(key);
					if (image == null) {
						registry
								.put(
										key,
										new ScriptDebugImageDescriptor(
												DebugUITools
														.getImageDescriptor(IDebugUIConstants.IMG_OBJS_BREAKPOINT),
												flags));
						return registry.get(key);
					}
					return image;
				} else {
					String key = flags + "disabled";
					Image image = registry.get(key);
					if (image == null) {
						registry
								.put(
										key,
										new ScriptDebugImageDescriptor(
												DebugUITools
														.getImageDescriptor(IDebugUIConstants.IMG_OBJS_BREAKPOINT_DISABLED),
												flags));
						return registry.get(key);
					}
					return image;
				}
			} catch (CoreException e) {
				DLTKDebugPlugin.log(e);

			}
		}

		return null;
	}

	protected Image getVariableImage(IScriptVariable variable) {
		IScriptVariable v = variable;
		IScriptValue scriptValue;
		try {
			scriptValue = (IScriptValue) v.getValue();
		} catch (DebugException e) {
			return ScriptDebugImages
					.get(ScriptDebugImages.IMG_OBJS_LOCAL_VARIABLE);
		}
		String typeString = (scriptValue).getType().getName();
		if (typeString.equals("function"))
			return DLTKPluginImages.get(DLTKPluginImages.IMG_METHOD_PRIVATE);
		if (typeString.equals("javaclass"))
			return DLTKPluginImages.get(DLTKPluginImages.IMG_OBJS_CLASS);
		if (typeString.equals("javaobject"))
			return DLTKPluginImages.get(DLTKPluginImages.IMG_METHOD_PROTECTED);
		if (typeString.equals("javaarray"))
			return DLTKPluginImages.get(DLTKPluginImages.IMG_METHOD_DEFAULT);
		String fullName = scriptValue.getEvalName();
		if (fullName != null) {
			if (fullName.indexOf('.') >= 0 || (fullName.equals("this"))) {
				return DLTKPluginImages.get(DLTKPluginImages.IMG_METHOD_PUBLIC);
			}
		}
		return ScriptDebugImages.get(ScriptDebugImages.IMG_OBJS_LOCAL_VARIABLE);
	}

	public String getEditorId(IEditorInput input, Object element) {
		return JS_EDITOR_ID;
	}
}
