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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import de.mhus.lib.errors.MException;

public class MSql {

    /**
     * Prepare a string to use it in a sql query. It will also append the quots. A null string will
     * be represented as one space of NULL
     *
     * @param string
     * @param notNull Set to true will return a single space instead of the string NULL
     * @return encoded string
     */
    public static String encode(String string, boolean notNull) {
        if (string == null) return (notNull ? "' '" : "NULL");
        if (string.indexOf('\'') < 0) return '\'' + string + '\'';
        return '\'' + string.replaceAll("'", "''") + '\'';
    }

    /**
     * Escape all single quots to double single quots.
     *
     * @param in
     * @return escaped string
     */
    public static String escape(String in) {
        if (in == null) return "";
        if (in.indexOf('\'') < 0) return in;
        return in.replaceAll("'", "''");
    }

    /**
     * Remove all double single quots.
     *
     * @param in
     * @return unescaped string
     */
    public static String unescape(String in) {
        if (in == null) return "";
        if (in.indexOf('\'') < 0) return in;
        return in.replaceAll("''", "'");
    }

    /**
     * Escape single quots and truncates the string is needed.
     *
     * @param in
     * @param truncateSize
     * @return escaped string
     */
    public static String escape(String in, int truncateSize) {
        if (in == null) return null;
        if (in.length() > truncateSize) in = in.substring(0, truncateSize);
        if (in.indexOf('\'') < 0) return in;
        return in.replaceAll("'", "''");
    }

    /**
     * Executes a bundle of queries, separated by semicolon.
     *
     * @param sth
     * @param sql
     * @throws SQLException
     */
    public static void executeUpdateQueries(Statement sth, String sql) throws SQLException {
        String[] parts = sql.split(";");
        for (int i = 0; i < parts.length; i++)
            if (parts[i].trim().length() != 0) {
                sth.executeUpdate(parts[i].trim());
            }
    }

    /**
     * used to prepare SQL string literals by doubling each embedded ' and wrapping in ' at each
     * end. Further quoting is required to use the results in Java String literals. If you use
     * PreparedStatement, then this method is not needed. The ' quoting is automatically handled for
     * you.
     *
     * @param sql Raw SQL string literal
     * @return sql String literal enclosed in '
     */
    public static String quoteSQL(String sql) {
        StringBuilder sb = new StringBuilder(sql.length() + 5);
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
     * @return normalized string
     */
    public static String toSqlLabel(String in, Connection con) {
        boolean error = false;
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (!(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '_')
                    || (i == 0 && c >= '0' && c <= '9')) {
                error = true;
                break;
            }
        }

        if (!error) return in;

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (i == 0 && c >= '0' && c <= '9') {
                out.append('_');
            }
            if (!(c >= 'a' && c <= 'z'
                    || c >= 'A' && c <= 'Z'
                    || c >= '0' && c <= '9'
                    || c == '_')) {
                out.append('_');
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }

    public static void fillProperties(ResultSet res, MProperties prop)
            throws SQLException, MException {
        fillProperties(res, prop, null);
    }

    public static void fillProperties(ResultSet res, MProperties prop, SqlTranslator translator)
            throws SQLException, MException {
        ResultSetMetaData meta = res.getMetaData();
        if (translator == null) translator = new SqlTranslator();
        for (int i = 1; i < meta.getColumnCount(); i++) {
            String name = meta.getColumnName(i);
            int type = meta.getColumnType(i);

            switch (type) {
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    translator.toString(res, prop, name, i, type);
                    break;

                case Types.NUMERIC:
                case Types.DECIMAL:
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.REAL:
                    translator.toNumber(res, prop, name, i, type);
                    break;

                case Types.BIT:
                    translator.toBoolean(res, prop, name, i, type);
                    break;

                case Types.INTEGER:
                    translator.toInt(res, prop, name, i, type);
                    break;

                case Types.BIGINT:
                    translator.toLong(res, prop, name, i, type);
                    break;

                case Types.FLOAT:
                case Types.DOUBLE:
                    translator.toDouble(res, prop, name, i, type);
                    break;

                case Types.BINARY:
                case Types.VARBINARY:
                case Types.LONGVARBINARY:
                case Types.CLOB:
                    translator.toBinary(res, prop, name, i, type);
                    break;

                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                    translator.toDate(res, prop, name, i, type);
                    break;
            }
        }
    }

    public static class SqlTranslator {

        public void toString(ResultSet res, MProperties prop, String name, int i, int type)
                throws MException, SQLException {
            prop.setString(name, res.getString(i));
        }

        public void toDate(ResultSet res, MProperties prop, String name, int i, int type)
                throws MException, SQLException {
            prop.setDate(name, res.getTimestamp(i));
        }

        public void toBinary(ResultSet res, MProperties prop, String name, int i, int type)
                throws MException, SQLException {}

        public void toDouble(ResultSet res, MProperties prop, String name, int i, int type)
                throws MException, SQLException {
            prop.setDouble(name, res.getDouble(i));
        }

        public void toLong(ResultSet res, MProperties prop, String name, int i, int type)
                throws MException, SQLException {
            prop.setLong(name, res.getLong(i));
        }

        public void toInt(ResultSet res, MProperties prop, String name, int i, int type)
                throws MException, SQLException {
            prop.setInt(name, res.getInt(i));
        }

        public void toBoolean(ResultSet res, MProperties prop, String name, int i, int type)
                throws MException, SQLException {
            prop.setBoolean(name, res.getBoolean(i));
        }

        public void toNumber(ResultSet res, MProperties prop, String name, int i, int type)
                throws MException, SQLException {
            prop.setNumber(name, res.getBigDecimal(i));
        }
    }

    /**
     * Validate the name as a column name. If the name contains not allowed characters the method
     * will throw a sql exception. Use this method to deny sql injection for column names.
     *
     * @param name The name of the column
     * @return The name. The method can be used in line.
     * @throws SQLException
     */
    public static String column(String name) throws SQLException {
        String n = name;
        if (n.length() > 2 && n.startsWith("`") && n.endsWith("`"))
            n = n.substring(1, n.length() - 1);
        if (!n.matches("^[a-zA-Z_]+[a-zA-Z0-9._]*$"))
            throw new SQLException("name is not a column identifier, possible injection");
        return name;
    }

    /**
     * Returns true if a column exists in the result set. The function is linear do not use it in
     * every iteration of an result set. Try to cache the result. Be aware of case sensitive / not
     * sensitive database implementations.
     *
     * @param rs Result Set to check
     * @param column Name of the column to that should exists
     * @return true if the column exists in the result set.
     * @throws SQLException
     */
    public static boolean hasColumn(ResultSet rs, String column) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (column.equals(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }
}
