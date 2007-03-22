package org.eclipse.dltk.internal.ui.text;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.IWorkingCopyManager;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;


public class ScriptReconcilingStrategy implements IReconcilingStrategy,
		IReconcilingStrategyExtension {
	private ITextEditor fEditor;

	private IWorkingCopyManager fManager;

	private IDocumentProvider fDocumentProvider;

	private IProgressMonitor fProgressMonitor;
	
	private IScriptReconcilingListener fJavaReconcilingListener;
	private boolean fIsScriptReconcilingListener;
	
	private boolean fNotify= true;
	
	public ScriptReconcilingStrategy(ITextEditor editor) {
		fEditor = editor;
		fManager = DLTKUIPlugin.getDefault().getWorkingCopyManager();
		fDocumentProvider = DLTKUIPlugin.getDefault()
				.getSourceModuleDocumentProvider();
		
		fIsScriptReconcilingListener= fEditor instanceof IScriptReconcilingListener;
	}
	
	private IProblemRequestorExtension getProblemRequestorExtension() {
		IAnnotationModel model= fDocumentProvider.getAnnotationModel(fEditor.getEditorInput());
		if (model instanceof IProblemRequestorExtension)
			return (IProblemRequestorExtension) model;
		return null;
	}
	
	protected void reconcile(final boolean initialReconcile) {	
		final ISourceModule unit = fManager.getWorkingCopy(fEditor
				.getEditorInput());

		if (unit == null) {
			return;
		}

		try {
			SafeRunner.run(new ISafeRunnable() {
			public void run() {
				try {					
					/* fix for missing cancel flag communication */
					IProblemRequestorExtension extension= getProblemRequestorExtension();
					if (extension != null) {
						extension.setProgressMonitor(fProgressMonitor);
						extension.setIsActive(true);
					}

					try {
						// reconcile
						synchronized (unit) {
							unit.reconcile(true, null, fProgressMonitor);
						}
					} catch (OperationCanceledException ex) {
						Assert.isTrue(fProgressMonitor == null
								|| fProgressMonitor.isCanceled());
					} finally {
						/* fix for missing cancel flag communication */
						if (extension != null) {
							extension.setProgressMonitor(null);
							extension.setIsActive(false);
						}
					}
				} catch (ModelException ex) {
					handleException(ex);
				}
			}

			public void handleException(Throwable ex) {
				IStatus status = new Status(IStatus.ERROR,
						DLTKUIPlugin.PLUGIN_ID, IStatus.OK,
						"Error in DLTK Core during reconcile", ex); //$NON-NLS-1$
				DLTKUIPlugin.getDefault().getLog().log(status);
			}
		});
		}
		finally {
			// Always notify listeners, see https://bugs.eclipse.org/bugs/show_bug.cgi?id=55969 for the final solution
			try {
				if (fIsScriptReconcilingListener) {
					IProgressMonitor pm= fProgressMonitor;
					if (pm == null)
						pm= new NullProgressMonitor();
					fJavaReconcilingListener.reconciled(unit, !fNotify, pm);
				}
			} finally {
				fNotify= true;
			}
		}
	}

	public void aboutToBeReconciled() {

	}

	public void setDocument(IDocument document) {
		
	}

	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		reconcile(false);
	}

	public void reconcile(IRegion partition) {
		reconcile(false);
	}

	public void setProgressMonitor(IProgressMonitor monitor) {
		fProgressMonitor = monitor;
	}

	public void initialReconcile() {
		reconcile(true);
	}
}
