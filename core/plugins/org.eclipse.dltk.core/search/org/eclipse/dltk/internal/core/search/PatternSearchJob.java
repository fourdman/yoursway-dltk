/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.dltk.internal.core.search;

import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.dltk.core.search.IDLTKSearchScope;
import org.eclipse.dltk.core.search.SearchParticipant;
import org.eclipse.dltk.core.search.SearchPattern;
import org.eclipse.dltk.core.search.index.Index;
import org.eclipse.dltk.core.search.indexing.IndexManager;
import org.eclipse.dltk.core.search.indexing.ReadWriteMonitor;
import org.eclipse.dltk.core.search.matching.MatchLocator;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.internal.core.search.processing.IJob;
import org.eclipse.dltk.internal.core.search.processing.JobManager;


public class PatternSearchJob implements IJob {

	protected SearchPattern pattern;

	protected IDLTKSearchScope scope;

	protected SearchParticipant participant;

	protected IndexQueryRequestor requestor;

	protected boolean areIndexesReady;

	protected long executionTime = 0;

	public PatternSearchJob(SearchPattern pattern,
			SearchParticipant participant, IDLTKSearchScope scope,
			IndexQueryRequestor requestor) {
		this.pattern = pattern;
		this.participant = participant;
		this.scope = scope;
		this.requestor = requestor;
	}

	public boolean belongsTo(String jobFamily) {
		return true;
	}

	public void cancel() {
		// search job is cancelled through progress
	}

	public void ensureReadyToRun() {
		if (!this.areIndexesReady)
			getIndexes(null/* progress */); // may trigger some index recreation
	}

	public boolean execute(IProgressMonitor progressMonitor) {
		if (progressMonitor != null && progressMonitor.isCanceled())
			throw new OperationCanceledException();

		boolean isComplete = COMPLETE;
		executionTime = 0;
		Index[] indexes = getIndexes(progressMonitor);
		try {
			int max = indexes.length;
			if (progressMonitor != null)
				progressMonitor.beginTask("", max); //$NON-NLS-1$
			for (int i = 0; i < max; i++) {
				isComplete &= search(indexes[i], progressMonitor);
				if (progressMonitor != null) {
					if (progressMonitor.isCanceled())
						throw new OperationCanceledException();
					progressMonitor.worked(1);
				}
			}
			if (JobManager.VERBOSE)
				System.err.println("-> execution time: " + executionTime + "ms - " + this);//$NON-NLS-1$//$NON-NLS-2$
			return isComplete;
		} finally {
			if (progressMonitor != null)
				progressMonitor.done();
		}
	}

	public Index[] getIndexes(IProgressMonitor progressMonitor) {
		// acquire the in-memory indexes on the fly
		IPath[] indexLocations = this.participant.selectIndexes(this.pattern,
				this.scope);
		int length = indexLocations.length;
		Index[] indexes = new Index[length];
		int count = 0;
		IndexManager indexManager = ModelManager.getModelManager()
				.getIndexManager();
		for (int i = 0; i < length; i++) {
			if (progressMonitor != null && progressMonitor.isCanceled())
				throw new OperationCanceledException();
			// may trigger some index recreation work
			String indexLocation = indexLocations[i].toOSString();
			Index index = indexManager.getIndex(indexLocation);
			if (index == null) {
				// only need containerPath if the index must be built
				IPath containerPath = (IPath) indexManager.indexLocations
						.keyForValue(indexLocation);
				if (containerPath != null) // sanity check
					index = indexManager
							.getIndex(containerPath, indexLocation,
									true /* reuse index file */, false /*
																		 * do
																		 * not
																		 * create
																		 * if
																		 * none
																		 */);
			}
			if (index != null)
				indexes[count++] = index; // only consider indexes which are
											// ready
		}
		if (count == length)
			this.areIndexesReady = true;
		else
			System.arraycopy(indexes, 0, indexes = new Index[count], 0, count);
		return indexes;
	}

	public boolean search(Index index, IProgressMonitor progressMonitor) {
		if (index == null)
			return COMPLETE;
		if (progressMonitor != null && progressMonitor.isCanceled())
			throw new OperationCanceledException();

		ReadWriteMonitor monitor = index.monitor;
		if (monitor == null)
			return COMPLETE; // index got deleted since acquired
		try {
			monitor.enterRead(); // ask permission to read
			long start = System.currentTimeMillis();
			MatchLocator.findIndexMatches(this.pattern, index, requestor,
					this.participant, this.scope, progressMonitor);
			executionTime += System.currentTimeMillis() - start;
			return COMPLETE;
		} catch (IOException e) {
			if (e instanceof java.io.EOFException)
				e.printStackTrace();
			return FAILED;
		} finally {
			monitor.exitRead(); // finished reading
		}
	}

	public String toString() {
		return "searching " + pattern.toString(); //$NON-NLS-1$
	}
}
