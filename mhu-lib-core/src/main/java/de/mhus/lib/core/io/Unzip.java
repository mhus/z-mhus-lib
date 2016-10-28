package de.mhus.lib.core.io;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.lang.MObject;

public class Unzip extends MObject {
		
  public void unzip(File src,File dst,FileFilter filter) throws ZipException, IOException {
    Enumeration<?> entries;
    ZipFile zipFile;
      zipFile = new ZipFile(src);

      entries = zipFile.entries();

      while(entries.hasMoreElements()) {
        ZipEntry entry = (ZipEntry)entries.nextElement();

        if(entry.isDirectory()) {
          // Assume directories are stored parents first then children.
          // System.err.println("Extracting directory: " + entry.getName());
	      log().t("Unzip directory: " + entry.getName());
          // This is not robust, just for demonstration purposes.
          (new File(dst,entry.getName())).mkdir();
          continue;
        }

        File dstFile = new File(dst,entry.getName());
        if (filter == null || !filter.accept(dstFile)) {
	        log().t("Unzip file: " + entry.getName());
	        MFile.copyFile(zipFile.getInputStream(entry),
	           new BufferedOutputStream(new FileOutputStream(dstFile)));
        }
      }

      zipFile.close();
  }

}
