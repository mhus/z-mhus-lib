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
package de.mhus.lib.core;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import de.mhus.lib.core.cfg.CfgProperties;
import de.mhus.lib.core.io.FileChecker;
import de.mhus.lib.core.io.PdfFileChecker;
import de.mhus.lib.core.logging.Log;

/**
 * 
 * @author hummel
 */
public class MFile {
	
	private static final String DEFAULT_MIME = "text/plain";
//	private static ResourceNode<?> mimeConfigCache;
	private static Properties mhuMimeConfigCache;
	private static Log log = Log.getLog(MFile.class);
	
	public static final String TYPE_PDF = "pdf";
	
	private static HashMap<String, FileChecker> fileChecker = new HashMap<>();
	static {
		fileChecker.put(TYPE_PDF, new PdfFileChecker());
	}

	/**
	 * Return the Suffix of a file. Its the string after the last dot.
	 * @param _file 
	 * @return the file suffix
	 */
	public static String getFileSuffix(File _file) {
		if (_file == null) return null;
		return getFileSuffix(_file.getAbsolutePath());
	}

	/**
	 * Return the Suffix of a file. Its the string after the last dot or an empty string.
	 * @param name 
	 * @return the file suffix
	 */
	public static String getFileSuffix(String name) {
		if (name == null) return null;
		
		if (!MString.isIndex(name, '.')) return "";
		name = MString.afterLastIndex(name, '.');

		return name;

	}

	/**
	 * Returns the name of the file in a path name. Using the OS specific
	 * separator.
	 * @param path 
	 * @return the file name
	 */
	public static String getFileName(String path) {
		if (path == null) return null;
		
		while (MString.isIndex(path, File.separatorChar))
			path = MString.afterIndex(path, File.separatorChar);

		return path;

	}

	/**
	 * return the internal working directory.
	 * @return current directory
	 */
	public static File getWorkingDirectory() {

		return new File(System.getProperty("user.dir"));

	}

	/**
	 * Open and read a file. It returns the content of the file as string.
	 * @param _f 
	 * @return file content
	 */
	public static String readFile(File _f) {
		return readFile(_f, MString.CHARSET_UTF_8);
	}
	
	/**
	 * Open and read a file. It returns the content of the file as string.
	 * @param _f 
	 * @param encoding 
	 * @return file content
	 */
	public static String readFile(File _f, String encoding) {
		if (_f == null) return null;
		if (encoding == null) encoding = MString.CHARSET_UTF_8;
		
		try {
			FileInputStream fis = new FileInputStream(_f);
			InputStreamReader fr = new InputStreamReader(fis, encoding);
			String ret = readFile(fr);
			fis.close();
			return ret;
		} catch (Exception e) {
			log.d(_f, e);
		}
		return null;
	}

	/**
	 * Open and read a file. It returns the content of the file as string.
	 * @param _is 
	 * @return file content
	 */
	public static String readFile(Reader _is) {
		if (_is == null) return null;
		
		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[1024];
		try {
			while (true) {
				int size = _is.read(buffer);
				if (size < 0)
					return sb.toString();
				if (size > 0) {
					sb.append(buffer, 0, size);
				} else
					MThread.sleep(50);
			}
		} catch (EOFException eofe) {
		} catch (Exception e) {
			log.d(e);
		}

		return sb.toString();

	}

	/**
	 * Open and read a stream. It returns the content of the file as string.
	 * Be aware of special characters.
	 * @param _is 
	 * @return file content
	 */
	public static String readFile(InputStream _is) {
		return readFile(_is, MString.CHARSET_UTF_8);
	}
	
	public static String readFile(InputStream _is, String encoding) {
		if (_is == null) return null;
		if (encoding == null) encoding = MString.CHARSET_UTF_8;
		
		try {
			InputStreamReader fr = new InputStreamReader(_is, encoding);
			String ret = readFile(fr);
			return ret;
		} catch (Exception e) {
			log.d(e);
		}
		return null;
	}

	/**
	 * Open and read a file. It returns the content of the file as byte array.
	 * @param in 
	 * @return file content
	 * @throws IOException 
	 */
	public static byte[] readBinaryFile(File in) throws IOException {
		if (in == null) return null;
		
		InputStream fis = new FileInputStream(in);
		return readBinary(fis, false);
	}

	/**
	 * Open and read a stream. It returns the content of the file as byte array.
	 * @param is 
	 * @return file content
	 * @throws IOException 
	 */
	public static byte[] readBinary(InputStream is) throws IOException {
		return readBinary(is, false);
	}

