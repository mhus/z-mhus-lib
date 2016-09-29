/*
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import de.mhus.lib.errors.MException;

/**
 * <p>MSql class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MSql {

	/**
	 * Prepare a string to use it in a sql query. It will also append the quots. A null string will be represented as one space of NULL
	 *
	 * @param string a {@link java.lang.String} object.
	 * @param notNull Set to true will return a single space instead of the string NULL
	 * @return a {@link java.lang.String} object.
	 */
	public static String encode(String string, boolean notNull) {
		if (string == null)
			return (notNull ? "' '" : "NULL");
		if (string.indexOf('\'') < 0)
			return '\'' + string + '\'';
		return '\'' + string.replaceAll("'", "''") + '\'';
	}

	/**
	 * Escape all single quots to double single quots.
	 *
	 * @param in a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String escape(String in) {
		if (in == null)
			return "";
		if (in.indexOf('\'') < 0)
			return in;
		return in.replaceAll("'", "''");
	}

	/**
	 * Remove all double single quots.
	 *
	 * @param in a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String unescape(String in) {
		if (in == null)
			return "";
		if (in.indexOf('\'') < 0)
			return in;
		return in.replaceAll("''", "'");
	}

	/**
	 * Escape single quots and truncates the string is needed.
	 *
	 * @param in a {@link java.lang.String} object.
	 * @param truncateSize a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String escape(String in, int truncateSize) {
		if (in == null)
			return null;
		if (in.length() > truncateSize)
			in = in.substring(0, truncateSize);
		if (in.indexOf('\'') < 0)
			return in;
		return in.replaceAll("'", "''");
	}

	/**
	 * Executes a bundle of queries, separated by semicolon.
	 *
	 * @param sth a {@link java.sql.Statement} object.
	 * @param sql a {@link java.lang.String} object.
	 * @throws java.sql.SQLException if any.
	 */
	public static void executeUpdateQueries(Statement sth, String sql)
			throws SQLException {
		String[] parts = sql.split(";");
		for (int i = 0; i < parts.length; i++)
			if (parts[i].trim().length() != 0) {
				sth.executeUpdate(parts[i].trim());
			}
	}

	/**
	 * used to prepare SQL string literals by doubling each embedded ' and
	 * wrapping in ' at each end. Further quoting is required to use the results
	 * in Java String literals. If you use PreparedStatement, then this method
	 * is not needed. The ' quoting is automatically handled for you.
	 *
	 * @param sql
	 *            Raw SQL string literal
	 * @return sql String literal enclosed in '
	 */
	public static String quoteSQL(String sql) {
		StringBuffer sb = new StringBuffer(sql.length() + 5);
		sb.append('\'');
		for (int i = 0; i < sql.length(); i++) {
			char c = sql.charAt(i);
			if (c == '\'') {
				sb.append("\'\'");
			} else {
				sb.append(c);
			}
		}
		sb.append('\'');
		return sb.toString();
	}

	/**
	 * Removes all non standard characters. Currently do not validate keywords.
	 *
	 * @param in The string to validate
	 * @param con Optional the sql connection to validate keywords. null is possible.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toSqlLabel(String in, Connection con) {
		boolean error = false;
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			if (!(c >='a' && c <= 'z' || c >='A' && c <= 'Z' || c >= '0' && c <= '9' || c == '_'  ) ||
			     (i == 0 && c >= '0' && c <= '9' )
			   ) {
				error =true;
				break;
			}
			
		}
		
		if (!error) return in;

		StringBuffer out = new StringBuffer();
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			if (i == 0 && c >= '0' && c <= '9' ) {
				out.append('_');
			}
			if (!  (c >='a' && c <= 'z' || c >='A' && c <= 'Z' || c >= '0' && c <= '9' || c == '_'  ) ) {
				out.append('_');
			} else {
				out.append(c);
			}
		}
		return out.toString();
	}


	/**
	 * <p>fillProperties.</p>
	 *
	 * @param res a {@link java.sql.ResultSet} object.
	 * @param prop a {@link de.mhus.lib.core.MProperties} object.
	 * @throws java.sql.SQLException if any.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public static void fillProperties(ResultSet res, MProperties prop) throws SQLException, MException {
		fillProperties(res, prop, null);
	}
	
	/**
	 * <p>fillProperties.</p>
	 *
	 * @param res a {@link java.sql.ResultSet} object.
	 * @param prop a {@link de.mhus.lib.core.MProperties} object.
	 * @param translator a {@link de.mhus.lib.core.MSql.SqlTranslator} object.
	 * @throws java.sql.SQLException if any.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public static void fillProperties(ResultSet res, MProperties prop, SqlTranslator translator) throws SQLException, MException {
		ResultSetMetaData meta = res.getMetaData();
		if (translator == null) translator = new SqlTranslator();
		for (int i = 1; i < meta.getColumnCount(); i++) {
			String name = meta.getColumnName(i);
			int type = meta.getColumnType(i);
			
			switch( type ) {
		      case Types.CHAR:
		      case Types.VARCHAR:
		      case Types.LONGVARCHAR:
		    	  translator.toString(res,prop, name, i, type);
		        break;

		      case Types.NUMERIC:
		      case Types.DECIMAL:
		      case Types.TINYINT:
		      case Types.SMALLINT:
		      case Types.REAL:
		    	  translator.toNumber(res,prop, name, i, type);
		        break;

		      case Types.BIT:
		    	  translator.toBoolean(res,prop, name, i, type);
		        break;

		      case Types.INTEGER:
		    	  translator.toInt(res,prop, name, i, type);
		        break;

		      case Types.BIGINT:
		    	  translator.toLong(res,prop, name, i, type);
		        break;


		      case Types.FLOAT:
		      case Types.DOUBLE:
		    	  translator.toDouble(res,prop, name, i, type);
		        break;

		      case Types.BINARY:
		      case Types.VARBINARY:
		      case Types.LONGVARBINARY:
		      case Types.CLOB:
		    	  translator.toBinary(res,prop, name, i, type);
		        break;

		      case Types.DATE:
		      case Types.TIME:
		      case Types.TIMESTAMP:
		    	  translator.toDate(res,prop, name, i, type);
		        break;
		        
		    }
			
			
		}
	}

	public static class SqlTranslator {

		public void toString(ResultSet res, MProperties prop, String name, int i, int type) throws MException, SQLException {
	    	  prop.setString(name, res.getString(i));
		}

		public void toDate(ResultSet res, MProperties prop, String name, int i, int type) throws MException, SQLException {
	    	  prop.setDate(name, res.getTimestamp(i));
		}

		public void toBinary(ResultSet res, MProperties prop, String name, int i, int type) throws MException, SQLException {
			
		}

		public void toDouble(ResultSet res, MProperties prop, String name, int i, int type) throws MException, SQLException {
	    	  prop.setDouble(name, res.getDouble(i));
		}

		public void toLong(ResultSet res, MProperties prop, String name, int i, int type) throws MException, SQLException {
	    	  prop.setLong(name, res.getLong(i));
		}

		public void toInt(ResultSet res, MProperties prop, String name, int i, int type) throws MException, SQLException {
	    	  prop.setInt(name, res.getInt(i));
		}

		public void toBoolean(ResultSet res, MProperties prop, String name, int i, int type) throws MException, SQLException {
	    	  prop.setBoolean(name, res.getBoolean(i));
		}

		public void toNumber(ResultSet res, MProperties prop, String name, int i, int type) throws MException, SQLException {
	    	  prop.setNumber(name, res.getBigDecimal(i));
		}
		
	}
		
}
