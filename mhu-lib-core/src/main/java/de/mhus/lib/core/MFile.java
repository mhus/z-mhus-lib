/*
 * ./core/de/mhu/lib/MFile.java
 *  Copyright (C) 2002-2004 Mike Hummel
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package de.mhus.lib.core;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.errors.MException;

/**
 * 
 * @author hummel
 */
public class MFile {
	
	private static final String DEFAULT_MIME = "plain/text";
	private static ResourceNode mimeConfigCache;
	private static ResourceNode mhuMimeConfigCache;
	private static Log log = Log.getLog(MFile.class);


	/**
	 * Return the Suffix of a file. Its the string after the last dot.
	 */
	public static String getFileSuffix(File _file) {

		return getFileSuffix(_file.getAbsolutePath());
	}

	/**
	 * Return the Suffix of a file. Its the string after the last dot or an empty string.
	 */
	public static String getFileSuffix(String _name) {

		String name = _name;

		if (!MString.isIndex(name, '.')) return "";
		name = MString.afterLastIndex(name, '.');

		return name;

	}

	/**
	 * Returns the name of the file in a path name. Using the OS specific
	 * separator.
	 */
	public static String getFileName(String _path) {

		String name = _path;

		while (MString.isIndex(name, File.separatorChar))
			name = MString.afterIndex(name, File.separatorChar);

		return name;

	}

	/**
	 * return the internal working directory.
	 */
	public static File getWorkingDirectory() {

		return new File(System.getProperty("user.dir"));

	}

	/**
	 * Open and read a file. It returns the content of the file as string.
	 * Be aware of special characters.
	 */
	public static String readFile(File _f) {

		StringBuffer sb = new StringBuffer();
		try {
			InputStream fis = new FileInputStream(_f);
			while (fis.available() != 0)
				sb.append((char) fis.read());
			fis.close();
		} catch (Exception e) {
			log.d(_f, e);
		}

		return sb.toString();

	}

	/**
	 * Open and read a file. It returns the content of the file as string.
	 * Be aware of special characters.
	 */
	public static String readFile(Reader _is) {

		StringBuffer sb = new StringBuffer();
		char[] buffer = new char[1024];
		try {
			while (true) {
				int size = _is.read(buffer);
				if (size < 0)
					return sb.toString();
				if (size > 0) {
					sb.append(buffer, 0, size);
				}
			}
		} catch (EOFException eofe) {
		} catch (Exception e) {
			log.d(e);
		}

		return sb.toString();

	}

	public static String readUTF8(InputStream _is) throws IOException {
		return readUCF(_is,Charset.forName(MString.CHARSET_DEFAULT));
	}
	
	/**
	 * Read a stream. It returns the content of the file as string.
	 * It will not read byte ofter byte. it will read the hole content as
	 * binary and translate it to a string. This will also create UCF
	 * characters. But be aware of big files. It will cost the double lot of memory
	 * for a short time.
	 * 
	 * @param _is
	 * @param charset The charset to be used or null for the default charset
	 * @return
	 * @throws IOException 
	 */
	public static String readUCF(InputStream _is, Charset charset) throws IOException {
		byte[] buffer = readBinary(_is);
		return new String(buffer, charset);
	}
	
	/**
	 * Open and read a stream. It returns the content of the file as string.
	 * Be aware of special characters.
	 */
	public static String readFile(InputStream _is) {

		StringBuffer sb = new StringBuffer();
		try {
			while (true) {
				int ret = _is.read();
				if (ret < 0)
					break;
				sb.append((char) ret);
			}
		} catch (EOFException eofe) {
		} catch (Exception e) {
			log.d(e);
		}

		return sb.toString();

	}

	/**
	 * Open and read a file. It returns the content of the file as byte array.
	 */
	public static byte[] readBinaryFile(File in) throws IOException {
		InputStream fis = new FileInputStream(in);
		int size = (int) in.length();
		byte[] buffer = new byte[size];
		for (int i = 0; i < size; i++)
			buffer[i] = (byte) fis.read();
		fis.close();
		return buffer;
	}

	/**
	 * Open and read a stream. It returns the content of the file as byte array.
	 */
	public static byte[] readBinary(InputStream is) throws IOException {
		return readBinary(is, false);
	}

