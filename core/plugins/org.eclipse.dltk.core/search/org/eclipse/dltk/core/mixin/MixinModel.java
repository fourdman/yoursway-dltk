package org.eclipse.dltk.core.mixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.core.ElementChangedEvent;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.IDLTKProject;
import org.eclipse.dltk.core.IElementChangedListener;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelElementDelta;
import org.eclipse.dltk.core.IParent;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.mixin.IMixinRequestor.ElementInfo;
import org.eclipse.dltk.internal.core.ModelCache;
import org.eclipse.dltk.internal.core.mixin.MixinCache;
import org.eclipse.dltk.internal.core.mixin.MixinManager;

public class MixinModel {
	private MixinCache cache = null;
	/**
	 * Contains list of mixin elements.
	 */
	private Map elementToMixinCache = new HashMap();
	private IDLTKLanguageToolkit toolkit = null;
	
	private MixinRequestor mixinRequestor = new MixinRequestor();
	
	private ISourceModule currentModule;

	public MixinModel(IDLTKLanguageToolkit toolkit) {
		this.toolkit = toolkit;

		// long maxMemory = Runtime.getRuntime().freeMemory();
		double ratio = 1000;

		this.cache = new MixinCache(
				(int) (ModelCache.DEFAULT_ROOT_SIZE * ratio));
		DLTKCore.addElementChangedListener(changedListener);
	}

	public void stop() {
		DLTKCore.removeElementChangedListener(changedListener);
	}

	public IMixinElement get(String key) {
		MixinElement element = getCreateEmpty(key);
		if (DLTKCore.VERBOSE) {
			System.out.println("Filling ratio:" + this.cache.fillingRatio());
			this.cache.printStats();
		}
		buildElementTree(element);
		if( element.isFinal() && element.sourceModules.size() > 0 ) {
			return element;
		}
		this.cache.remove(element);
		cache.resetSpaceLimit(ModelCache.DEFAULT_ROOT_SIZE, element);
		return null;
	}

	private void buildElementTree(MixinElement element) {
		if (element.isFinal()) {
			return;
		}
		ISourceModule[] containedModules = findModules(element);
		if( containedModules.length == 0 ) {
			cache.remove(element);
			cache.resetSpaceLimit(ModelCache.DEFAULT_ROOT_SIZE, element);
			return;
		}
		for (int i = 0; i < containedModules.length; ++i) {
			if (!this.elementToMixinCache.containsKey(containedModules[i])) {
				this.elementToMixinCache.put(containedModules[i], new ArrayList());
				reportModule(containedModules[i]);
			}
		}
		// mark selected element and all subelements as finished.
		markElementAsFinal(element);
	}

	private void markElementAsFinal(MixinElement element) {
		element.bFinal = true;
		for( int i = 0; i < element.children.size(); ++i ) {
			markElementAsFinal((MixinElement)element.children.get(i));
		}
	}

	private void reportModule(ISourceModule sourceModule) {
		IMixinParser mixinParser;
		try {
			mixinParser = MixinManager.getMixinParser(sourceModule);
			if (mixinParser != null) {
				this.currentModule = sourceModule;
				char[] content = sourceModule.getSource().toCharArray();
				mixinParser.setRequirestor(mixinRequestor);
				mixinParser.parserSourceModule(content, true, sourceModule );
				this.currentModule = null;
			}
		} catch (CoreException e) {
			e.printStackTrace();
			return;
		}
	}

	private void processChildren(IModelElement element, List els) {
		if (element.getElementType() == IModelElement.SOURCE_MODULE) {
			els.add(element);
		} else if (element instanceof IParent) {
			IParent parent = (IParent) element;
			IModelElement[] children = null;
			try {
				children = parent.getChildren();
			} catch (ModelException e) {
				return;
			}
			for (int i = 0; i < children.length; ++i) {
				processChildren(children[i], els);
			}
		}
	}

	/**
	 * Should find all elements source modules to be shure we build complete child tree.
	 * @param element
	 * @return
	 */
	private ISourceModule[] findModules(MixinElement element) {
		// lets for test add all possible modules from workspace.
		List modules = new ArrayList();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		for (int i = 0; i < projects.length; ++i) {
			IDLTKProject prj = DLTKCore.create(projects[i]);
			IDLTKLanguageToolkit languageToolkit = null;
			try {
				languageToolkit = DLTKLanguageManager.getLanguageToolkit(prj);
			} catch (CoreException e) {
				e.printStackTrace();
				continue;
			}
			if (languageToolkit != null && languageToolkit.getNatureID().equals(this.toolkit.getNatureID())) {
				processChildren(prj, modules);
			}
		}
		return (ISourceModule[]) modules.toArray(new ISourceModule[modules
				.size()]);
	}

	private MixinElement getCreateEmpty(String key) {
		MixinElement element = (MixinElement)MixinModel.this.cache.get(key);
		if( element == null ) {
			element = new MixinElement(key, currentModule );
			this.cache.put(key, element);
			this.cache.ensureSpaceLimit(1, element);
		}
		return element;
	}