	/**
	 * Open and read a stream. It returns the content of the file as byte array.
	 * @param is 
	 * @param close 
	 * @return file content
	 * @throws IOException 
	 */
	public static byte[] readBinary(InputStream is, boolean close)
			throws IOException {
		if (is == null) return null;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		try {
			while (true) {
				int size = is.read(buffer);
				if (size < 0 ) break;
				if (size == 0)
					MThread.sleep(50);
				else
					baos.write(buffer, 0, size);
			}
		} catch (EOFException e) {}
		if (close) is.close();
		return baos.toByteArray();
	}

	/**
	 * Open and read a stream. It put the content of the file into the byte array.
	 * @param is 
	 * @param buffer 
	 * @param offset 
	 * @param length 
	 * @throws IOException 
	 */
	public static void readBinary(InputStream is, byte[] buffer, int offset,
			int length) throws IOException {
		if (is == null || buffer == null) return;
		
		do {
			int j = is.read(buffer, offset, length);
			if (j < 0)
				throw new EOFException();
			if (j == 0)
				MThread.sleep(10);
			offset = offset + j;
			length = length - j;
		} while (length > 0);
	}

	/**
	 * Open and write a file. Be aware of special characters.
	 * @param _f 
	 * @param _content 
	 * @return true if successful
	 */
	public static boolean writeFile(File _f, String _content) {
		if (_f == null) return false;
		try {
			OutputStream fos = new FileOutputStream(_f);
			if (_content != null) {
				char[] c = _content.toCharArray();
				for (int i = 0; i < c.length; i++)
					fos.write(c[i]);
			}
			fos.close();
		} catch (Exception e) {
			log.d(_f, e );
			return false;
		}

		return true;
	}

	/**
	 * Open and write the content of the byte array a file.
	 * 
	 * @param _f
	 * @param _content
	 * @return true if successful
	 */
	public static boolean writeFile(File _f, byte[] _content) {
		if (_f == null) return false;
		
		try {
			OutputStream fos = new FileOutputStream(_f);
			if (_content != null)
				writeFile(fos, _content, 0, _content.length);
			fos.close();
		} catch (Exception e) {
			log.d(_f, e );
			return false;
		}

		return true;
	}

	/**
	 * Write the byte array to the stream.
	 * 
	 * @param fos
	 * @param _content
	 * @param offset
	 * @param length
	 * @throws IOException
	 */
	public static void writeFile(OutputStream fos, byte[] _content, int offset,
			int length) throws IOException {
		if (fos == null || _content == null) return;
		fos.write(_content, offset, length);
	}

	/**
	 * Copy a file.
	 * 
	 * @param _src
	 * @param _dest
	 * @return true if successful
	 */
	public static boolean copyFile(File _src, File _dest) {
		if (_src == null || _dest == null) return false;
		
		if (_src.isDirectory())
			return false;

		if (_dest.isDirectory())
			_dest = new File(_dest, _src.getName());
		
		if (_dest.equals(_src)) return false;
		
		try {
			InputStream fis = new FileInputStream(_src);
			OutputStream fos = new FileOutputStream(_dest) ;
			copyFile(fis, fos);
			fis.close();
			fos.close();
		} catch (Exception e) {
			log.d(_src,_dest, e );
			return false;
		}
		return true;

	}

	/**
	 * Copy a stream.
	 * @param _is 
	 * @param _os 
	 */
	public static void copyFile(InputStream _is, OutputStream _os) {
		if (_is == null || _os == null) return;
		
		long free = Runtime.getRuntime().freeMemory();
		if (free < 1024)
			free = 1024;
		if (free > 32768)
			free = 32768;

		byte[] buffer = new byte[(int) free];
		int i = 0;

		try {
			while ((i = _is.read(buffer)) != -1)
				_os.write(buffer, 0, i);
		} catch (Exception e) {
			log.d( e );
		}
	}

	/**
	 * Copy a stream.
	 * @param _is 
	 * @param _os 
	 */
	public static void copyFile(Reader _is, Writer _os) {
		if (_is == null || _os == null) return;
		
		long free = Runtime.getRuntime().freeMemory();
		if (free < 1024)
			free = 1024;
		if (free > 32768)
			free = 32768;

		char[] buffer = new char[(int) free/2];
		int i = 0;

		try {
			while ((i = _is.read(buffer)) != -1)
				_os.write(buffer, 0, i);
		} catch (Exception e) {
			log.d( e );
		}
	}
	
	/**
	 * Normalize the filename, removes all special characters.
	 * 
	 * @param _name
	 * @return useful file name
	 */
	public static String toFileName(String _name) {
		if (_name == null) return null;
		String out = _name.replace('\\', '_');
		out = out.replace('/', '_');
		out = out.replace('*', '_');
		out = out.replace('?', '_');
		out = out.replace('+', '_');
		out = out.replace('#', '_');
		out = out.replace('\'', '_');
		out = out.replace('"', '_');
		out = out.replace('$', '_');
		out = out.replace('%', '_');
		out = out.replace('?', '_');
		out = out.replace(':', '_');
		out = out.replace('<', '_');
		out = out.replace('>', '_');
		out = out.replace('{', '_');
		out = out.replace('}', '_');

		return out;
	}

