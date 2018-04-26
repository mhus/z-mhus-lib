/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.io;

import java.io.File;
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
	/**
	 * If you want be sure reject everything that is not NO. If you
	 * are generous reject everything that is YES. The value UNKNOWN
	 * should not happen the value should bes set as default value before you
	 * check a file.
	 * 
	 */
	@Override
	public SUSPICIOUS checkForSuspicious(File in) throws IOException {
		return new PdfChecker().isSuspicious(in);
	}

}
