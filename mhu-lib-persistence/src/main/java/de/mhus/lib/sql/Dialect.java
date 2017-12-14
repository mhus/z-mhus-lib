package de.mhus.lib.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.query.AAnd;
import de.mhus.lib.adb.query.AAttribute;
import de.mhus.lib.adb.query.ACompare;
import de.mhus.lib.adb.query.AConcat;
import de.mhus.lib.adb.query.ADbAttribute;
import de.mhus.lib.adb.query.ADynValue;
import de.mhus.lib.adb.query.AEnumFix;
import de.mhus.lib.adb.query.AFix;
import de.mhus.lib.adb.query.ALimit;
import de.mhus.lib.adb.query.AList;
import de.mhus.lib.adb.query.ALiteral;
import de.mhus.lib.adb.query.ALiteralList;
import de.mhus.lib.adb.query.ANot;
import de.mhus.lib.adb.query.ANull;
import de.mhus.lib.adb.query.AOperation;
import de.mhus.lib.adb.query.AOr;
import de.mhus.lib.adb.query.AOrder;
import de.mhus.lib.adb.query.APart;
import de.mhus.lib.adb.query.APrint;
import de.mhus.lib.adb.query.AQuery;
import de.mhus.lib.adb.query.AQueryCreator;
import de.mhus.lib.adb.query.ASubQuery;
import de.mhus.lib.annotations.adb.DbType;
import de.mhus.lib.cao.CaoMetaDefinition;
import de.mhus.lib.cao.util.MetadataBundle;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MSql;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.parser.Parser;
import de.mhus.lib.core.parser.ParsingPart;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;
import de.mhus.lib.sql.commonparser.Common2SqlCompiler;
import de.mhus.lib.sql.parser.FunctionPart;
import de.mhus.lib.sql.parser.ICompiler;
import de.mhus.lib.sql.parser.SqlCompiler;

/**
 * The dialect implements database vendor specific behaviors. The behavior outside
 * this class should be the same. This includes an abstraction of the database definition
 * the query language and execution behavior. The dialect also normalize the naming.
 * 
 * This class can compare a configuration with a database table structure
 * and can modify the database structure without deleting existing tables.
 * 
 * TODO: on request: remove other columns
 * TODO: views, foreign keys
 * @author mikehummel
 *
 */
public abstract class Dialect extends MObject implements ICompiler, AQueryCreator {

	// POSSIBLE TYPES OF COLUMNS

	public static final String I_TYPE = "type";
	public static final String I_UNIQUE = "unique";

	public static final String I_NAME = "name";

	public static final String I_TABLE = "table";

	public static final String I_FIELDS = "fields";

	public static final String K_PRIMARY_KEY = "primary_key";

	public static final String K_NAME = "name";

	public static final String K_TYPE = "type";

	public static final String K_SIZE = "size";

	public static final String K_DEFAULT = "default";

	public static final String K_NOT_NULL = "notnull";

	public static final String K_CATEGORIES = "category";

	public static final String K_DESCRIPTION = "description";
	
	public static final String K_HINTS = "hints";
	
	public static final String C_VIRTUAL = "[virtual]";

	public static final String C_PRIMARY_KEY = "[pk]";

	public static final String C_ENUMERATION = "[enum]";

	private Parser sqlParser = new SqlCompiler(this);
	private Parser commonParser = new Common2SqlCompiler(this);


	/**
	 * Return the named type for a TYPE enum value. Use this
	 * function to be sure you have all hacks included.
	 * 
	 * @param type The type enum
	 * @return x null if type is null/unknown or the name
	 */
	public static String typeEnumToString(DbType.TYPE type) {
		if (type == null) return null;
		if (type == DbType.TYPE.UNKNOWN) return null;
		return type.name().toLowerCase();
	}

	/**
	 * Create a database structure from configuration.
	 * 
	 * @param data
	 * @param db
	 * @param caoMeta
	 * @param cleanup 
	 * @throws Exception
	 */
	public void createStructure(IConfig data, DbConnection db,MetadataBundle caoMeta, boolean cleanup) throws Exception {
		createTables(data,db,caoMeta, cleanup);
		createIndexes(data,db,caoMeta, cleanup);
		createData(data,db);
	}

