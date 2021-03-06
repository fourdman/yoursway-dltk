package org.eclipse.dltk.tcl.internal.core.packages;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.dltk.tcl.internal.core.packages.DLTKTclHelper.TclPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class persistently holds all required information for specified
 * interpreter package to path associations.
 */
public class PackagesManager {
	private static final String DEPENDENCY_TAG = "dependency";
	private static final String INTERPRETER_TAG = "interpreter";
	private static final String VALUE_ATTR = "value";
	private static final String PACKAGES_FILE = "packages.txt";
	private static final String PACKAGES_TAG = "packages";
	private static final String PACKAGE_TAG = "package";

	private static final String INTERPRETER_ATTR = INTERPRETER_TAG;
	private static final String NAME_ATTR = "name";
	private static final String PATH_TAG = "path";

	private static PackagesManager manager;

	/**
	 * Contains association of PackageKey to PackageInformation
	 */
	private Map packages = new HashMap();

	/**
	 * Contains set of interpreter to list of packages association.
	 */
	private Map interpreterToPackages = new HashMap();

	public static PackagesManager getInstance() {
		if (manager == null) {
			manager = new PackagesManager();
		}
		return manager;
	}

	private PackagesManager() {
		initialize();
	}

	private static class PackageKey {
		private String packageName;
		private String interpreterPath;

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((interpreterPath == null) ? 0 : interpreterPath
							.hashCode());
			result = prime * result
					+ ((packageName == null) ? 0 : packageName.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PackageKey other = (PackageKey) obj;
			if (interpreterPath == null) {
				if (other.interpreterPath != null)
					return false;
			} else if (!interpreterPath.equals(other.interpreterPath))
				return false;
			if (packageName == null) {
				if (other.packageName != null)
					return false;
			} else if (!packageName.equals(other.packageName))
				return false;
			return true;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public String getInterpreterPath() {
			return interpreterPath;
		}

		public void setInterpreterPath(String interpreterPath) {
			this.interpreterPath = interpreterPath;
		}
	}

	public static class PackageInformation {
		private Set paths = new HashSet();
		private Set dependencies = new HashSet();

		public Set getPaths() {
			return paths;
		}

		public void setPaths(Set paths) {
			this.paths = paths;
		}

		public Set getDependencies() {
			return dependencies;
		}

		public void setDependencies(Set dependencies) {
			this.dependencies = dependencies;
		}
	}

