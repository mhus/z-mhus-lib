package de.mhus.lib.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class AbstractFileChecker implements FileChecker {

	@Override
	public boolean isFileType(File in) throws IOException {
		FileInputStream is = new FileInputStream(in);
		boolean out = isFileType(is);
		is.close();
		return out;
	}

}
