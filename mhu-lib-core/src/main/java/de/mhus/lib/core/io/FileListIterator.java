package de.mhus.lib.core.io;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Iterator;

import de.mhus.lib.core.util.FlatteningIterator;

/**
 * Iterates over all non-directory files contained in some subdirectory of the
 * current one.
 * 
 * @author david
 */
public class FileListIterator implements Iterator<File>, Iterable<File> {
	private final FlatteningIterator<File> flatteningIterator;

	@Override
	public void remove() {
	}

	public FileListIterator(File file, FileFilter filter) {
		this.flatteningIterator = new FlatteningIterator<File>(new FileIterator(file,
				filter));
	}

	public FileListIterator(File file) {
		this(file, null);
	}

	@Override
	public boolean hasNext() {
		return flatteningIterator.hasNext();
	}

	@Override
	public File next() {
		return flatteningIterator.next();
	}

	/**
	 * Iterator to iterate over all the files contained in a directory. It
	 * returns a File object for non directories or a new FileIterator obejct
	 * for directories.
	 */
	private static class FileIterator implements Iterator<Object> {
		private final Iterator<File> files;
		private final FileFilter filter;

		FileIterator(File file, FileFilter filter) {
			this.files = Arrays.asList(file.listFiles(filter)).iterator();
			this.filter = filter;
		}

		@Override
		public void remove() {
		}

		@Override
		public Object next() {
			File next = this.files.next();

			if (next.isDirectory())
				return new FileIterator(next, this.filter);
			else
				return next;
		}

		@Override
		public boolean hasNext() {
			return this.files.hasNext();
		}
	}

	@Override
	public Iterator<File> iterator() {
		return this;
	}
}