	private void initialize() {
		IPath packages = TclPlugin.getDefault().getStateLocation().append(
				PACKAGES_FILE);
		File packagesFile = packages.toFile();
		if (packagesFile.exists()) {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
				Document document = builder.parse(new BufferedInputStream(
						new FileInputStream(packagesFile), 2048));
				populate(document.getDocumentElement());
			} catch (ParserConfigurationException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			} catch (SAXException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
	}

	private void save() {
		IPath packages = TclPlugin.getDefault().getStateLocation().append(
				PACKAGES_FILE);
		File packagesFile = packages.toFile();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			save(document);

			FileOutputStream fos = new FileOutputStream(packagesFile, false);
			BufferedOutputStream bos = new BufferedOutputStream(fos, 2048);

			TransformerFactory serFactory = TransformerFactory.newInstance();
			Transformer transformer = serFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$

			DOMSource source = new DOMSource(document);
			StreamResult outputTarget = new StreamResult(bos);
			transformer.transform(source, outputTarget);
			bos.close();
			fos.close();

		} catch (ParserConfigurationException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		} catch (TransformerException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void save(Document doc) {
		Element packages = doc.createElement(PACKAGES_TAG); //$NON-NLS-1$
		doc.appendChild(packages);
		for (Iterator iterator = this.packages.keySet().iterator(); iterator
				.hasNext();) {
			PackageKey key = (PackageKey) iterator.next();

			Element packageElement = doc.createElement(PACKAGE_TAG);
			packageElement.setAttribute(NAME_ATTR, key.getPackageName());
			packageElement.setAttribute(INTERPRETER_ATTR, key
					.getInterpreterPath());

			PackageInformation info = (PackageInformation) this.packages
					.get(key);
			Set paths = info.getPaths();
			for (Iterator iterator2 = paths.iterator(); iterator2.hasNext();) {
				IPath path = (IPath) iterator2.next();
				Element pathElement = doc.createElement(PATH_TAG);
				pathElement.setAttribute(VALUE_ATTR, path.toOSString());
				packageElement.appendChild(pathElement);
			}
			Set deps = info.getDependencies();
			for (Iterator iterator2 = deps.iterator(); iterator2.hasNext();) {
				String pkgName = (String) iterator2.next();
				Element pkgElement = doc.createElement(DEPENDENCY_TAG);
				pkgElement.setAttribute(NAME_ATTR, pkgName);
				packageElement.appendChild(pkgElement);
			}
			packages.appendChild(packageElement);
		}
		for (Iterator iterator = this.interpreterToPackages.keySet().iterator(); iterator
				.hasNext();) {
			String interpreter = (String) iterator.next();

			Element interpreterElement = doc.createElement(INTERPRETER_TAG);
			interpreterElement.setAttribute(NAME_ATTR, interpreter);
			Set pkgs = (Set) this.interpreterToPackages.get(interpreter);
			for (Iterator iterator2 = pkgs.iterator(); iterator2.hasNext();) {
				String pkgName = (String) iterator2.next();
				Element pathElement = doc.createElement(PACKAGE_TAG);
				pathElement.setAttribute(VALUE_ATTR, pkgName);
				interpreterElement.appendChild(pathElement);
			}
			packages.appendChild(interpreterElement);
		}
	}

	private synchronized void populate(Element documentElement) {
		NodeList childNodes = documentElement.getChildNodes();
		int length = childNodes.getLength();
		for (int i = 0; i < length; i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equalsIgnoreCase(PACKAGE_TAG)) {
					Element e = (Element) child;
					String packageName = e.getAttribute(NAME_ATTR);
					String interpreter = e.getAttribute(INTERPRETER_ATTR);
					PackageInformation packageInfo = new PackageInformation();
					NodeList childrens = e.getChildNodes();
					for (int j = 0; j < childrens.getLength(); j++) {
						Node path = childrens.item(j);
						if (path.getNodeType() == Node.ELEMENT_NODE) {
							if (path.getNodeName().equalsIgnoreCase(PATH_TAG)) {
								String pathValue = ((Element) path)
										.getAttribute(VALUE_ATTR);
								packageInfo.getPaths().add(new Path(pathValue));
							} else if (path.getNodeName().equalsIgnoreCase(
									DEPENDENCY_TAG)) {
								String pkgName = ((Element) path)
										.getAttribute(NAME_ATTR);
								packageInfo.getDependencies().add(pkgName);
							}
						}
					}
					this.packages.put(makeKey(packageName, interpreter),
							packageInfo);
				} else if (child.getNodeName()
						.equalsIgnoreCase(INTERPRETER_TAG)) {
					Element e = (Element) child;
					String interpreter = e.getAttribute(NAME_ATTR);
					NodeList paths = e.getChildNodes();
					Set packages = new HashSet();
					for (int j = 0; j < paths.getLength(); j++) {
						Node packageNode = paths.item(j);
						if (packageNode.getNodeType() == Node.ELEMENT_NODE) {
							if (packageNode.getNodeName().equalsIgnoreCase(
									PACKAGE_TAG)) {
								String packageNameValue = ((Element) packageNode)
										.getAttribute(VALUE_ATTR);
								packages.add(packageNameValue);
							}
						}
					}
					this.interpreterToPackages.put(interpreter, packages);
				}
			}
		}
	}

	private PackageKey makeKey(String packageName, String interpreter) {
		PackageKey key = new PackageKey();
		key.setPackageName(packageName);
		key.setInterpreterPath(interpreter);
		return key;
	}

