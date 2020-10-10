/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import de.mhus.lib.core.cfg.CfgProperties;
import de.mhus.lib.core.io.FileChecker;
import de.mhus.lib.core.io.PdfFileChecker;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.util.IObserver;
import de.mhus.lib.core.util.MUri;

/** @author hummel */
public class MFile {

    public static final String DEFAULT_MIME = "text/plain";
    //	private static ResourceNode<?> mimeConfigCache;
    private static Properties mhuMimeConfigCache;
    private static Log log = Log.getLog(MFile.class);

    public static final String TYPE_PDF = "pdf";
    private static final int MAX_LEVELS = 100;

    private static HashMap<String, FileChecker> fileChecker = new HashMap<>();

    static {
        fileChecker.put(TYPE_PDF, new PdfFileChecker());
    }

    /**
     * Set unix like permissions to a file. Only possible in unix like systems. Check
     * MSystem.isWindows
     *
     * @param file The file
     * @param permissionsStr The posix string "rwxrwxrwx"
     * @return true if the action was successful
     */
    public static boolean setUnixPermissions(File file, String permissionsStr) {
        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString(permissionsStr);
        try {
            Files.setPosixFilePermissions(file.toPath(), permissions);
        } catch (IOException e) {
            log.d(file, permissionsStr, e);
            return false;
        }
        return true;
    }

    /**
     * Return a posix permission string. Only possible in unix like systems. Check MSystem.isWindows
     *
     * @param file The file
     * @return The posix string like "rwxrwxrwx" or "rw-r--r--" or null if not possible
     */
    public static String getUnixPermissions(File file) {
        try {
            Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(file.toPath());
            return PosixFilePermissions.toString(permissions);
        } catch (IOException e) {
            log.d(file, e);
        }
        return null;
    }

    /**
     * Return the Suffix of a file. Its the string after the last dot. It's lower case and trimmed!
     *
     * @param _file
     * @return the file suffix
     */
    public static String getFileExtension(File _file) {
        if (_file == null) return null;
        return getFileExtension(_file.getAbsolutePath());
    }

    /**
     * Return the Suffix of a file. Its the string after the last dot or an empty string. It's lower
     * case and trimmed!
     *
     * @param name
     * @return the file suffix
     */
    public static String getFileExtension(String name) {
        if (name == null) return null;

        if (!MString.isIndex(name, '.')) return "";
        name = MString.afterLastIndex(name, '.');

        return name.trim().toLowerCase();
    }

    /**
     * Returns the name of the file in a path name. Using the OS specific separator.
     * '/dir/subdir/file.ext' will return 'file.ext'. This function use the system separator slash
     * or backslash for windows. If you always have a slash as directory separator use
     * MUri.getFileName()
     *
     * @param path
     * @return the file name
     */
    public static String getFileName(String path) {
        if (path == null) return null;

        if (MString.isIndex(path, File.separatorChar))
            path = MString.afterLastIndex(path, File.separatorChar);

        return path;
    }

    /**
     * Returns the directory without file name or current directory. '/dir/subdir/file' will return
     * '/dir/subdir'. This function use the system separator slash or backslash for windows. If you
     * always have a slash as directory separator use MUri.getFileDirectory()
     *
     * @param path
     * @return The previous directory name or null
     */
    public static String getFileDirectory(String path) {
        if (path == null) return null;

        if (MString.isIndex(path, File.separatorChar)) {
            String ret = MString.beforeLastIndex(path, File.separatorChar);
            while (ret.endsWith("" + File.separatorChar)) ret = ret.substring(0, ret.length() - 1);
            if (ret.length() == 0) return null;
            return ret;
        }
        return null;
    }

    /**
     * return the internal working directory.
     *
     * @return current directory
     */
    public static File getWorkingDirectory() {

        return new File(System.getProperty("user.dir"));
    }

    /**
     * Open and read a file. It returns the content of the file as string.
     *
     * @param _f
     * @return file content
     */
    public static String readFile(File _f) {
        return readFile(_f, MString.CHARSET_UTF_8);
    }

