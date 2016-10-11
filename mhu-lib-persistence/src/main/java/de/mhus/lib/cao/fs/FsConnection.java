package de.mhus.lib.cao.fs;

import java.io.File;
import java.util.WeakHashMap;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoMetaDefinition.TYPE;
import de.mhus.lib.cao.CaoMetadata;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.CaoPolicyProvider;
import de.mhus.lib.cao.util.MutableMetadata;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;

public class FsConnection extends CaoConnection {

	public static final String MODIFIED = "modified";
	private FsNode root;
	private WeakHashMap<String, FsNode> cache = new WeakHashMap<>();
	private CaoMetadata metadata;
	private CaoPolicyProvider policyProvider;
	
	public FsConnection(File root) {
		this(new FsDriver(), root);
	}
	
	public FsConnection(FsDriver driver, File root) {
		super(driver);
		
		metadata = new MutableMetadata(driver)
				.addDefinition(MODIFIED, TYPE.LONG, 0);
		
		policyProvider = new FsPolicyProvider();
		
		this.root = new FsNode(this,root);
		
	}
	
	@Override
	public CaoNode getRoot() {
		return root;
	}

	@Override
	public CaoNode getResource(String path) {
		FsNode node = cache.get(path);
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
		
		cache.put(path, node);
		
		return node;
	}

	public void fillProperties(File file, MProperties p) {
		p.setLong(MODIFIED, file.lastModified());
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

}
