package de.mhus.lib.sql;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.mhus.lib.adb.query.ALimit;
import de.mhus.lib.adb.query.APrint;
import de.mhus.lib.adb.query.AQuery;
import de.mhus.lib.annotations.adb.DbType;
import de.mhus.lib.core.MSql;
import de.mhus.lib.core.config.IConfig;

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
public class DialectPostgresql extends DialectDefault {

	@Override
	public String normalizeColumnName(String columnName) {
		//		if ("key".equals(columnName))
		//			return "key_";
		//		return columnName;
		return columnName + "_"; // TODO not working at all
	}

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String toSqlDateValue(Date date) {
		synchronized (dateFormat) {
			return "'" + dateFormat.format(date) + "'";
		}
	}

	@Override
	protected void createTableLastCheck(IConfig ctable, String tn, StringBuffer sql) {
	}

	@Override
	public String escape(String text) {
		String ret = MSql.escape(text);
		if (ret == null || text == null) return ret;
		if (ret.indexOf('\\') < 0)
			return ret;
		return ret.replaceAll("\\\\", "\\\\\\\\");
	}

	@Override
	protected boolean equalTypes(String should, String is, int fSize) {
		is = is.toUpperCase();
		if (is.indexOf("CHAR") >=0) {
			is = is + "(" + fSize + ")"; // add size to type
		} else
		if (is.equals("INT4")) is = "INT";
		else
		if (is.equals("FLOAT8")) is = "DOUBLE PRECISION";
		
		return should.equals(is);
	}

	@Override
	public String getDbType(String type, String size) {
		String t = type.toUpperCase();
		
		if (t.equals(DbType.TYPE.BLOB.name())) 
			t = "BYTEA";
		else
		if (t.equals(DbType.TYPE.DATETIME.name())) 
			t = "TIMESTAMP";
		else
		if (t.equals(DbType.TYPE.DOUBLE.name()))
			t = "DOUBLE PRECISION";
		else
			t = super.getDbType(type, size);

		return t;
	}
	
	@Override
	protected ResultSet findTable(DatabaseMetaData meta, String name) throws SQLException {
		 return meta.getTables(null, null, name.toLowerCase(), new String[] {"TABLE"});
   }

	@Override
	protected ResultSet findColumn(DatabaseMetaData meta, String tn, String fName) throws SQLException {
		return meta.getColumns(null, null, tn.toLowerCase(), fName);
	}

	@Override
	protected ResultSet findPrimaryKeys(DatabaseMetaData meta, String tn) throws SQLException {
		return  meta.getPrimaryKeys(null, null, tn.toLowerCase());
	}

	@Override
	protected ResultSet findIndex(DatabaseMetaData meta, String table, boolean unique) throws SQLException {
		return meta.getIndexInfo(null, null, table.toLowerCase(), unique, false);
	}
	
	@Override
	public void createQuery(APrint p, AQuery<?> query) {
		StringBuffer buffer = ((SqlDialectCreateContext)query.getContext()).getBuffer();

		if (p instanceof ALimit) {
			if (((ALimit)p).getLimit() >= 0)
				buffer.append(" LIMIT ").append(((ALimit)p).getLimit());
			if (((ALimit)p).getOffset() >= 0)
				buffer.append(" OFFSET ").append(((ALimit)p).getOffset());
		} else
			super.createQuery(p, query);

	}
	
}
