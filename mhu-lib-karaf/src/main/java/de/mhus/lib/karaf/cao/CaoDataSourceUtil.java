package de.mhus.lib.karaf.cao;

import de.mhus.lib.cao.CaoDataSource;
import de.mhus.lib.karaf.MOsgi;

public class CaoDataSourceUtil {

	public static CaoDataSource lookup(String name) {
		for (CaoDataSource ds : MOsgi.getServices(CaoDataSource.class, null)) {
			if (ds.getName().equals(name)) return ds;
		}
		return null;
	}
}
