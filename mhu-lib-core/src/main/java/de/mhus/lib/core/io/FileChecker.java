package de.mhus.lib.core.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileChecker {

	boolean isFileType(File in) throws IOException;
	
	boolean isFileType(InputStream in) throws IOException;
	
}
