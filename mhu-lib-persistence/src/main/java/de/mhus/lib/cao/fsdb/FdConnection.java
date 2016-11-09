package de.mhus.lib.cao.fsdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.TimeoutException;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoConst;
import de.mhus.lib.cao.CaoMetaDefinition.TYPE;
import de.mhus.lib.cao.aspect.CaoPolicyAspectFactory;
import de.mhus.lib.cao.CaoMetadata;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.CaoPolicy;
import de.mhus.lib.cao.util.MutableMetadata;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.errors.MException;

public class FdConnection extends CaoConnection {

	private FdNode root;
	private WeakHashMap<String, FdNode> cache = new WeakHashMap<>();
	private CaoMetadata metadata;
	private File rootDir;
	private File filesDir;
	private File indexDir;
	private File lockFile;
	private int fileSub;
	
	public FdConnection(String name, String root, boolean useCache) throws MException, IOException, TimeoutException {
		this(name, new File(root), useCache);
	}
	
	public FdConnection(String name, File root, boolean useCache) throws MException, IOException, TimeoutException {
		this(name, new FdDriver(), root);
		if (!useCache)
			cache = null;
		if (this.root != null) this.root.reload();
		registerAspectFactory(CaoPolicy.class, new FdPolicyProvider());
		
		actionList.add(new FdCreate());
		actionList.add(new FdDelete());
		actionList.add(new FdUploadRendition());
		actionList.add(new FdDeleteRendition());
		
	}
	
	public FdConnection(String name, FdDriver driver, File root) throws IOException, TimeoutException {
		super(name, driver);
		
		metadata = new MutableMetadata(driver)
				.addDefinition(CaoConst.MODIFIED, TYPE.LONG, 0);
				
		rootDir = root;
		filesDir = new File(root, "repository/files");
		indexDir = new File(root, "repository/index");
		lockFile = new File(root, "sync.lock");
		filesDir.mkdirs();
		indexDir.mkdirs();
		fileSub = filesDir.getAbsolutePath().length();
		
		this.root = new FdNode(this,filesDir, null);
		
		fullIndex();
	}
	
	private void fullIndex() throws IOException, TimeoutException {
		
		FileLock lock = MFile.aquireLock(lockFile, MTimeInterval.MINUTE_IN_MILLISECOUNDS);
		try {
			log().d("Full index", getName());
			indexDir(filesDir);
			
		} finally {
			MFile.releaseLock(lock);
		}
		
	}

	private void indexDir(File dir) {
		for (File f : dir.listFiles()) {
			
			if (f.isFile() && !f.getName().startsWith(".") && !f.getName().startsWith("_")) {
				// auto convert
				log().d(getName(),"Convert file");
				File tmp = new File(dir, "__temp." + f.getName());
				f.renameTo(tmp);
				f.mkdirs();
				tmp.renameTo(new File(f, "__cao.content.content" ));
			}

			if (f.isDirectory() && !f.getName().startsWith(".") && !f.isHidden()) {
				try {
					indexFile(f);
				} catch (Throwable t) {
					log().d("Index",f,t);
				}
				indexDir(f);
			}				
		}
	}

	void indexFile(File f) throws IOException {
		File meta = getMetaFileFor(f);
		MProperties p = MProperties.load(meta);
		if (p == null) p = new MProperties();
		String id = p.getString("_id", null);
		if (id == null) {
			id = UUID.randomUUID().toString();
			p.setString("_id", id);
			p.save(meta);
		}
		rememberIndex(f,id);
	}

	private void rememberIndex(File f, String id) {
		if (id == null) return;
		String c = f.getAbsolutePath().substring(fileSub);
		File iFile = new File(indexDir, idCut(id) );
		String cc = MFile.readFile(iFile);
		if (!c.equals(cc)) {
			iFile.getParentFile().mkdirs();
			MFile.writeFile(iFile, c );
		}
	}

	private String idCut(String id) {
		return id.replace('-', '/');
	}

	private String getPathForId(String id) {
		if (id == null) return null;
		File iFile = new File(indexDir, idCut(id) );
		String c = MFile.readFile(iFile);
		return c;
	}
	
	@Override
	public CaoNode getRoot() {
		return root;
	}

	@Override
	public CaoNode getResourceByPath(String path) {
		if (path == null) return null;
		synchronized (this) {
			FdNode node = cache == null ? null : cache.get(path);
			if (node != null) {
				if (node.isValid()) return node;
				cache.remove(path);
			}
			
			node = root;
			for (String part : path.split("/")) {
				if (MString.isSet(part)) {
					node = (FdNode) node.getNode(part);
					if (node == null) return null;
				}
			}
			
			if (cache != null) cache.put(path, node);
			
			return node;
		}
	}

	public void fillProperties(File file, MProperties p) {
		
			File metaFile = getMetaFileFor(file);
			
			if (metaFile.exists() && metaFile.isFile()) {
				MProperties meta = MProperties.load(metaFile);
				p.putAll(meta);
			}
		
		p.setLong(CaoConst.MODIFIED, file.lastModified());
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

	@Override
	public CaoNode getResourceById(String id) {
		String  path = getPathForId(id);
		return getResourceByPath(path);
//		File f = new File(filesDir, path);
	}

	public File getContentFileFor(File file, String rendition) {
		if (file.isFile()) {
			if (rendition != null) return null;
			return file;
		}

		if (rendition == null) rendition = "content";
		rendition = MFile.normalize(rendition);
		return new File(file, "__cao.content." + rendition);
		
	}

	void deleteIndex(String id) {
		if (id == null) return;
		File iFile = new File(indexDir, idCut(id) );
		if (iFile.exists()) iFile.delete();
	}

}
