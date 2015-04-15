package de.mhus.lib.sql;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.mhus.lib.annotations.adb.DbType;
import de.mhus.lib.core.MSql;
import de.mhus.lib.core.directory.ResourceNode;

/**
 * This class can compare a configuration with a database table structure
 * and can modify the database structure without deleting existing tables.
 * 
 * TODO: on request: remove other columns
 * TODO: views, foreign keys
 * TODO: data !!!
 * @author mikehummel
 *
 */
public class DialectMysql extends DialectDefault {

	@Override
	public String normalizeColumnName(String columnName) {
		//		if ("key".equals(columnName))
		//			return "key_";
		//		return columnName;
		return columnName + "_"; // TODO not working at all
	}

	@Override
	public String getDbType(String type, String size) {
		String t = type.toUpperCase();


		if (t.equals(DbType.TYPE.FLOAT.name())) {
			t = "DOUBLE"; // Float is too small - use double
		} else
			if (t.equals(DbType.TYPE.BLOB.name())) {
				t = "LONGBLOB"; // blob is too small - use longblob
			} else
				return super.getDbType(t, size);

		return t;
	}

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String toSqlDateValue(Date date) {
		synchronized (dateFormat) {
			return "'" + dateFormat.format(date) + "'";
		}
	}

	@Override
	protected void createTableLastCheck(ResourceNode ctable, String tn, StringBuffer sql) {
		sql.append(" ENGINE=InnoDb");
	}

	@Override
	public String escape(String text) {
		String ret = MSql.escape(text);
		if (ret == null || text == null) return ret;
		if (ret.indexOf('\\') < 0)
			return ret;
		return ret.replaceAll("\\\\", "\\\\\\\\");
	}

}
