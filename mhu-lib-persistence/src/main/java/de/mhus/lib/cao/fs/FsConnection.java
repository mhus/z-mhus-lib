package de.mhus.lib.cao.fs;

import java.io.File;
import java.util.WeakHashMap;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoMetaDefinition.TYPE;
import de.mhus.lib.cao.CaoMetadata;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.CaoPolicyProvider;
import de.mhus.lib.cao.util.MutableMetadata;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;

public class FsConnection extends CaoConnection {

	public static final String MODIFIED = "modified";
	private FsNode root;
	private WeakHashMap<String, FsNode> cache = new WeakHashMap<>();
	private CaoMetadata metadata;
	private CaoPolicyProvider policyProvider;
	private boolean useMetaFile;
	
	public FsConnection(String name, String root, boolean useMetaFile, boolean useCache) {
		this(name, new File(root), useMetaFile, useCache);
	}
	
	public FsConnection(String name, File root, boolean useMetaFile, boolean useCache) {
		this(name, new FsDriver(), root);
		this.useMetaFile = useMetaFile;
		if (!useCache)
			cache = null;
		if (this.root != null) this.root.reload();
	}
	
	public FsConnection(String name, FsDriver driver, File root) {
		super(name, driver);
		
		metadata = new MutableMetadata(driver)
				.addDefinition(MODIFIED, TYPE.LONG, 0);
		
		policyProvider = new FsPolicyProvider();
		
		this.root = new FsNode(this,root, null);
		
	}
	
	@Override
	public CaoNode getRoot() {
		return root;
	}

	@Override
	public CaoNode getResourceByPath(String path) {
		synchronized (this) {
			FsNode node = cache == null ? null : cache.get(path);
			if (node != null) {
				if (node.isValid()) return node;
				cache.remove(path);
			}
			
			node = root;
			for (String part : path.split("/")) {
				if (MString.isSet(part)) {
					node = (FsNode) node.getNode(part);
					if (node == null) return null;
				}
			}
			
			if (cache != null) cache.put(path, node);
			
			return node;
		}
	}

	public void fillProperties(File file, MProperties p) {
		
		if (useMetaFile) {
			File metaFile = getMetaFileFor(file);
			
			if (metaFile.exists() && metaFile.isFile()) {
				MProperties meta = MProperties.load(metaFile);
				p.putAll(meta);
			}
		}
		
		p.setLong(MODIFIED, file.lastModified());
	}
	
	public File getMetaFileFor(File file) {
		File metaFile = null;
		if (file.isDirectory()) 
			metaFile = new File(file, "__cao.meta");
		else
			metaFile = new File(file.getParentFile(), "__cao." + file.getName() + ".meta");
		return metaFile;
	}

	public CaoMetadata getMetadata() {
		return metadata;
	}

	public CaoPolicyProvider getPolicyProvider() {
		return policyProvider;
	}

	public void setPolicyProvider(CaoPolicyProvider policyProvider) {
		this.policyProvider = policyProvider;
	}

	@Override
	public CaoNode getResourceById(String id) {
		return getResourceByPath(id);
	}

	public boolean isUseMetaFile() {
		return useMetaFile;
	}

	public File getContentFileFor(File file, String rendition) {
		if (file.isFile()) {
			if (rendition != null) return null;
			return file;
		}
		if (useMetaFile) {
			if (rendition == null) rendition = "content";
			rendition = MFile.normalize(rendition);
			return new File(file, "__cao." + rendition);
		}
		
		return null;
	}

}
