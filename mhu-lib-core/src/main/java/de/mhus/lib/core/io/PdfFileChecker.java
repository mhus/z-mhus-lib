package de.mhus.lib.core.io;

import java.io.IOException;
import java.io.InputStream;

import de.mhus.lib.core.MFile;

public class PdfFileChecker extends AbstractFileChecker {

	@Override
	public boolean isFileType(InputStream in) throws IOException {
		
		byte[] b = new byte[5];
		MFile.readBinary(in, b, 0, 5);
		return b[0] == '%' && b[1] == 'P' && b[2] == 'D' && b[3] == 'F' && b[4] == '-';
	}

}