    /**
     * Open and read a file. It returns the content of the file as string.
     *
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
     *
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
                if (size < 0) return sb.toString();
                if (size > 0) {
                    sb.append(buffer, 0, size);
                } else MThread.sleep(50);
            }
        } catch (EOFException eofe) {
        } catch (Exception e) {
            log.d(e);
        }

        return sb.toString();
    }

    /**
     * Open and read a stream. It returns the content of the file as string. Be aware of special
     * characters.
     *
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
     *
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
     *
     * @param is
     * @return file content
     * @throws IOException
     */
    public static byte[] readBinary(InputStream is) throws IOException {
        return readBinary(is, false);
    }

    /**
     * Open and read a stream. It returns the content of the file as byte array.
     *
     * @param is
     * @param close
     * @return file content
     * @throws IOException
     */
    public static byte[] readBinary(InputStream is, boolean close) throws IOException {
        if (is == null) return null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            while (true) {
                int size = is.read(buffer);
                if (size < 0) break;
                if (size == 0) MThread.sleep(50);
                else baos.write(buffer, 0, size);
            }
        } catch (EOFException e) {
        }
        if (close) is.close();
        return baos.toByteArray();
    }

    /**
     * Open and read a stream. It put the content of the file into the byte array.
     *
     * @param is
     * @param buffer
     * @param offset
     * @param length
     * @throws IOException
     */
    public static void readBinary(InputStream is, byte[] buffer, int offset, int length)
            throws IOException {
        if (is == null || buffer == null) return;

        do {
            int j = is.read(buffer, offset, length);
            if (j < 0) throw new EOFException();
            if (j == 0) MThread.sleep(10);
            offset = offset + j;
            length = length - j;
        } while (length > 0);
    }

    /**
     * Open and read a stream. It put the content of the file into the byte array.
     *
     * @param is
     * @param buffer
     * @param offset
     * @param length
     * @throws IOException
     */
    public static void readBinary(ObjectInput is, byte[] buffer, int offset, int length)
            throws IOException {
        if (is == null || buffer == null) return;

        do {
            int j = is.read(buffer, offset, length);
            if (j < 0) throw new EOFException();
            if (j == 0) MThread.sleep(10);
            offset = offset + j;
            length = length - j;
        } while (length > 0);
    }

    /**
     * Open and write a file. Be aware of special characters.
     *
     * @param _f
     * @param _content
     * @return true if successful
     */
    public static boolean writeFile(File _f, String _content) {
        if (_f == null) return false;
        try {
            OutputStream fos = new FileOutputStream(_f);
            OutputStreamWriter osw = new OutputStreamWriter(fos, MString.CHARSET_UTF_8);
            osw.write(_content);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            log.d(_f, e);
            return false;
        }

        return true;
    }

    public static boolean writeFile(OutputStream os, String content) {
        return writeFile(os, content, MString.CHARSET_UTF_8);
    }

    public static boolean writeFile(OutputStream os, String _content, String charsetName) {
        try {
            if (_content != null) {
                OutputStreamWriter osw = new OutputStreamWriter(os, charsetName);
                osw.write(_content);
                osw.flush();
            }
        } catch (Exception e) {
            log.d(e);
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
            if (_content != null) writeFile(fos, _content, 0, _content.length);
            fos.close();
        } catch (Exception e) {
            log.d(_f, e);
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
    public static void writeFile(OutputStream fos, byte[] _content, int offset, int length)
            throws IOException {
        if (fos == null || _content == null) return;
        fos.write(_content, offset, length);
    }

    /**
     * Copy a file. Return -1 if src or dest is null, -2 if src is a directory, -3 if src and dest
     * is the same file, -4 on error.
     *
     * @param _src
     * @param _dest
     * @return lesser zero for an error or the copied size
     */
    public static long copyFile(File _src, File _dest) {
        if (_src == null || _dest == null) return -1;

        if (_src.isDirectory()) return -2;

        if (_dest.isDirectory()) _dest = new File(_dest, _src.getName());

        if (_dest.equals(_src)) return -3;

        long size = 0;
        try {
            InputStream fis = new FileInputStream(_src);
            OutputStream fos = new FileOutputStream(_dest);
            size = copyFile(fis, fos);
            fis.close();
            fos.close();
        } catch (Exception e) {
            log.d(_src, _dest, e);
            return -4;
        }
        return size;
    }

    /**
     * Copy a stream.
     *
     * @param _is
     * @param _os
     * @return The copied size or -1 if _is or _os is null
     */
    public static long copyFile(InputStream _is, OutputStream _os) {
        if (_is == null || _os == null) return -1;

        long size = 0;
        long free = Runtime.getRuntime().freeMemory();
        if (free < 1024) free = 1024;
        if (free > 32768) free = 32768;

        byte[] buffer = new byte[(int) free];
        int i = 0;

        try {
            while ((i = _is.read(buffer)) != -1) {
                if (i == 0) MThread.sleep(100);
                else {
                    _os.write(buffer, 0, i);
                    size += i;
                }
            }
        } catch (Exception e) {
            log.d(e);
        }
        return size;
    }

    /**
     * Copy a stream.
     *
     * @param _is
     * @param _os
     * @return Return the copied size of bytes or -1.
     */
    public static long copyFile(Reader _is, Writer _os) {
        if (_is == null || _os == null) return -1;

        long size = 0;
        long free = Runtime.getRuntime().freeMemory();
        if (free < 1024) free = 1024;
        if (free > 32768) free = 32768;

        char[] buffer = new char[(int) free / 2];
        int i = 0;

        try {
            while ((i = _is.read(buffer)) != -1)
                if (i == 0) MThread.sleep(100);
                else {
                    _os.write(buffer, 0, i);
                    size += i;
                }
        } catch (Exception e) {
            log.d(e);
        }
        return size;
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

        for (int i = 0; i < list.length; i++) deleteDir(list[i]);

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

        if (_filter == null) copyDir(_src, _dest, (FileFilter) null);
        else copyDir(_src, _dest, MString.split(_filter, ","));
    }

    public static void copyDir(File _src, File _dest, final String[] _filter) {
        if (_src == null || _dest == null) return;

        if (_filter == null) copyDir(_src, _dest, (FileFilter) null);
        else
            copyDir(
                    _src,
                    _dest,
                    new FileFilter() {

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
            if (!_filter.accept(_src)) return;
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

        if (name.indexOf('\\') >= 0) name = name.replace("\\\\", "_");
        if (name.indexOf('/') >= 0) name = name.replace("/", "_");
        if (name.indexOf('*') >= 0) name = name.replace("\\*", "_");
        if (name.indexOf('?') >= 0) name = name.replace("\\?", "_");
        if (name.indexOf(':') >= 0) name = name.replace(":", "_");
        if (name.indexOf(' ') >= 0) name = name.replace(" ", "_");
        if (name.indexOf("..") >= 0) name = name.replace("..", "_");
        if (name.indexOf('~') >= 0) name = name.replace('~', '_');

        return name;
    }

    /**
     * Return a name free from problematic characters like back slash, they will be changed to
     * underscore. The slash as separator between folders is allowed.
     *
     * @param name
     * @return useful path
     */
    public static String normalizePath(String name) {
        if (name == null) return null;

        if (name.indexOf('\\') >= 0) name = name.replace("\\\\", "/");
        if (name.indexOf('*') >= 0) name = name.replace("\\*", "_");
        if (name.indexOf('?') >= 0) name = name.replace("\\?", "_");
        if (name.indexOf(':') >= 0) name = name.replace(":", "_");
        if (name.indexOf(' ') >= 0) name = name.replace(" ", "_");
        if (name.indexOf("..") >= 0) name = name.replace("..", "_");
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
        if (key.length() > 1 && key.indexOf('.', 1) >= 0) key = MString.beforeLastIndex(key, '.');
        return key;
    }

    /**
     * Replace the Extension of the file
     *
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
     *
     * @param extension full file name or only extension
     * @return the mime type
     */
    public static String getMimeType(String extension) {
        loadMimeTypes();
        return getMimeType(
                extension,
                mhuMimeConfigCache != null
                        ? mhuMimeConfigCache.getProperty("default", DEFAULT_MIME)
                        : DEFAULT_MIME);
    }

    /**
     * Searching for the mime type in config and as last option have a static list of extensions.
     *
     * @param extension full file name or only extension
     * @param def
     * @return the mime type
     */
    public static String getMimeType(String extension, String def) {
        if (extension == null) return null;
        extension = extension.trim().toLowerCase();
        if (MString.isIndex(extension, '.')) extension = MString.afterLastIndex(extension, '.');

        String mime = null;
        try {
            if (mimeProperties.value() != null)
                mime = mimeProperties.value().getString(extension, null);
        } catch (Throwable t) {
        }

        if (mime == null) {
            loadMimeTypes();
            if (mhuMimeConfigCache != null) {
                mime = mhuMimeConfigCache.getProperty(extension, null);
            }
        }

        if (mime == null) mime = def;

        return mime;
    }

    public static synchronized void loadMimeTypes() {
        if (mhuMimeConfigCache == null) {
            try {
                mhuMimeConfigCache = new Properties();
                MSystem.loadProperties(MFile.class, mhuMimeConfigCache, "mime-types.properties");
            } catch (Exception e) {
                MLogUtil.log().t(e);
            }
        }
    }

    /**
     * Write a line list to a file. Every line will be URL encoded
     *
     * @param file
     * @param lines
     * @param append
     * @throws IOException
     */
    public static void writeLinesEncoded(File file, List<String> lines, boolean append)
            throws IOException {
        if (file == null || lines == null) return;
        FileWriter w = new FileWriter(file, append);
        writeLinesEncoded(w, lines);
        w.close();
    }

    public static void writeLinesEncoded(Writer w, List<String> lines) throws IOException {
        for (String line : lines) {
            w.write(MUri.encode(line));
            w.write('\n');
        }
    }

    /**
     * Read a file into a list line by line
     *
     * @param file
     * @param removeLastEmpty If you have written line by line the last ENTER will produce an empty
     *     line, set true to remove this line.
     * @return the file content as list of lines
     * @throws IOException
     */
    public static List<String> readLinesEncoded(File file, boolean removeLastEmpty)
            throws IOException {
        if (file == null) return null;

        final LinkedList<String> out = new LinkedList<>();
        readLines(
                file,
                new IObserver<String>() {

                    @Override
                    public void update(Object o, Object reason, String arg) {
                        out.add(MUri.decode((String) arg));
                    }
                });

        if (removeLastEmpty && out.size() > 0 && MString.isEmpty(out.getLast())) out.removeLast();

        return out;
    }

    /**
     * Write a line list to a file. Be aware of the ENTER \\n character in the lines ! Every line
     * will be truncated by a ENTER \\n sign. This means the last line is empty.
     *
     * @param file
     * @param lines
     * @param append
     * @throws IOException
     */
    public static void writeLines(File file, List<String> lines, boolean append)
            throws IOException {
        if (file == null || lines == null) return;
        FileWriter w = new FileWriter(file, append);
        writeLines(w, lines);
        w.close();
    }

    public static void writeLines(Writer w, List<String> lines) throws IOException {
        for (String line : lines) {
            w.write(line);
            w.write('\n');
        }
    }

    /**
     * Read a file into a list line by line
     *
     * @param file
     * @param removeLastEmpty If you have written line by line the last ENTER will produce an empty
     *     line, set true to remove this line.
     * @return the file content as list of lines
     * @throws IOException
     */
    public static List<String> readLines(File file, boolean removeLastEmpty) throws IOException {
        if (file == null) return null;

        final LinkedList<String> out = new LinkedList<>();
        readLines(
                file,
                new IObserver<String>() {

                    @Override
                    public void update(Object o, Object reason, String arg) {
                        out.add((String) arg);
                    }
                });

        if (removeLastEmpty && out.size() > 0 && MString.isEmpty(out.getLast())) out.removeLast();

        return out;
    }

    /**
     * Read an stream into a list line by line. The stream will not be closed.
     *
     * @param is
     * @param removeLastEmpty If you have written line by line the last ENTER will produce an empty
     *     line, set true to remove this line.
     * @return the file content as list of lines
     * @throws IOException
     */
    public static List<String> readLines(InputStream is, boolean removeLastEmpty)
            throws IOException {
        if (is == null) return null;

        final LinkedList<String> out = new LinkedList<>();
        readLines(
                is,
                new IObserver<String>() {

                    @Override
                    public void update(Object o, Object reason, String arg) {
                        out.add((String) arg);
                    }
                });

        if (removeLastEmpty && out.size() > 0 && MString.isEmpty(out.getLast())) out.removeLast();

        return out;
    }

    public static void readLines(File file, IObserver<String> lineObserver) throws IOException {
        if (file == null || lineObserver == null) return;
        FileReader r = new FileReader(file);
        readLines(r, lineObserver);
        r.close();
    }

    /**
     * The stream will not be closed.
     *
     * @param is
     * @param lineObserver
     * @throws IOException
     */
    public static void readLines(InputStream is, IObserver<String> lineObserver)
            throws IOException {
        if (is == null || lineObserver == null) return;
        InputStreamReader r =
                new InputStreamReader(
                        is, MString.CHARSET_CHARSET_UTF_8); // default charset is UTF-8
        readLines(r, lineObserver);
    }

    public static void readLines(Reader r, IObserver<String> lineObserver) throws IOException {
        if (r == null || lineObserver == null) return;
        BufferedReader br = new BufferedReader(r);
        String line = null;
        do {
            line = br.readLine();
            if (line != null) lineObserver.update(null, null, line);
        } while (line != null);
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

    public static FileLock aquireLock(File lockFile, long timeout)
            throws IOException, TimeoutException {
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
            if (System.currentTimeMillis() - start > timeout)
                throw new TimeoutException(lockFile.getAbsolutePath());
        }
    }

    public static String replaceSuffix(String name, String newSuffix) {
        if (name == null) return null;
        int pos = name.lastIndexOf('.');
        if (pos >= 0) name = name.substring(0, pos);
        return name + '.' + newSuffix;
    }

    public static FileChecker getFileCheck(String type) {
        return fileChecker.get(type);
    }

    public static Reader openFileReader(File file, String encoding)
            throws FileNotFoundException, UnsupportedEncodingException {
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader fr = new InputStreamReader(fis, encoding);
        return fr;
    }

    public static List<File> findAllFiles(File root, FileFilter fileFilter) {
        LinkedList<File> out = new LinkedList<>();
        findAllFiles(root, fileFilter, out, MAX_LEVELS);
        return out;
    }

    public static void findAllFiles(
            File root, FileFilter fileFilter, List<File> list, int maxLevels) {
        maxLevels--;
        if (maxLevels < 0) return;
        for (File child : root.listFiles()) {
            if (child.isDirectory()) {
                if (child.getName().equals(".") || child.getName().equals("..")) {
                    // ignore the dotties
                } else {
                    if (fileFilter.accept(child)) findAllFiles(child, fileFilter, list, maxLevels);
                }
            } else {
                if (fileFilter.accept(child)) list.add(child);
            }
        }
    }

    public static File[] filter(File dir, String pattern) {
        return dir.listFiles(
                new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return MString.compareFsLikePattern(name, pattern);
                    }
                });
    }

    /**
     * Touch a file
     *
     * @param file
     * @return true if touch was successful
     */
    public static boolean touch(File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.close();
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    public static void mkParentDir(File f) {
        if (f == null) return;
        File dir = f.getParentFile();
        if (!dir.exists())
            dir.mkdirs();
    }
}