	/**
	 * Create or Update the defined tables. The config object need a bundle of 'table' configurations
	 * which define the needed table structure.
	 * Example:
	 * [config]
	 *   [table name='table name' primary_key='field1,field2,...']
	 *     [field name='field name' prefix='prefix_' type='field type' default='def value' notnull=yes/no /]
	 *   [/table]
	 * [/config]
	 * @param data
	 * @param db
	 * @param caoBundle
	 * @param cleanup 
	 * @throws Exception
	 */
	public abstract void createTables(IConfig data, DbConnection db, MetadataBundle caoBundle, boolean cleanup) throws Exception;

	/**
	 * Create or update indexes. The configuration need a bundle of 'index' elements to define the indexes.
	 * Example:
	 * [config]
	 *   [index name='name' table='table name' btree=yes/no unique=yes/no fields='field1,field2,...'/]
	 * [/config]
	 * @param data
	 * @param db
	 * @param caoMeta
	 * @param cleanup 
	 * @throws Exception
	 */
	public abstract void createIndexes(IConfig data, DbConnection db, MetadataBundle caoMeta, boolean cleanup) throws Exception;

	/**
	 * Execute 'data' configs:
	 * select = a select query to define a condition and/or data set
	 * set and column = set a value in the config to the value from column
	 * condition = found,not found,error,no error
	 * 
	 * @param data
	 * @param db
	 * @throws Exception
	 */
	public abstract  void createData(IConfig data, DbConnection db) throws Exception;

	protected String getFieldConfig(IConfig f) {
		String type = getDbType(f);

		String ret = normalizeColumnName(f.getString("name",null)) + " " + type;

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
	}

	/**
	 * Returns a formated default value.
	 * 
	 * @param def
	 * @return x
	 */
	protected String getDbDef(String def) {
		try {
			Double.valueOf(def);
		} catch (NumberFormatException e) {
			def = MSql.quoteSQL(def);
		}
		return def;
	}


	/**
	 * Return a normalized cao type from the config.
	 * @param f
	 * @return x
	 */
	protected CaoMetaDefinition.TYPE getCaoType(IConfig f) {
		String type = f.getString("type",DbType.TYPE.STRING.name()).toUpperCase();
		CaoMetaDefinition.TYPE t = CaoMetaDefinition.TYPE.STRING;
		if (f.getString(K_CATEGORIES, "").indexOf(C_ENUMERATION) > -1) {
			t = CaoMetaDefinition.TYPE.STRING;
		} else
			if (type.equals(DbType.TYPE.STRING.name()) || type.equals("CHAR") || type.equals("VARCHAR") ) {
			} else
				if (type.equals(DbType.TYPE.INT.name()) || type.equals("INTEGER")) {
					t = CaoMetaDefinition.TYPE.LONG;
				} else
					if (type.equals("DATE")) {
						t = CaoMetaDefinition.TYPE.DATETIME;
					} else
						if (type.equals(DbType.TYPE.DATETIME.name())) {
							t = CaoMetaDefinition.TYPE.DATETIME;
						} else
							if (type.equals("TIME")) {
								t = CaoMetaDefinition.TYPE.DATETIME;
							} else
								if (type.equals("TIMESTAMP")) {
									t = CaoMetaDefinition.TYPE.DATETIME;
								} else
									if (type.equals(DbType.TYPE.BOOL.name()) || type.equals("BOOLEAN")) {
										t = CaoMetaDefinition.TYPE.BOOLEAN;
									} else
										if (type.equals(DbType.TYPE.BLOB.name())) {
											t = CaoMetaDefinition.TYPE.BINARY;
										} else
											if (type.equals(DbType.TYPE.DOUBLE.name())) {
												t = CaoMetaDefinition.TYPE.DOUBLE;
											} else
												if (type.equals(DbType.TYPE.FLOAT.name())) {
													t = CaoMetaDefinition.TYPE.DOUBLE;
												} else
													if (type.equals("TEXT")) {
													} else
														if (type.equals("LONGTEXT")) {
														} else
															if (type.equals("LONGBLOB")) {
																t = CaoMetaDefinition.TYPE.BINARY;
															} else
																if (type.equals(DbType.TYPE.UUID.name())) {
																	t = CaoMetaDefinition.TYPE.ELEMENT;
																}
		return t;
	}

