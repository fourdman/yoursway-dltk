package org.eclipse.dltk.ui.templates;

import org.eclipse.dltk.internal.ui.editor.ScriptSourceViewer;
import org.eclipse.dltk.internal.ui.preferences.ScriptSourcePreviewerUpdater;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.persistence.TemplatePersistenceData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;

public abstract class ScriptTemplatePreferencePage extends TemplatePreferencePage
		implements IWorkbenchPreferencePage {
	
	public boolean performOk() {
		boolean ok = super.performOk();

		// TODO: save plugin prefereces

		return ok;
	}
	
	protected abstract ScriptSourceViewerConfiguration createSourceViewerConfiguration(IDocument document); 
		
	
	protected SourceViewer createViewer(Composite parent) {
		IDocument document = new Document();
		
		IPreferenceStore store = getPreferenceStore();
		
		ScriptSourceViewerConfiguration configuration = createSourceViewerConfiguration(document);
		
		
		SourceViewer viewer = new ScriptSourceViewer(parent, null, null, false,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL, store);
		
		viewer.configure(configuration);
		viewer.setEditable(false);
		viewer.setDocument(document);
		
		Control control = viewer.getControl();
		control.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.FILL_VERTICAL));

		// Font font = JFaceResources
		// .getFont(TPreferenceConstants.EDITOR_TEXT_FONT);
		// viewer.getTextWidget().setFont(font);
		
		new ScriptSourcePreviewerUpdater(viewer, configuration, store);

		return viewer;
	}

	protected void updateViewerInput() {
		IStructuredSelection selection = (IStructuredSelection) getTableViewer()
				.getSelection();
		SourceViewer viewer = getViewer();

		if (selection.size() == 1
				&& selection.getFirstElement() instanceof TemplatePersistenceData) {
			TemplatePersistenceData data = (TemplatePersistenceData) selection
					.getFirstElement();
			Template template = data.getTemplate();
			String contextId = template.getContextTypeId();

			IDocument doc = viewer.getDocument();

			String start = null;
			if ("rdoc".equals(contextId)) { //$NON-NLS-1$
				start = "/**" + doc.getLegalLineDelimiters()[0]; //$NON-NLS-1$
			} else
				start = ""; //$NON-NLS-1$

			doc.set(start + template.getPattern());
			int startLen = start.length();
			viewer.setDocument(doc, startLen, doc.getLength() - startLen);

		} else {
			viewer.getDocument().set(""); //$NON-NLS-1$
		}
	}

	protected boolean isShowFormatterSetting() {
		return false;
	}
	
	// protected String getFormatterPreferenceKey() {
	// return PreferenceConstants.TEMPLATES_USE_CODEFORMATTER;
	// }
}