/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui.editor;

import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.core.IMember;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.internal.ui.editor.AnnotatedImageDescriptor;
import org.eclipse.dltk.ruby.core.RubyConstants;
import org.eclipse.dltk.ruby.internal.ui.RubyImages;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.dltk.ui.viewsupport.ImageImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class RubyOutlineLabelDecorator extends LabelProvider implements
		ILabelDecorator {
	protected static class RubyOutlineImageDescriptor extends
			AnnotatedImageDescriptor {

		private int flags;

		public RubyOutlineImageDescriptor(ImageDescriptor baseImageDescriptor,
				Point size, int flags) {
			super(baseImageDescriptor, size);
									
			this.flags = flags;
		}

		protected void drawAnnotations() {
			ImageData data = null;
			
			// Need more correct handling of flags
			// Label generation will be moved in ruby specific class
			if ((flags & Modifiers.AccStatic) != 0) {
				data = getImageData(RubyImages.DESC_OVR_STATIC_FIELD);
			} 
			else if ((flags & Modifiers.AccConstant) !=0 ) {
				data = getImageData(RubyImages.DESC_OVR_CONST_FIELD);
			}
			
			if (data != null) {
				drawImageTopRight(data);
			}
			
			if ((flags & RubyConstants.RubyAttributeModifier) != 0) {
				data = getImageData(DLTKPluginImages.DESC_OVR_ABSTRACT);
				drawImageTopLeft(data);
			}
			
			if ((flags & RubyConstants.RubyAliasModifier) != 0) {
				data = getImageData(DLTKPluginImages.DESC_OVR_CALLER);
				drawImageTopLeft(data);
			}
			
		}
	}

	public RubyOutlineLabelDecorator() {
	}

	public String decorateText(String text, Object element) {
		if (element instanceof IMethod) {
			IMethod method = (IMethod) element;
			try {
				if ((method.getFlags() & RubyConstants.RubyAliasModifier) != 0) {
					String oldName = method.getParameters()[0];
					return method.getElementName();// + " [alias for " + oldName + "]";
				}
			} catch (ModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return text;
	}

	public Image decorateImage(Image image, Object obj) {

		try {
			if (obj instanceof IMember) {
				IMember member = (IMember) obj;
				int flags = member.getFlags();

				ImageDescriptor baseImage = new ImageImageDescriptor(image);
				Rectangle bounds = image.getBounds();

				ImageDescriptor dsc = new RubyOutlineImageDescriptor(baseImage,
						new Point(bounds.width, bounds.height), flags);

				return dsc.createImage();
			}

		} catch (ModelException e) {
			e.printStackTrace();
		}

		return image;
	}
}