	/**
	 * Return a database specific type for the normalized type from configuration.
	 * @param f
	 * @return x
	 */
	public String getDbType(IConfig f) {
		return getDbType(f.getString("type","string"),f.getString("size", "100"));
	}

	/**
	 * Return a database specific type for the normalized type from the type and size.
	 * @param type The general type name - see const
	 * @param size the size, if needed
	 * @return x
	 */
	public String getDbType(String type, String size) {
		String t = type.toUpperCase();
		if (t.equals(DbType.TYPE.STRING.name()) || t.equals("CHAR") || t.equals("VARCHAR") ) {
			t = "VARCHAR(" + size + ")";
		} else
			if (t.equals(DbType.TYPE.INT.name()) || t.equals("INTEGER")) {
				t = "INT";
			} else
				if (t.equals(DbType.TYPE.LONG.name()) ) {
					t = "BIGINT";
				} else
					if (t.equals("DATE")) {
						t = "DATE";
					} else
						if (t.equals(DbType.TYPE.DATETIME.name())) {
							t = "DATETIME";
						} else
							if (t.equals("TIME")) {
								t = "TIME";
							} else
								if (t.equals("TIMESTAMP")) {
									t = "TIMESTAMP";
								} else
									if (t.equals(DbType.TYPE.BOOL.name()) || t.equals("BOOLEAN")) {
										t = "TINYINT";
									} else
										if (t.equals(DbType.TYPE.BLOB.name())) {
											t = "BLOB";
										} else
											if (t.equals(DbType.TYPE.DOUBLE.name())) {
												t = "DOUBLE";
											} else
												if (t.equals(DbType.TYPE.FLOAT.name())) {
													t = "FLOAT";
												} else
													if (t.equals("TEXT")) {
														t = "TEXT";
													} else
														if (t.equals("LONGTEXT")) {
															t = "LONGTEXT";
														} else
															if (t.equals("LONGBLOB")) {
																t = "LONGBLOB";
															} else
																if (t.equals(DbType.TYPE.UUID.name())) {
																	t = "VARCHAR(40)";
																}
		return t;
	}

	/**
	 * Return a valid index name.
	 * 
	 * @param tableName
	 * @return x
	 * @throws Exception
	 */
	public abstract String normalizeIndexName(String tableName) throws Exception;

	/**
	 * Return a valid table name.
	 * 
	 * @param tableName
	 * @return x
	 * @throws Exception
	 */
	public abstract String normalizeTableName(String tableName) throws Exception;

	/**
	 * Return a valid column name.
	 * 
	 * @param columnName
	 * @return x
	 */
	public abstract String normalizeColumnName(String columnName);

	/**
	 * Return a parser object to parse a sql query. The parser should change the output to be
	 * specialized to the database type.
	 * @param language 
	 * 
	 * @return x
	 * @throws MException
	 */
	public Parser getQueryParser(String language) throws MException {
		//		return new SimpleQueryParser(); // from dialect
		//		return new SqlCompiler(); // from dialect

		if (language == null || JdbcConnection.LANGUAGE_SQL.equals(language))
			return sqlParser;

		if (DbConnection.LANGUAGE_COMMON.equals(language))
			return commonParser;

		throw new MException(this,"language not supported", language);
	}

	/**
	 * Interface for the parser.
	 */
	@Override
	public boolean isParseAttributes() {
		return true;
	}

	/**
	 * Interface for the parser.
	 */
	@Override
	public ParsingPart compileFunction(FunctionPart function) {
		return function;
	}

	@Override
	public String toSqlDateValue(Date date) {
		return "'" + MDate.toIsoDate(date) + "'";
	}

	@Override
	public String valueToString(Object value) {
		return MCast.objectToString(value);
	}

	/**
	 * Detects the language of this query string. By default it will return null
	 * what means the default language.
	 * 
	 * It will detect the common language.
	 * 
	 * @param sql
	 * @return x
	 */
	public String detectLanguage(String sql) {
		if (sql == null) return null;
		if (sql.startsWith("<common>")) return DbConnection.LANGUAGE_COMMON;
		return null;
	}

	public void prepareConnection(Connection con) throws SQLException {
		con.setAutoCommit(false);
	}

}