	/**
	 * Open and read a stream. It returns the content of the file as byte array.
	 * @param is 
	 * @param close 
	 * @return 
	 * @throws IOException 
	 */
	public static byte[] readBinary(InputStream is, boolean close)
			throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
		byte buffer[] = new byte[1024];
		do {
			int j = is.read(buffer, 0, buffer.length);
			if (j >= 0) {
				stream.write(buffer, 0, j);
			} else {
				if ( close ) is.close();
				return stream.toByteArray();

			}
		} while (true);

	}

	/**
	 * Open and read a stream. It put the content of the file into the byte array.
	 */
	public static void readBinary(InputStream is, byte[] buffer, int offset,
			int length) throws IOException {
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
	 */
	public static boolean writeFile(File _f, String _content) {

		try {
			OutputStream fos = new FileOutputStream(_f);
			char[] c = _content.toCharArray();
			for (int i = 0; i < c.length; i++)
				fos.write(c[i]);
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
	 * @return
	 */
	public static boolean writeFile(File _f, byte[] _content) {

		try {
			OutputStream fos = new FileOutputStream(_f);
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
		fos.write(_content, offset, length);
	}

	/**
	 * Copy a file.
	 * 
	 * @param _src
	 * @param _dest
	 * @return
	 */
	public static boolean copyFile(File _src, File _dest) {

		if (_src.isDirectory())
			return false;

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
	 */
	public static void copyFile(InputStream _is, OutputStream _os) {

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
	 */
	public static void copyFile(Reader _is, Writer _os) {

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
	 * @return
	 */
	public static String toFileName(String _name) {
		// FIXME: Use StringBuffer
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
	 * FIXME: Use FileFilter
	 * 
	 * @param _src
	 * @param _dest
	 * @param _filter
	 */
	public static void copyDir(File _src, File _dest, String _filter) {
		if (_filter == null)
			copyDir(_src, _dest, (FileFilter)null);
		else
			copyDir(_src, _dest, MString.split(_filter, ","));
	}


	
	public static void copyDir(File _src, File _dest, final String[] _filter) {
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
	 * @return
	 */
	public static String normalize(String name) {
		if (name.indexOf('\\') >= 0) name = name.replaceAll("\\\\", "_");
		if (name.indexOf('/') >= 0) name = name.replaceAll("/", "_");
		if (name.indexOf('*') >= 0) name = name.replaceAll("\\*", "_");
		if (name.indexOf('?') >= 0) name = name.replaceAll("\\?", "_");
		if (name.indexOf(':') >= 0) name = name.replaceAll(":", "_");
		if (name.indexOf(' ') >= 0) name = name.replaceAll(" ", "_");

		return name;
	}

	/**
	 * Returns the name without path and extension.
	 * 
	 * @param key
	 * @return
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
	 * @param canonicalName
	 * @param string
	 * @return 
	 */
	public static String replaceExtension(String name, String newExtension) {
		if (name == null || newExtension == null) return name;
		return MString.beforeLastIndex(name, '.') + "." + newExtension;
	}
	
	/**
	 * Searching for the mime type in config and as last option have a static list of extensions.
	 * @param extension
	 * @return 
	 */
	public static String getMimeType(String extension) {
		if (extension == null) return null;
		extension = extension.trim().toLowerCase();
		
		String mime = null;
		ResourceNode config = MSingleton.get().getConfigProvider().getConfig(MFile.class, null);
		if (config != null) {
			ResourceNode map = config.getNode("mapping");
			if (map != null)
				try {
					mime = map.getExtracted(extension);
				} catch (MException e) {
				}
		}
		if (mime == null && config != null) {
			try {
				String file = config.getExtracted("file");
				if (file != null) {
					if (mimeConfigCache == null) {
						try {
	// TODO change way to load properties
	//						mimeConfigCache = MConfigFactory.getInstance().createConfigFor(new File(file));
						} catch (Exception e) {
							log.w(file,e);
						}
					}
					if (mimeConfigCache != null) {
						mime = mimeConfigCache.getExtracted(extension);
						if (mime == null)
							mime = mimeConfigCache.getExtracted("default");
					}
				}
			} catch (MException e) {
			}
		}
		
		if (mime == null) {
			if (mhuMimeConfigCache == null) {
				try {
//					mhuMimeConfigCache = ConfigUtil.loadPropertiesForClass(MFile.class);
				} catch (Exception e) {
					
				}
			}
			try {
				if (mhuMimeConfigCache != null) {
					mime = mhuMimeConfigCache.getExtracted(extension);
					if (mime == null)
						mime = mhuMimeConfigCache.getExtracted("default");
				}
			} catch (MException e) {
			}
		}
		
		if (mime == null)
			mime = DEFAULT_MIME;
		
		return mime;
		
	}

}
