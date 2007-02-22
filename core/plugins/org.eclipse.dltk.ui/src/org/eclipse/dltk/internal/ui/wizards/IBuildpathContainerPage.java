/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.dltk.internal.ui.wizards;

import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.jface.wizard.IWizardPage;


/**
 * A buildpath container page allows the user to create a new or edit an 
 * existing buildpath container entry.
 * <p>
 * Clients should implement this interface and include the name of their 
 * class in an extension contributed to the dltk.ui's buildpath container page 
 * extension point (named <code>org.eclipse.dltk.ui.buildpathContainerPage
 * </code>).
 * </p>
 * <p>
 * Clients implementing this interface may subclass from 
 * <code>org.eclipse.jface.wizard.WizardPage</code>.
 * </p>
 * Clients implementing this interface may also implement
 * <code>IBuildpathContainerPageExtension</code> to get additional context
 * before this page is opened.
 *
	 *
 */
public interface IBuildpathContainerPage extends IWizardPage {
	
	/**
	 * Called when the buildpath container wizard is closed by selecting 
	 * the finish button. Implementers typically override this method to 
	 * store the page result (new/changed buildpath entry returned in 
	 * getSelection) into its model.
	 * 
	 * @return if the operation was successful. Only when returned
	 * <code>true</code>, the wizard will close.
	 */
	public boolean finish();
	
	/**
	 * Returns the edited or created buildpath container entry. This method
	 * may return <code>null</code> if no buildpath container entry exists.
	 * The returned buildpath entry is of kind <code>IBuildpathEntry.BPE_CONTAINER
	 * </code>.
	 * 
	 * @return the buildpath entry edited or created on the page.
	 */
	public IBuildpathEntry getSelection();
	
	/**
	 * Sets the buildpath container entry to be edited or <code>null</code> 
	 * if a new entry should be created.
	 * 
	 * @param containerEntry the buildpath entry to edit or <code>null</code>.
	 * If not <code>null</code> then the buildpath entry must be of
	 * kind <code>IBuildpathEntry.BPE_CONTAINER</code>
	 */
	public void setSelection(IBuildpathEntry containerEntry);	
}
