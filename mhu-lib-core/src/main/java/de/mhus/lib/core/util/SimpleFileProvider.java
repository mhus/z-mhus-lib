package de.mhus.lib.core.util;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class SimpleFileProvider implements FileProvider {

	private File root;
	
	public SimpleFileProvider(File root) {
		this.root = root;
	}
	
	@Override
	public File getFile(String path) {
		return new File(root, path);
	}

	@Override
	public Set<String> getContent(String path) {
		File file = getFile(path);
		HashSet<String> out = new HashSet<>();
		for (File sub : file.listFiles()) {
			try {
				if (sub.isDirectory()) {
					out.add(path + sub.getName() + "/");
				} else {
					out.add(path + sub.getName());
				}
			} catch (Throwable t) {}
		}
		return out;
	}

}