	private IElementChangedListener changedListener = new IElementChangedListener() {
		public void elementChanged(ElementChangedEvent event) {
			IModelElementDelta delta = event.getDelta();
			processDelta(delta);
		}

		private void processDelta(IModelElementDelta delta) {
			IModelElement element = delta.getElement();
			if (delta.getKind() == IModelElementDelta.REMOVED
					|| (delta.getFlags() & IModelElementDelta.F_REMOVED_FROM_BUILDPATH) != 0
					|| (delta.getFlags() & IModelElementDelta.CHANGED) != 0) {
				if (element.getElementType() == IModelElement.SOURCE_MODULE) {
					MixinModel.this.remove((ISourceModule) element);
				}
			}
			if ((delta.getFlags() & IModelElementDelta.F_CHILDREN) != 0) {
				IModelElementDelta[] affectedChildren = delta
						.getAffectedChildren();
				for (int i = 0; i < affectedChildren.length; i++) {
					IModelElementDelta child = affectedChildren[i];
					processDelta(child);
				}
			}
		}
	};

	public void remove(ISourceModule element) {
		// cache.remove(element);
		// cache.resetSpaceLimit(ModelCache.DEFAULT_ROOT_SIZE, element);
	}

	private class MixinElement implements IMixinElement {
		private final static String NONE_KEY = "";
		private String key;
		private boolean bFinal = false;
		private List sourceModules = new ArrayList();
		private Map sourceModuleToObject = new HashMap();

		/**
		 * List of Strings.
		 */
		private List children = new ArrayList();

		public boolean equals(Object obj) {
			if( obj instanceof MixinElement ) {
				return this.key.equals(((MixinElement)obj).key);
			}
			return false;
		}

		public int hashCode() {
			return this.key.hashCode();
		}

		public MixinElement(String key) {
			this.key = key;
		}
		public String toString() {
			return this.getName() + " final[" + this.bFinal + "]" + this.children + " ";
		}

		public MixinElement(ElementInfo info, ISourceModule module) {
			this(info.key, currentModule);
			addInfo(info, module);
		}

		public void addInfo(ElementInfo info, ISourceModule module) {
			if( info.object != null ) {
				Object object = this.sourceModuleToObject.get(module);
				if( object != null ) {
					if( object instanceof List ) {
						((List)object).add(info.object);
					}
					else {
						List l = new ArrayList();
						l.add(object);
						l.add(info.object);
						object = l;
						this.sourceModuleToObject.put(module, object);
					}
				}
				else {
					List l = new ArrayList();
					l.add(info.object);
					object = l;
					this.sourceModuleToObject.put(module, object);
				}
			}
		}

		public MixinElement(String key, ISourceModule currentModule) {
			this.key = key;
			addModule(currentModule);
		}

		public void addModule(ISourceModule currentModule) {
			if( currentModule != null ) {
				if( !this.sourceModules.contains(currentModule)) {
					this.sourceModules.add(currentModule);
				}
			}
		}

		public IMixinElement[] getChildren() {
			List childs = new ArrayList();
			for (int i = 0; i < this.children.size(); ++i) {
				childs.add(MixinModel.this.get((String) this.children.get(i)));
			}
			return (IMixinElement[]) childs.toArray(new IMixinElement[childs
					.size()]);
		}

		public String getKey() {
			return this.key;
		}

		protected String getParentKey() {
			if (this.key.indexOf(IMixinRequestor.MIXIN_NAME_SEPARATOR) == -1) {
				return NONE_KEY;
			}
			return this.key.substring(0, this.key
					.lastIndexOf(IMixinRequestor.MIXIN_NAME_SEPARATOR));
		}
		protected String getName() {
			if (this.key.indexOf(IMixinRequestor.MIXIN_NAME_SEPARATOR) == -1) {
				return this.key;
			}
			return this.key.substring(1+this.key
					.lastIndexOf(IMixinRequestor.MIXIN_NAME_SEPARATOR));
		}

		public IMixinElement getParent() {
			String parentKey = this.getParentKey();
			if (parentKey == null) {
				return null;
			}
			return get(parentKey);

		}

		public ISourceModule[] getSourceModules() {
			if (!this.isFinal()) {
				get(this.key);
			}
			return (ISourceModule[]) this.sourceModules
					.toArray(new ISourceModule[this.sourceModules.size()]);
		}

		public Object getObject(ISourceModule module) {
			return this.sourceModuleToObject.get(module);
		}

		public boolean isFinal() {
			return this.bFinal;
		}
	};
	private class MixinRequestor implements IMixinRequestor {
		public void reportElement(ElementInfo info) {
			if( DLTKCore.VERBOSE_MIXIN ) {
				System.out.println("Append mixin:" + info.key);
			}
			String[] list = info.key.split(IMixinRequestor.MIXIN_NAME_SEPARATOR);
			MixinElement element = getCreateEmpty(info.key);
			addElementToModules(element);
			element.addModule(currentModule);
			element.addInfo(info, currentModule);
			// Append as childs for all other elements. Also append modules to all selected elements.
			if( list.length != 1 ) {
				for( int i = 0 ; i < list.length -1; ++i ) {
					MixinElement parent = getCreateEmpty(element.getParentKey());
					if( !parent.children.contains(element)) {
						parent.children.add(element);
					}
					addElementToModules(parent);
					element = parent;
				}
			}
		}

		private void addElementToModules(MixinElement element) {
			List elements = (List)MixinModel.this.elementToMixinCache.get(currentModule);
			if( !elements.contains(element)) {
				elements.add(element);
			}
		}
	};
}