	/**
	 * Return paths specific only for selected package.
	 */
	public synchronized IPath[] getPathsForPackage(IInterpreterInstall install,
			String packageName) {
		PackageKey key = makeKey(packageName, install.getInstallLocation()
				.getAbsolutePath());
		if (this.packages.containsKey(key)) {
			PackageInformation info = (PackageInformation) this.packages
					.get(key);
			Set els = info.getPaths();
			return (IPath[]) els.toArray(new IPath[els.size()]);
		}
		// Retrieve paths from interpreter with all dependencies.
		TclPackage[] srcs = DLTKTclHelper.getSrcs(install.getInstallLocation(),
				install.getEnvironmentVariables(), packageName);

		PackageInformation resultInfo = null;
		for (int i = 0; i < srcs.length; i++) {
			Set paths2 = srcs[i].getPaths();
			PackageKey okey = makeKey(srcs[i].getName(), install
					.getInstallLocation().getAbsolutePath());
			PackageInformation info;
			if (this.packages.containsKey(okey)) {
				info = (PackageInformation) this.packages.get(okey);
			} else {
				info = new PackageInformation();
			}
			info.getPaths().addAll(paths2);
			info.getDependencies().addAll(srcs[i].getDependencies());
			this.packages.put(okey, info);
			if (okey.equals(key)) {
				resultInfo = info;
			}
		}
		// Follow all dependencies
		if (resultInfo == null) {
			this.packages.put(key, new PackageInformation());
			return new IPath[0];
		}

		Set resultPaths = new HashSet();
		resultPaths.addAll(resultInfo.getPaths());

		save();
		return (IPath[]) resultPaths.toArray(new IPath[resultPaths.size()]);
	}

	public synchronized Map getDependencies(String pkgName,
			IInterpreterInstall install) {
		Set checkedPackages = new HashSet();
		Map packages = new HashMap();
		PackageKey key = makeKey(pkgName, install);
		PackageInformation info = (PackageInformation) this.packages.get(key);
		if (info != null) {
			traverseDependencies(packages, checkedPackages, info, install);
		}
		return packages;
	}

	private PackageKey makeKey(String pkgName, IInterpreterInstall install) {
		return makeKey(pkgName, install.getInstallLocation().getAbsolutePath());
	}

	private synchronized void traverseDependencies(Map packages,
			Set checkedPackages, PackageInformation resultInfo,
			IInterpreterInstall install) {
		Set dependencies = resultInfo.getDependencies();
		for (Iterator iterator = dependencies.iterator(); iterator.hasNext();) {
			String pkgName = (String) iterator.next();
			if (!checkedPackages.contains(pkgName)) {
				checkedPackages.add(pkgName);
				PackageKey pkgKey = makeKey(pkgName, install
						.getInstallLocation().getAbsolutePath());
				if (this.packages.containsKey(pkgKey)) {
					PackageInformation depInfo = (PackageInformation) this.packages
							.get(pkgKey);
					packages.put(pkgName, depInfo);
					traverseDependencies(packages, checkedPackages, depInfo,
							install);
				}
			}
		}
	}

	public synchronized Set getPackageNames(IInterpreterInstall install) {
		String key = install.getInstallLocation().getAbsolutePath();
		if (this.interpreterToPackages.containsKey(key)) {
			Set set = (Set) this.interpreterToPackages.get(key);
			return set;
		}
		// Evaluate
		Set packages = DLTKTclHelper.getPackages(install);
		this.interpreterToPackages.put(key, packages);
		save();
		return packages;
	}

	public synchronized Set getInternalPackageNames(
			IInterpreterInstall install, IScriptProject project) {
		String key = "internal|||" + project.getElementName() + "|||"
				+ install.getInstallLocation().getAbsolutePath();
		if (this.interpreterToPackages.containsKey(key)) {
			Set set = (Set) this.interpreterToPackages.get(key);
			return set;
		}
		return new HashSet();
	}

	public synchronized void setInternalPackageNames(
			IInterpreterInstall install, Set names) {
		String key = "internal|||"
				+ install.getInstallLocation().getAbsolutePath();
		Set values = new HashSet();
		values.addAll(names);
		this.interpreterToPackages.put(key, values);
		save();
	}