	/**
	 * Remove a directory and all included content.
	 * 
	 * @param _dir
	 */
	public static void deleteDir(File _dir) {
		if (_dir == null) return;
		
		if (!_dir.isDirectory()) {
			_dir.delete();
			return;
		}

		File[] list = _dir.listFiles();

		for (int i = 0; i < list.length; i++)
			deleteDir(list[i]);

		_dir.delete();

	}

	/**
	 * Copy a hole directory to another location.
	 * 
	 * @param _src
	 * @param _dest
	 */
	public static void copyDir(File _src, File _dest) {
		copyDir(_src, _dest, (String) null);
	}

	/**
	 * Copy a directory with content. Use the filter to filter the content.
	 * 
	 * @param _src
	 * @param _dest
	 * @param _filter
	 */
	public static void copyDir(File _src, File _dest, String _filter) {
		if (_src == null || _dest == null) return;
		
		if (_filter == null)
			copyDir(_src, _dest, (FileFilter)null);
		else
			copyDir(_src, _dest, MString.split(_filter, ","));
	}


	
	public static void copyDir(File _src, File _dest, final String[] _filter) {
		if (_src == null || _dest == null) return;

		if (_filter == null) 
			copyDir(_src, _dest, (FileFilter)null);
		else
			copyDir(_src, _dest, new FileFilter() {

				@Override
				public boolean accept(File arg0) {
					for (int i = 0; i < _filter.length; i++) {
						if (_filter[i] != null && _filter[i].equals(arg0.getName()))
							return false;
					}
					return true;
				}
				
			});
	}
	public static void copyDir(File _src, File _dest, FileFilter _filter) {
		if (_src == null || _dest == null) return;

		if (_filter != null) {
			if (!_filter.accept(_src))
				return;
		}

		if (!_src.isDirectory()) {
			copyFile(_src, _dest);
			return;
		}

		try {
			_dest.mkdirs();
		} catch (Exception e) {
		}

		File[] list = _src.listFiles();

		for (int i = 0; i < list.length; i++)
			copyDir(list[i], new File(_dest, list[i].getName()), _filter);

	}

	/**
	 * Return a name free from problematic characters like slash, they will be changed to underscore
	 * 
	 * @param name
	 * @return useful file name
	 */
	public static String normalize(String name) {
		if (name == null) return null;
		
		if (name.indexOf('\\') >= 0) name = name.replaceAll("\\\\", "_");
		if (name.indexOf('/') >= 0) name = name.replaceAll("/", "_");
		if (name.indexOf('*') >= 0) name = name.replaceAll("\\*", "_");
		if (name.indexOf('?') >= 0) name = name.replaceAll("\\?", "_");
		if (name.indexOf(':') >= 0) name = name.replaceAll(":", "_");
		if (name.indexOf(' ') >= 0) name = name.replaceAll(" ", "_");
		if (name.indexOf("..") >= 0) name = name.replaceAll("..", "_");
		if (name.indexOf('~') >= 0) name = name.replace('~', '_');

		return name;
	}

	/**
	 *  Return a name free from problematic characters like back slash, they will be changed to underscore.
	 *  The slash as separator between folders is allowed.
	 * @param name 
	 * @return useful path
	 */
	public static String normalizePath(String name) {
		if (name == null) return null;
		
		if (name.indexOf('\\') >= 0) name = name.replaceAll("\\\\", "/");
		if (name.indexOf('*') >= 0) name = name.replaceAll("\\*", "_");
		if (name.indexOf('?') >= 0) name = name.replaceAll("\\?", "_");
		if (name.indexOf(':') >= 0) name = name.replaceAll(":", "_");
		if (name.indexOf(' ') >= 0) name = name.replaceAll(" ", "_");
		if (name.indexOf("..") >= 0) name = name.replaceAll("..", "_");
		if (name.indexOf('~') >= 0) name = name.replace('~', '_');

		return name;
	}
	
	/**
	 * Returns the name without path and extension.
	 * 
	 * @param key
	 * @return file name
	 */
	public static String getFileNameOnly(String key) {
		if (key == null) return null;
		if (key.indexOf('/') >= 0) key = MString.afterLastIndex(key, '/');
		if (key.indexOf('\\') >= 0) key = MString.afterLastIndex(key, '\\');
		if (key.length() > 1 && key.indexOf('.',1) >= 0) key = MString.beforeLastIndex(key, '.');
		return key;
	}

	/**
	 * Replace the Extension of the file
	 * @param name 
	 * @param newExtension 
	 * @return the new file name
	 */
	public static String replaceExtension(String name, String newExtension) {
		if (name == null || newExtension == null) return name;
		return MString.beforeLastIndex(name, '.') + "." + newExtension;
	}
	
