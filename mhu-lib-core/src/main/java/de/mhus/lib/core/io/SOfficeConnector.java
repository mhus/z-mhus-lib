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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.core.parser.StringPropertyReplacer;
import de.mhus.lib.errors.NotFoundException;

public class SOfficeConnector {

	public static final String SOFFICE_CONTENT = "content.xml";
	public static final String WORD_CONTENT = "word/document.xml";
	private static CfgString BINARY = new CfgString(SOfficeConnector.class, "binary", "soffice");
	private String binary = BINARY.value();
	private boolean valid = false;
	private String version;
	
	public SOfficeConnector() {
		findVersion();
	}	
	
	private void findVersion() {
		valid = false;
		version = null;
		if (binary == null || binary.indexOf("soffice") < 0) return;
		try {
			version = MSystem.execute(binary,"--version").getOutput();
			valid = MString.isSet(version) && version.startsWith("LibreOffice ");
		} catch (IOException e) {
			if (MSystem.isUnix()) {
				// retry with unix default location
				File f = new File("/usr/bin/soffice");
				if (f.exists() && f.canExecute()) {
					binary = f.getPath();
					try {
						version = MSystem.execute(binary,"--version").getOutput();
						valid = MString.isSet(version) && version.startsWith("LibreOffice ");
					} catch (IOException e2) {
					}
				}
			}
		}
		
	}

	public String getBinary() {
		return binary;
	}

	public void setBinary(String binary) {
		this.binary = binary;
		findVersion();
	}

	public boolean isValid() {
		return valid;
	}

	public String getVersion() {
		return version;
	}

	public String convertToPdf(String in, String outDir) throws NotFoundException, IOException {
		return convertTo("pdf", in, outDir);
	}
	
	/**
	 * Convert from in file format to 'format'.
	 * 
	 * @param format The resulting format and filter, see https://ask.libreoffice.org/en/question/2641/convert-to-command-line-parameter/
	 * @param in Input file
	 * @param outDir output directory or null for the same location as the input file
	 * @return Path to the generated file
	 * 
	 * @throws NotFoundException
	 * @throws IOException
	 */
	public String convertTo(String format, String in, String outDir) throws NotFoundException, IOException {
		if (!valid)
			throw new NotFoundException("LibreOffice not found");
		
		File inFile = new File(in);
		if (!inFile.exists() || !inFile.isFile())
			throw new FileNotFoundException(in);
		
		if (outDir != null) {
			File outFile = new File(outDir);
			if (outFile.exists() && !outFile.isDirectory())
				throw new IOException("out directory is not a directory: " + outDir);

			if (!outFile.exists())
				if (!outFile.mkdirs())
					throw new IOException("can't create out directory: " + outDir);
		}
		
		String[] res = null;
		if (outDir == null)
			res = MSystem.execute(binary,"--headless","-convert-to",format,in).toArray();
		else
			res = MSystem.execute(binary,"--headless","-convert-to",format,"-outdir",outDir,in).toArray();
			
		for (String line : res[0].split("\n")) {
			line = line.trim();
			if (line.startsWith("convert ")) {
				int p1 = line.indexOf(" -> ");
				int p2 = line.indexOf(" using", p1);
				if (p1 > 0 && p2 > 0)
					return line.substring(p1+4, p2);
			}
		}
		return null;
	}
	
	/*
	 * This is for testing purposes ... output should be like ...
	 * 
	 *  LibreOffice 6.0.5.2 54c8cbb85f300ac59db32fe8a675ff7683cd5a16
	 *  /private/tmp/Devices.pdf
	 * 
	 */
	public static void main(String args[]) throws NotFoundException, IOException {
		SOfficeConnector inst = new SOfficeConnector();
		inst.setBinary("/Users/mikehummel/dev/LibreOffice.app/Contents/MacOS/soffice");
		System.out.println(inst.getVersion());
		String to = inst.convertToPdf("/Users/mikehummel/Devices.ods", "/tmp");
		System.out.println(to);
	}
	
	@Override
	public String toString() {
		return version;
	}
	
	public static void replace(File from, File to, StringPropertyReplacer replacer) throws Exception {
		replace(from, to, new StringPropertyRewriter(replacer));
	}
	
	public static void replace(File from, File to, StreamRewriter replacer) throws Exception {
		ZipFile inZip = new ZipFile(from);
		FileOutputStream os = new FileOutputStream(to);
		ZipOutputStream outZip = new ZipOutputStream(os);
		
		// copy
		Enumeration<? extends ZipEntry> entries = inZip.entries();
		while (entries.hasMoreElements()) {
			ZipEntry inNext = entries.nextElement();
			InputStream isZip = inZip.getInputStream(inNext);
			ZipEntry e = new ZipEntry(inNext.getName());
			outZip.putNextEntry(e);
			if (inNext.getName().equals(SOFFICE_CONTENT) || inNext.getName().equals(WORD_CONTENT)) {
				InputStream isRewritten = replacer.rewriteContent(inNext.getName(), isZip);
				MFile.copyFile(isRewritten, outZip);
			} else {
				MFile.copyFile(isZip, outZip);
			}
			outZip.closeEntry();
			isZip.close();
		}
		outZip.close();
		inZip.close();
	}
	
	public static String content(File from) throws ZipException, IOException {
		ZipFile inZip = new ZipFile(from);
		try {
			// copy
			Enumeration<? extends ZipEntry> entries = inZip.entries();
			while (entries.hasMoreElements()) {
				ZipEntry inNext = entries.nextElement();
				InputStream isZip = inZip.getInputStream(inNext);
				if (inNext.getName().equals(SOFFICE_CONTENT) || inNext.getName().equals(WORD_CONTENT)) {
					String content = MFile.readFile(isZip);
					isZip.close();
					return content;
				}
				isZip.close();
			}
		} finally {
			inZip.close();
		}
		return null;
	}
	
}
