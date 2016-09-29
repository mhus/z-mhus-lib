package de.mhus.lib.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;

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
public class DialectHsqldb extends DialectDefault {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	protected String getFieldConfig(ResourceNode f) {
		try {
			String type = getDbType(f);
			String ret = f.getString("name",null).toUpperCase() + " " + type;

			String def = f.getExtracted("default");
			if (def != null) {
				def = getDbDef(def);
				ret = ret + " DEFAULT " + def;
			}
			boolean notNull = f.getBoolean("notnull", false);
			if (notNull)
				ret = ret + " NOT NULL";
			else
				ret = ret + " NULL";
			return ret;
		} catch (MException e) {
			throw new MRuntimeException(e);
		}
	}

	protected String getFieldConfigWithoutExtras(ResourceNode f) {
		try {
			String type = getDbType(f);
			String ret = f.getString("name").toUpperCase() + " " + type;

			//		String def = f.getExtracted("default");
			//		if (def != null) {
			//			def = getDbDef(def);
			//			ret = ret + " DEFAULT " + def;
			//		}
			//		boolean notNull = f.getBoolean("notnull", false);
			//		if (notNull)
			//			ret = ret + " NOT NULL";
			//		else
			//			ret = ret + " NULL";
			return ret;
		} catch (MException e) {
			throw new MRuntimeException(e);
		}
	}


	@Override
	public String toSqlDateValue(Date date) {
		synchronized (dateFormat) {
			return "TO_DATE('" + dateFormat.format(date) + "','YYYY-MM-DD HH:MI:SS')";
		}
	}

	@Override
	public String normalizeColumnName(String columnName) {
		return columnName.toUpperCase();
	}

	@Override
	protected void alterColumn(Statement sth,String tn, ResourceNode cfield) {
		String sql = "ALTER TABLE " + tn + " ALTER COLUMN " + getFieldConfigWithoutExtras(cfield);
		log().t("alter table",sql);
		try {
			sth.execute(sql);
		} catch (Exception e) {
			log().i(sql,e);
		}

	}

	@Override
	protected void alterTableChangePrimaryKey(Statement sth, String tn,
			String keys) {
		alterTableDropPrimaryKey(sth, tn);
		alterTableAddPrimaryKey(sth, tn, keys);
	}

	@Override
	protected void recreateIndex(Statement sth, boolean unique, boolean btree,
			String iName, String table, String columns) {

		iName = table + iName;
		String sql = "DROP INDEX " + iName;
		log().t(sql);
		try {
			sth.execute(sql.toString());
		} catch (Exception e) {
			log().i(sql,e);
		}
		sql = "CREATE " + (unique ? "UNIQUE" : "" ) + " INDEX " + iName + (btree ? " USING BTREE" : "") +" ON "+table+ "("+columns+")";
		log().t(sql);
		try {
			sth.execute(sql.toString());
		} catch (Exception e) {
			log().i(sql,e);
		}
	}

	@Override
	protected void createIndex(Statement sth, boolean unique, boolean btree,
			String iName, String table, String columns) {

		iName = table + iName;

		String sql = "CREATE " + (unique ? "UNIQUE" : "" ) + " INDEX " + iName + (btree ? " USING BTREE" : "") +" ON "+table+ "("+columns+")";
		log().t(sql);
		try {
			sth.execute(sql.toString());
		} catch (Exception e) {
			log().i(sql,e);
		}

	}

	@Override
	protected boolean equalsIndexName(String table, String iName, String iName2) {
		return iName2.equals(table + iName);
	}

	@Override
	public String normalizeTableName(String tableName) throws Exception {
		return tableName.toUpperCase() + "_";
	}

	@Override
	public String normalizeIndexName(String tableName) throws Exception {
		return tableName.toUpperCase();
	}

	@Override
	public void prepareConnection(Connection con) throws SQLException {
		super.prepareConnection(con);
		Statement sth = con.createStatement();
		sth.execute("SET DATABASE TRANSACTION CONTROL MVCC");
		sth.execute("SET DATABASE DEFAULT ISOLATION LEVEL READ COMMITTED");
		//sth.execute("SET DATABASE TRANSACTION ROLLBACK ON DEADLOCK TRUE");
		sth.close();
	}

}