	private static CfgProperties mimeProperties = new CfgProperties(MFile.class, "mime");
	
	/**
	 * Searching for the mime type in config and as last option have a static list of extensions.
	 * @param extension full file name or only extension
	 * @return the mime type
	 */
	public static String getMimeType(String extension) {
		if (extension == null) return null;
		extension = extension.trim().toLowerCase();
		if (MString.isIndex(extension, '.')) extension = MString.afterLastIndex(extension, '.');
		
		String mime = null;
		try {
			if (mimeProperties.value() != null)
				mime = mimeProperties.value().getString(extension, null);
		} catch (Throwable t) {}
		
		if (mime == null) {
			if (mhuMimeConfigCache == null) {
				try {
					mhuMimeConfigCache = new Properties();
					MSystem.loadProperties(MFile.class, mhuMimeConfigCache, "mime-types.properties");
				} catch (Exception e) {
					
				}
			}
			if (mhuMimeConfigCache != null) {
				mime = mhuMimeConfigCache.getProperty(extension, null);
				if (mime == null)
					mime = mhuMimeConfigCache.getProperty("default", null);
			}
		}
		
		if (mime == null)
			mime = DEFAULT_MIME;
		
		return mime;
		
	}

	/**
	 * Write a line list to a file. Be aware of the ENTER \\n character in the lines !
	 * Every line will be truncated by a ENTER \\n sign. This means the last line is empty.
	 * 
	 * @param file
	 * @param lines
	 * @param append
	 * @throws IOException
	 */
	public static void writeLines(File file, List<String> lines, boolean append) throws IOException {
		if (file == null || lines == null) return;
		FileWriter w = new FileWriter(file,append);
		writeLines(w,lines);
		w.close();
	}
	
	private static void writeLines(Writer w, List<String> lines) throws IOException {
		for (String line : lines) {
			w.write(line);
			w.write('\n');
		}
	}

	/**
	 * Read a file into a list line by line
	 * 
	 * @param file
	 * @param removeLastEmpty If you have written line by line the last ENTER will produce an empty line, set true to remove this line.
	 * @return the file content as list of lines
	 * @throws IOException
	 */
	public static List<String> readLines(File file, boolean removeLastEmpty) throws IOException {
		if (file == null) return null;
		
		final LinkedList<String> out = new LinkedList<>();
		readLines(file, new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				out.add((String)arg);
			}
		});
		
		if (removeLastEmpty && out.size() > 0 && MString.isEmpty(out.getLast()))
			out.removeLast();
		
		return out;
	}
	
	public static void readLines(File file, Observer lineObserver) throws IOException {
		if (file == null || lineObserver == null) return;
		FileReader r = new FileReader(file);
		readLines(r,lineObserver);
		r.close();
	}

	public static void readLines(Reader r, Observer lineObserver) throws IOException {
		if (r == null || lineObserver == null) return;
		BufferedReader br = new BufferedReader(r);
		String line = null;
		do {
			line = br.readLine();
			if (line != null)
				lineObserver.update(null, line);
		} while(line != null);
	}

	public static File getFile(File parent, String path) {
		if (MString.isEmpty(path)) return null; 
		path = path.trim();
		if (path.startsWith("/")) return new File(path);
		if (path.startsWith("~")) return new File(getUserHome(), path);
		if (parent.isFile()) parent = parent.getParentFile();
		return new File(parent, path);
	}

	public static File getUserHome() {
		return new File(System.getProperty("user.home"));
	}

	public static void releaseLock(FileLock lock) {
		try {
			FileChannel channel = lock.channel();
			lock.close();
			channel.close();
		} catch (IOException e) {
			log.d(e);
		}
	}

	public static FileLock aquireLock(File lockFile, long timeout) throws IOException, TimeoutException {
		if (!lockFile.exists()) lockFile.createNewFile();
		@SuppressWarnings("resource")
		FileChannel channel = new RandomAccessFile(lockFile, "rw").getChannel();
		FileLock lock = null;
		long start = System.currentTimeMillis();
		while (true) {
			try {
				lock = channel.lock();
				return lock;
			} catch (OverlappingFileLockException e) {
				MThread.sleep(200);
			}
			if (System.currentTimeMillis() - start > timeout) throw new TimeoutException(lockFile.getAbsolutePath());
		}
	}

	public static String replaceSuffix(String name, String newSuffix) {
		if (name == null) return null;
		int pos = name.lastIndexOf('.');
		if (pos >= 0)
			name = name.substring(0, pos);
		return name + '.' + newSuffix;
	}

	public static FileChecker getFileCheck(String type) {
		return fileChecker.get(type);
	}
	
}