	/**
	 * Return all packages paths in one call.
	 */
	public synchronized IPath[] getPathsForPackages(
			IInterpreterInstall install, Set packagesInBuild) {

		StringBuffer buf = new StringBuffer();
		String[] pkgs = (String[]) packagesInBuild
				.toArray(new String[packagesInBuild.size()]);
		for (int i = 0; i < pkgs.length; i++) {
			buf.append(pkgs[i]).append(" ");
		}
		PackageKey key = makeKey(buf.toString(), install.getInstallLocation()
				.getAbsolutePath());

		if (this.packages.containsKey(key)) {
			PackageInformation info = (PackageInformation) this.packages
					.get(key);
			Set paths = info.getPaths();
			return (IPath[]) paths.toArray(new IPath[paths.size()]);
		}
		// Retrieve paths from interpreter with all dependencies.
		TclPackage[] srcs = DLTKTclHelper.getSrcs(install.getInstallLocation(),
				install.getEnvironmentVariables(), buf.toString());
		Set result = new HashSet();
		for (int i = 0; i < srcs.length; i++) {
			Set paths2 = srcs[i].getPaths();
			PackageKey okey = makeKey(srcs[i].getName(), install
					.getInstallLocation().getAbsolutePath());
			PackageInformation info = null;
			if (this.packages.containsKey(okey)) {
				info = (PackageInformation) this.packages.get(okey);
			} else {
				info = new PackageInformation();
			}
			result.addAll(paths2);
			info.getPaths().addAll(paths2);
			info.getDependencies().addAll(srcs[i].getDependencies());
			this.packages.put(okey, info);
		}

		PackageInformation info = new PackageInformation();
		info.getPaths().addAll(result);
		this.packages.put(key, info);

		for (int i = 0; i < pkgs.length; i++) {
			PackageKey lkey = makeKey(pkgs[i], install.getInstallLocation()
					.getAbsolutePath());
			if (!this.packages.containsKey(lkey)) {
				this.packages.put(lkey, new PackageInformation());
			}
		}
		save();

		return (IPath[]) result.toArray(new IPath[result.size()]);
	}

	public IPath[] getAllPaths(String pkgName, IInterpreterInstall install) {
		Set result = new HashSet();
		IPath[] paths = this.getPathsForPackage(install, pkgName);
		result.addAll(Arrays.asList(paths));
		Map dependencies = this.getDependencies(pkgName, install);
		for (Iterator iterator = dependencies.keySet().iterator(); iterator
				.hasNext();) {
			String packageName = (String) iterator.next();
			PackageInformation info = (PackageInformation) dependencies
					.get(packageName);
			result.addAll(info.getPaths());
		}
		return (IPath[]) result.toArray(new IPath[result.size()]);
	}

	/**
	 * This method removes all information about specified interpreter.
	 * 
	 * @param install
	 */
	public synchronized void removeInterprterInfo(IInterpreterInstall install) {
		// Remove interpreter to packages set
		String interpreterPath = install.getInstallLocation().getAbsolutePath();
		this.interpreterToPackages.remove(interpreterPath);
		// Remove all values stored for interpreter packages
		for (Iterator iterator = this.packages.keySet().iterator(); iterator
				.hasNext();) {
			PackageKey key = (PackageKey) iterator.next();
			String path = key.getInterpreterPath();
			if (path.equals(interpreterPath)) {
				iterator.remove();
			}
		}
		save();
	}

	public IPath[] getPathsForPackageWithDeps(IInterpreterInstall install,
			String name) {
		Set result = new HashSet();
		IPath[] paths = this.getPathsForPackage(install, name);
		result.addAll(Arrays.asList(paths));

		Map dependencies = manager.getDependencies(name, install);
		for (Iterator iterator = dependencies.keySet().iterator(); iterator
				.hasNext();) {
			String pkgName = (String) iterator.next();
			result.addAll(Arrays.asList(getPathsForPackage(install, pkgName)));
		}
		return (IPath[]) result.toArray(new IPath[result.size()]);
	}
}
