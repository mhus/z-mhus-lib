package de.mhus.lib.core.directory;

import java.io.InputStream;
import java.net.URL;

import de.mhus.lib.core.MString;
import de.mhus.lib.errors.MException;

/**
 * <p>ClassLoaderResourceProvider class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ClassLoaderResourceProvider extends MResourceProvider<ResourceNode> {

	private ClassLoader loader;
	
	/**
	 * <p>Constructor for ClassLoaderResourceProvider.</p>
	 */
	public ClassLoaderResourceProvider() {
		//this(Thread.currentThread().getContextClassLoader());
		this(ClassLoaderResourceProvider.class.getClassLoader());
	}
	
	/**
	 * <p>Constructor for ClassLoaderResourceProvider.</p>
	 *
	 * @param loader a {@link java.lang.ClassLoader} object.
	 */
	public ClassLoaderResourceProvider(ClassLoader loader) {
		this.loader = loader;
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode getResource(String name) {
		return new CLResourceNode(loader,name);
	}

	/**
	 * <p>getClassLoader.</p>
	 *
	 * @return a {@link java.lang.ClassLoader} object.
	 */
	public ClassLoader getClassLoader() {
		return loader;
	}

	/**
	 * <p>setClassLoader.</p>
	 *
	 * @param loader a {@link java.lang.ClassLoader} object.
	 */
	public void setClassLoader(ClassLoader loader) {
		this.loader = loader;
	}

	private static class CLResourceNode extends ResourceNode {

		private String name;
		private ClassLoader loader;

		public CLResourceNode(ClassLoader loader, String name) {
			this.loader = loader;
			this.name = name;
		}

		@Override
		public String[] getPropertyKeys() {
			return new String[0];
		}

		@Override
		public ResourceNode getNode(String key) {
			return null;
		}

		@Override
		public ResourceNode[] getNodes() {
			return new ResourceNode[0];
		}

		@Override
		public ResourceNode[] getNodes(String key) {
			return null;
		}

		@Override
		public String[] getNodeKeys() {
			return new String[0];
		}

		@Override
		public String getName() throws MException {
			return MString.afterLastIndex(name, '/');
		}

		@Override
		public InputStream getInputStream(String key) {
			if (key == null)
				return loader.getResourceAsStream(name);
			return null;
		}

		@Override
		public ResourceNode getParent() {
			return null;
		}

		@Override
		public Object getProperty(String name) {
			return null;
		}

		@Override
		public boolean isProperty(String name) {
			return false;
		}

		@Override
		public void removeProperty(String key) {
		}

		@Override
		public void setProperty(String key, Object value) {
		}

		@Override
		public boolean isEditable() {
			return false;
		}

		@Override
		public URL getUrl() {
			return loader.getResource(name);
		}

		@Override
		public boolean isValide() {
			return true;
		}

		@Override
		public boolean hasContent() {
			return true;
		}
		
	}
}
