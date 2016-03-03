package de.mhus.lib.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.mhus.lib.core.MDate;

/**
 * This proxy is used to hold a instance of the connection while the ResultSet is used. That's
 * the only functionality.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JdbcResult extends DbResult {

	private ResultSet instance;
	private DbStatement sth; // need to have a reference to the statement to avoid a finalize
	private List<String> columnNames;

	JdbcResult(DbStatement sth, ResultSet instance) {
		this.sth = sth;
		this.instance = instance;
	}

	/**
	 * <p>unwrap.</p>
	 *
	 * @param iface a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a T object.
	 * @throws java.sql.SQLException if any.
	 */
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return instance.unwrap(iface);
	}

	/**
	 * <p>isWrapperFor.</p>
	 *
	 * @param iface a {@link java.lang.Class} object.
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return instance.isWrapperFor(iface);
	}

	/** {@inheritDoc} */
	@Override
	public boolean next() throws SQLException {
		return instance.next();
	}

	/** {@inheritDoc} */
	@Override
	public void close() throws SQLException {
		instance.close();
	}

	/**
	 * <p>wasNull.</p>
	 *
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean wasNull() throws SQLException {
		return instance.wasNull();
	}

	/**
	 * <p>getString.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.lang.String} object.
	 * @throws java.sql.SQLException if any.
	 */
	public String getString(int columnIndex) throws SQLException {
		return instance.getString(columnIndex);
	}

	/**
	 * <p>getBoolean.</p>
	 *
	 * @param columnIndex a int.
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean getBoolean(int columnIndex) throws SQLException {
		return instance.getBoolean(columnIndex);
	}

	/**
	 * <p>getByte.</p>
	 *
	 * @param columnIndex a int.
	 * @return a byte.
	 * @throws java.sql.SQLException if any.
	 */
	public byte getByte(int columnIndex) throws SQLException {
		return instance.getByte(columnIndex);
	}

	/**
	 * <p>getShort.</p>
	 *
	 * @param columnIndex a int.
	 * @return a short.
	 * @throws java.sql.SQLException if any.
	 */
	public short getShort(int columnIndex) throws SQLException {
		return instance.getShort(columnIndex);
	}

	/**
	 * <p>getInt.</p>
	 *
	 * @param columnIndex a int.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	public int getInt(int columnIndex) throws SQLException {
		return instance.getInt(columnIndex);
	}

	/**
	 * <p>getLong.</p>
	 *
	 * @param columnIndex a int.
	 * @return a long.
	 * @throws java.sql.SQLException if any.
	 */
	public long getLong(int columnIndex) throws SQLException {
		return instance.getLong(columnIndex);
	}

	/**
	 * <p>getFloat.</p>
	 *
	 * @param columnIndex a int.
	 * @return a float.
	 * @throws java.sql.SQLException if any.
	 */
	public float getFloat(int columnIndex) throws SQLException {
		return instance.getFloat(columnIndex);
	}

	/**
	 * <p>getDouble.</p>
	 *
	 * @param columnIndex a int.
	 * @return a double.
	 * @throws java.sql.SQLException if any.
	 */
	public double getDouble(int columnIndex) throws SQLException {
		return instance.getDouble(columnIndex);
	}

	/**
	 * <p>getBigDecimal.</p>
	 *
	 * @param columnIndex a int.
	 * @param scale a int.
	 * @return a {@link java.math.BigDecimal} object.
	 * @throws java.sql.SQLException if any.
	 */
	@SuppressWarnings("deprecation")
	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		return instance.getBigDecimal(columnIndex, scale);
	}

	/**
	 * <p>getBytes.</p>
	 *
	 * @param columnIndex a int.
	 * @return an array of byte.
	 * @throws java.sql.SQLException if any.
	 */
	public byte[] getBytes(int columnIndex) throws SQLException {
		return instance.getBytes(columnIndex);
	}

	/**
	 * <p>getDate.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.sql.Date} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Date getDate(int columnIndex) throws SQLException {
		return instance.getDate(columnIndex);
	}

	/**
	 * <p>getTime.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.sql.Time} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Time getTime(int columnIndex) throws SQLException {
		return instance.getTime(columnIndex);
	}

	/**
	 * <p>getTimestamp.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.sql.Timestamp} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return instance.getTimestamp(columnIndex);
	}

	/**
	 * <p>getAsciiStream.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.io.InputStream} object.
	 * @throws java.sql.SQLException if any.
	 */
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return instance.getAsciiStream(columnIndex);
	}

	/**
	 * <p>getUnicodeStream.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.io.InputStream} object.
	 * @throws java.sql.SQLException if any.
	 */
	@SuppressWarnings("deprecation")
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return instance.getUnicodeStream(columnIndex);
	}

	/**
	 * <p>getBinaryStream.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.io.InputStream} object.
	 * @throws java.sql.SQLException if any.
	 */
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return instance.getBinaryStream(columnIndex);
	}

	/** {@inheritDoc} */
	@Override
	public String getString(String columnLabel) throws SQLException {
		return instance.getString(columnLabel);
	}

	/** {@inheritDoc} */
	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		return instance.getBoolean(columnLabel);
	}

	/**
	 * <p>getByte.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a byte.
	 * @throws java.sql.SQLException if any.
	 */
	public byte getByte(String columnLabel) throws SQLException {
		return instance.getByte(columnLabel);
	}

	/**
	 * <p>getShort.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a short.
	 * @throws java.sql.SQLException if any.
	 */
	public short getShort(String columnLabel) throws SQLException {
		return instance.getShort(columnLabel);
	}

	/** {@inheritDoc} */
	@Override
	public int getInt(String columnLabel) throws SQLException {
		return instance.getInt(columnLabel);
	}

	/** {@inheritDoc} */
	@Override
	public long getLong(String columnLabel) throws SQLException {
		return instance.getLong(columnLabel);
	}

	/** {@inheritDoc} */
	@Override
	public float getFloat(String columnLabel) throws SQLException {
		return instance.getFloat(columnLabel);
	}

	/** {@inheritDoc} */
	@Override
	public double getDouble(String columnLabel) throws SQLException {
		return instance.getDouble(columnLabel);
	}

	/**
	 * <p>getBigDecimal.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param scale a int.
	 * @return a {@link java.math.BigDecimal} object.
	 * @throws java.sql.SQLException if any.
	 */
	@SuppressWarnings("deprecation")
	public BigDecimal getBigDecimal(String columnLabel, int scale)
			throws SQLException {
		return instance.getBigDecimal(columnLabel, scale);
	}

	/**
	 * <p>getBytes.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return an array of byte.
	 * @throws java.sql.SQLException if any.
	 */
	public byte[] getBytes(String columnLabel) throws SQLException {
		return instance.getBytes(columnLabel);
	}

	/** {@inheritDoc} */
	@Override
	public Date getDate(String columnLabel) throws SQLException {
		return instance.getDate(columnLabel);
	}

	/** {@inheritDoc} */
	@Override
	public Time getTime(String columnLabel) throws SQLException {
		return instance.getTime(columnLabel);
	}

	/** {@inheritDoc} */
	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return instance.getTimestamp(columnLabel);
	}

	/**
	 * <p>getAsciiStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.io.InputStream} object.
	 * @throws java.sql.SQLException if any.
	 */
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		return instance.getAsciiStream(columnLabel);
	}

	/**
	 * <p>getUnicodeStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.io.InputStream} object.
	 * @throws java.sql.SQLException if any.
	 */
	@SuppressWarnings("deprecation")
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		return instance.getUnicodeStream(columnLabel);
	}

	/** {@inheritDoc} */
	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return instance.getBinaryStream(columnLabel);
	}

	/**
	 * <p>getWarnings.</p>
	 *
	 * @return a {@link java.sql.SQLWarning} object.
	 * @throws java.sql.SQLException if any.
	 */
	public SQLWarning getWarnings() throws SQLException {
		return instance.getWarnings();
	}

	/**
	 * <p>clearWarnings.</p>
	 *
	 * @throws java.sql.SQLException if any.
	 */
	public void clearWarnings() throws SQLException {
		instance.clearWarnings();
	}

	/**
	 * <p>getCursorName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 * @throws java.sql.SQLException if any.
	 */
	public String getCursorName() throws SQLException {
		return instance.getCursorName();
	}

	/**
	 * <p>getMetaData.</p>
	 *
	 * @return a {@link java.sql.ResultSetMetaData} object.
	 * @throws java.sql.SQLException if any.
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return instance.getMetaData();
	}

	/**
	 * <p>getObject.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Object getObject(int columnIndex) throws SQLException {
		return instance.getObject(columnIndex);
	}

	/**
	 * <p>getObject.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Object getObject(String columnLabel) throws SQLException {
		return instance.getObject(columnLabel);
	}

	/**
	 * <p>findColumn.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	public int findColumn(String columnLabel) throws SQLException {
		return instance.findColumn(columnLabel);
	}

	/**
	 * <p>getCharacterStream.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.io.Reader} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return instance.getCharacterStream(columnIndex);
	}

	/**
	 * <p>getCharacterStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.io.Reader} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return instance.getCharacterStream(columnLabel);
	}

	/**
	 * <p>getBigDecimal.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.math.BigDecimal} object.
	 * @throws java.sql.SQLException if any.
	 */
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return instance.getBigDecimal(columnIndex);
	}

	/**
	 * <p>getBigDecimal.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.math.BigDecimal} object.
	 * @throws java.sql.SQLException if any.
	 */
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return instance.getBigDecimal(columnLabel);
	}

	/**
	 * <p>isBeforeFirst.</p>
	 *
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean isBeforeFirst() throws SQLException {
		return instance.isBeforeFirst();
	}

	/**
	 * <p>isAfterLast.</p>
	 *
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean isAfterLast() throws SQLException {
		return instance.isAfterLast();
	}

	/**
	 * <p>isFirst.</p>
	 *
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean isFirst() throws SQLException {
		return instance.isFirst();
	}

	/**
	 * <p>isLast.</p>
	 *
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean isLast() throws SQLException {
		return instance.isLast();
	}

	/**
	 * <p>beforeFirst.</p>
	 *
	 * @throws java.sql.SQLException if any.
	 */
	public void beforeFirst() throws SQLException {
		instance.beforeFirst();
	}

	/**
	 * <p>afterLast.</p>
	 *
	 * @throws java.sql.SQLException if any.
	 */
	public void afterLast() throws SQLException {
		instance.afterLast();
	}

	/**
	 * <p>first.</p>
	 *
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean first() throws SQLException {
		return instance.first();
	}

	/**
	 * <p>last.</p>
	 *
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean last() throws SQLException {
		return instance.last();
	}

	/**
	 * <p>getRow.</p>
	 *
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	public int getRow() throws SQLException {
		return instance.getRow();
	}

	/**
	 * <p>absolute.</p>
	 *
	 * @param row a int.
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean absolute(int row) throws SQLException {
		return instance.absolute(row);
	}

	/**
	 * <p>relative.</p>
	 *
	 * @param rows a int.
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean relative(int rows) throws SQLException {
		return instance.relative(rows);
	}

	/**
	 * <p>previous.</p>
	 *
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean previous() throws SQLException {
		return instance.previous();
	}

	/**
	 * <p>setFetchDirection.</p>
	 *
	 * @param direction a int.
	 * @throws java.sql.SQLException if any.
	 */
	public void setFetchDirection(int direction) throws SQLException {
		instance.setFetchDirection(direction);
	}

	/**
	 * <p>getFetchDirection.</p>
	 *
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	public int getFetchDirection() throws SQLException {
		return instance.getFetchDirection();
	}

	/**
	 * <p>setFetchSize.</p>
	 *
	 * @param rows a int.
	 * @throws java.sql.SQLException if any.
	 */
	public void setFetchSize(int rows) throws SQLException {
		instance.setFetchSize(rows);
	}

	/**
	 * <p>getFetchSize.</p>
	 *
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	public int getFetchSize() throws SQLException {
		return instance.getFetchSize();
	}

	/**
	 * <p>getType.</p>
	 *
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	public int getType() throws SQLException {
		return instance.getType();
	}

	/**
	 * <p>getConcurrency.</p>
	 *
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	public int getConcurrency() throws SQLException {
		return instance.getConcurrency();
	}

	/**
	 * <p>rowUpdated.</p>
	 *
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean rowUpdated() throws SQLException {
		return instance.rowUpdated();
	}

	/**
	 * <p>rowInserted.</p>
	 *
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean rowInserted() throws SQLException {
		return instance.rowInserted();
	}

	/**
	 * <p>rowDeleted.</p>
	 *
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean rowDeleted() throws SQLException {
		return instance.rowDeleted();
	}

	/**
	 * <p>updateNull.</p>
	 *
	 * @param columnIndex a int.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateNull(int columnIndex) throws SQLException {
		instance.updateNull(columnIndex);
	}

	/**
	 * <p>updateBoolean.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		instance.updateBoolean(columnIndex, x);
	}

	/**
	 * <p>updateByte.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a byte.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateByte(int columnIndex, byte x) throws SQLException {
		instance.updateByte(columnIndex, x);
	}

	/**
	 * <p>updateShort.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a short.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateShort(int columnIndex, short x) throws SQLException {
		instance.updateShort(columnIndex, x);
	}

	/**
	 * <p>updateInt.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a int.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateInt(int columnIndex, int x) throws SQLException {
		instance.updateInt(columnIndex, x);
	}

	/**
	 * <p>updateLong.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateLong(int columnIndex, long x) throws SQLException {
		instance.updateLong(columnIndex, x);
	}

	/**
	 * <p>updateFloat.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a float.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateFloat(int columnIndex, float x) throws SQLException {
		instance.updateFloat(columnIndex, x);
	}

	/**
	 * <p>updateDouble.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a double.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateDouble(int columnIndex, double x) throws SQLException {
		instance.updateDouble(columnIndex, x);
	}

	/**
	 * <p>updateBigDecimal.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.math.BigDecimal} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		instance.updateBigDecimal(columnIndex, x);
	}

	/**
	 * <p>updateString.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.lang.String} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateString(int columnIndex, String x) throws SQLException {
		instance.updateString(columnIndex, x);
	}

	/**
	 * <p>updateBytes.</p>
	 *
	 * @param columnIndex a int.
	 * @param x an array of byte.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		instance.updateBytes(columnIndex, x);
	}

	/**
	 * <p>updateDate.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.sql.Date} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateDate(int columnIndex, Date x) throws SQLException {
		instance.updateDate(columnIndex, x);
	}

	/**
	 * <p>updateTime.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.sql.Time} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateTime(int columnIndex, Time x) throws SQLException {
		instance.updateTime(columnIndex, x);
	}

	/**
	 * <p>updateTimestamp.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.sql.Timestamp} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		instance.updateTimestamp(columnIndex, x);
	}

	/**
	 * <p>updateAsciiStream.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.io.InputStream} object.
	 * @param length a int.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		instance.updateAsciiStream(columnIndex, x, length);
	}

	/**
	 * <p>updateBinaryStream.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.io.InputStream} object.
	 * @param length a int.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		instance.updateBinaryStream(columnIndex, x, length);
	}

	/**
	 * <p>updateCharacterStream.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.io.Reader} object.
	 * @param length a int.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		instance.updateCharacterStream(columnIndex, x, length);
	}

	/**
	 * <p>updateObject.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.lang.Object} object.
	 * @param scaleOrLength a int.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateObject(int columnIndex, Object x, int scaleOrLength)
			throws SQLException {
		instance.updateObject(columnIndex, x, scaleOrLength);
	}

	/**
	 * <p>updateObject.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.lang.Object} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateObject(int columnIndex, Object x) throws SQLException {
		instance.updateObject(columnIndex, x);
	}

	/**
	 * <p>updateNull.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateNull(String columnLabel) throws SQLException {
		instance.updateNull(columnLabel);
	}

	/**
	 * <p>updateBoolean.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBoolean(String columnLabel, boolean x)
			throws SQLException {
		instance.updateBoolean(columnLabel, x);
	}

	/**
	 * <p>updateByte.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a byte.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateByte(String columnLabel, byte x) throws SQLException {
		instance.updateByte(columnLabel, x);
	}

	/**
	 * <p>updateShort.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a short.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateShort(String columnLabel, short x) throws SQLException {
		instance.updateShort(columnLabel, x);
	}

	/**
	 * <p>updateInt.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a int.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateInt(String columnLabel, int x) throws SQLException {
		instance.updateInt(columnLabel, x);
	}

	/**
	 * <p>updateLong.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateLong(String columnLabel, long x) throws SQLException {
		instance.updateLong(columnLabel, x);
	}

	/**
	 * <p>updateFloat.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a float.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateFloat(String columnLabel, float x) throws SQLException {
		instance.updateFloat(columnLabel, x);
	}

	/**
	 * <p>updateDouble.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a double.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateDouble(String columnLabel, double x) throws SQLException {
		instance.updateDouble(columnLabel, x);
	}

	/**
	 * <p>updateBigDecimal.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.math.BigDecimal} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBigDecimal(String columnLabel, BigDecimal x)
			throws SQLException {
		instance.updateBigDecimal(columnLabel, x);
	}

	/**
	 * <p>updateString.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.lang.String} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateString(String columnLabel, String x) throws SQLException {
		instance.updateString(columnLabel, x);
	}

	/**
	 * <p>updateBytes.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x an array of byte.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		instance.updateBytes(columnLabel, x);
	}

	/**
	 * <p>updateDate.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.sql.Date} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateDate(String columnLabel, Date x) throws SQLException {
		instance.updateDate(columnLabel, x);
	}

	/**
	 * <p>updateTime.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.sql.Time} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateTime(String columnLabel, Time x) throws SQLException {
		instance.updateTime(columnLabel, x);
	}

	/**
	 * <p>updateTimestamp.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.sql.Timestamp} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateTimestamp(String columnLabel, Timestamp x)
			throws SQLException {
		instance.updateTimestamp(columnLabel, x);
	}

	/**
	 * <p>updateAsciiStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.io.InputStream} object.
	 * @param length a int.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateAsciiStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		instance.updateAsciiStream(columnLabel, x, length);
	}

	/**
	 * <p>updateBinaryStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.io.InputStream} object.
	 * @param length a int.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBinaryStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		instance.updateBinaryStream(columnLabel, x, length);
	}

	/**
	 * <p>updateCharacterStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param reader a {@link java.io.Reader} object.
	 * @param length a int.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateCharacterStream(String columnLabel, Reader reader,
			int length) throws SQLException {
		instance.updateCharacterStream(columnLabel, reader, length);
	}

	/**
	 * <p>updateObject.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.lang.Object} object.
	 * @param scaleOrLength a int.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateObject(String columnLabel, Object x, int scaleOrLength)
			throws SQLException {
		instance.updateObject(columnLabel, x, scaleOrLength);
	}

	/**
	 * <p>updateObject.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.lang.Object} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateObject(String columnLabel, Object x) throws SQLException {
		instance.updateObject(columnLabel, x);
	}

	/**
	 * <p>insertRow.</p>
	 *
	 * @throws java.sql.SQLException if any.
	 */
	public void insertRow() throws SQLException {
		instance.insertRow();
	}

	/**
	 * <p>updateRow.</p>
	 *
	 * @throws java.sql.SQLException if any.
	 */
	public void updateRow() throws SQLException {
		instance.updateRow();
	}

	/**
	 * <p>deleteRow.</p>
	 *
	 * @throws java.sql.SQLException if any.
	 */
	public void deleteRow() throws SQLException {
		instance.deleteRow();
	}

	/**
	 * <p>refreshRow.</p>
	 *
	 * @throws java.sql.SQLException if any.
	 */
	public void refreshRow() throws SQLException {
		instance.refreshRow();
	}

	/**
	 * <p>cancelRowUpdates.</p>
	 *
	 * @throws java.sql.SQLException if any.
	 */
	public void cancelRowUpdates() throws SQLException {
		instance.cancelRowUpdates();
	}

	/**
	 * <p>moveToInsertRow.</p>
	 *
	 * @throws java.sql.SQLException if any.
	 */
	public void moveToInsertRow() throws SQLException {
		instance.moveToInsertRow();
	}

	/**
	 * <p>moveToCurrentRow.</p>
	 *
	 * @throws java.sql.SQLException if any.
	 */
	public void moveToCurrentRow() throws SQLException {
		instance.moveToCurrentRow();
	}

	/**
	 * <p>getStatement.</p>
	 *
	 * @return a {@link java.sql.Statement} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Statement getStatement() throws SQLException {
		return instance.getStatement();
	}

	/**
	 * <p>getObject.</p>
	 *
	 * @param columnIndex a int.
	 * @param map a {@link java.util.Map} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Object getObject(int columnIndex, Map<String, Class<?>> map)
			throws SQLException {
		return instance.getObject(columnIndex, map);
	}

	/**
	 * <p>getRef.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.sql.Ref} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Ref getRef(int columnIndex) throws SQLException {
		return instance.getRef(columnIndex);
	}

	/**
	 * <p>getBlob.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.sql.Blob} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Blob getBlob(int columnIndex) throws SQLException {
		return instance.getBlob(columnIndex);
	}

	/**
	 * <p>getClob.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.sql.Clob} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Clob getClob(int columnIndex) throws SQLException {
		return instance.getClob(columnIndex);
	}

	/**
	 * <p>getArray.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.sql.Array} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Array getArray(int columnIndex) throws SQLException {
		return instance.getArray(columnIndex);
	}

	/**
	 * <p>getObject.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param map a {@link java.util.Map} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Object getObject(String columnLabel, Map<String, Class<?>> map)
			throws SQLException {
		return instance.getObject(columnLabel, map);
	}

	/**
	 * <p>getRef.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.sql.Ref} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Ref getRef(String columnLabel) throws SQLException {
		return instance.getRef(columnLabel);
	}

	/**
	 * <p>getBlob.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.sql.Blob} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Blob getBlob(String columnLabel) throws SQLException {
		return instance.getBlob(columnLabel);
	}

	/**
	 * <p>getClob.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.sql.Clob} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Clob getClob(String columnLabel) throws SQLException {
		return instance.getClob(columnLabel);
	}

	/**
	 * <p>getArray.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.sql.Array} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Array getArray(String columnLabel) throws SQLException {
		return instance.getArray(columnLabel);
	}

	/**
	 * <p>getDate.</p>
	 *
	 * @param columnIndex a int.
	 * @param cal a {@link java.util.Calendar} object.
	 * @return a {@link java.sql.Date} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return instance.getDate(columnIndex, cal);
	}

	/**
	 * <p>getDate.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param cal a {@link java.util.Calendar} object.
	 * @return a {@link java.sql.Date} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		return instance.getDate(columnLabel, cal);
	}

	/**
	 * <p>getTime.</p>
	 *
	 * @param columnIndex a int.
	 * @param cal a {@link java.util.Calendar} object.
	 * @return a {@link java.sql.Time} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return instance.getTime(columnIndex, cal);
	}

	/**
	 * <p>getTime.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param cal a {@link java.util.Calendar} object.
	 * @return a {@link java.sql.Time} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		return instance.getTime(columnLabel, cal);
	}

	/**
	 * <p>getTimestamp.</p>
	 *
	 * @param columnIndex a int.
	 * @param cal a {@link java.util.Calendar} object.
	 * @return a {@link java.sql.Timestamp} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		return instance.getTimestamp(columnIndex, cal);
	}

	/**
	 * <p>getTimestamp.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param cal a {@link java.util.Calendar} object.
	 * @return a {@link java.sql.Timestamp} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Timestamp getTimestamp(String columnLabel, Calendar cal)
			throws SQLException {
		return instance.getTimestamp(columnLabel, cal);
	}

	/**
	 * <p>getURL.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.net.URL} object.
	 * @throws java.sql.SQLException if any.
	 */
	public URL getURL(int columnIndex) throws SQLException {
		return instance.getURL(columnIndex);
	}

	/**
	 * <p>getURL.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.net.URL} object.
	 * @throws java.sql.SQLException if any.
	 */
	public URL getURL(String columnLabel) throws SQLException {
		return instance.getURL(columnLabel);
	}

	/**
	 * <p>updateRef.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.sql.Ref} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		instance.updateRef(columnIndex, x);
	}

	/**
	 * <p>updateRef.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.sql.Ref} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		instance.updateRef(columnLabel, x);
	}

	/**
	 * <p>updateBlob.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.sql.Blob} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		instance.updateBlob(columnIndex, x);
	}

	/**
	 * <p>updateBlob.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.sql.Blob} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		instance.updateBlob(columnLabel, x);
	}

	/**
	 * <p>updateClob.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.sql.Clob} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		instance.updateClob(columnIndex, x);
	}

	/**
	 * <p>updateClob.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.sql.Clob} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		instance.updateClob(columnLabel, x);
	}

	/**
	 * <p>updateArray.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.sql.Array} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateArray(int columnIndex, Array x) throws SQLException {
		instance.updateArray(columnIndex, x);
	}

	/**
	 * <p>updateArray.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.sql.Array} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateArray(String columnLabel, Array x) throws SQLException {
		instance.updateArray(columnLabel, x);
	}

	/**
	 * <p>getRowId.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.sql.RowId} object.
	 * @throws java.sql.SQLException if any.
	 */
	public RowId getRowId(int columnIndex) throws SQLException {
		return instance.getRowId(columnIndex);
	}

	/**
	 * <p>getRowId.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.sql.RowId} object.
	 * @throws java.sql.SQLException if any.
	 */
	public RowId getRowId(String columnLabel) throws SQLException {
		return instance.getRowId(columnLabel);
	}

	/**
	 * <p>updateRowId.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.sql.RowId} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		instance.updateRowId(columnIndex, x);
	}

	/**
	 * <p>updateRowId.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.sql.RowId} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		instance.updateRowId(columnLabel, x);
	}

	/**
	 * <p>getHoldability.</p>
	 *
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	public int getHoldability() throws SQLException {
		return instance.getHoldability();
	}

	/**
	 * <p>isClosed.</p>
	 *
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	public boolean isClosed() throws SQLException {
		return instance.isClosed();
	}

	/**
	 * <p>updateNString.</p>
	 *
	 * @param columnIndex a int.
	 * @param nString a {@link java.lang.String} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateNString(int columnIndex, String nString)
			throws SQLException {
		instance.updateNString(columnIndex, nString);
	}

	/**
	 * <p>updateNString.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param nString a {@link java.lang.String} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateNString(String columnLabel, String nString)
			throws SQLException {
		instance.updateNString(columnLabel, nString);
	}

	/**
	 * <p>updateNClob.</p>
	 *
	 * @param columnIndex a int.
	 * @param nClob a {@link java.sql.NClob} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		instance.updateNClob(columnIndex, nClob);
	}

	/**
	 * <p>updateNClob.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param nClob a {@link java.sql.NClob} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateNClob(String columnLabel, NClob nClob)
			throws SQLException {
		instance.updateNClob(columnLabel, nClob);
	}

	/**
	 * <p>getNClob.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.sql.NClob} object.
	 * @throws java.sql.SQLException if any.
	 */
	public NClob getNClob(int columnIndex) throws SQLException {
		return instance.getNClob(columnIndex);
	}

	/**
	 * <p>getNClob.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.sql.NClob} object.
	 * @throws java.sql.SQLException if any.
	 */
	public NClob getNClob(String columnLabel) throws SQLException {
		return instance.getNClob(columnLabel);
	}

	/**
	 * <p>getSQLXML.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.sql.SQLXML} object.
	 * @throws java.sql.SQLException if any.
	 */
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return instance.getSQLXML(columnIndex);
	}

	/**
	 * <p>getSQLXML.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.sql.SQLXML} object.
	 * @throws java.sql.SQLException if any.
	 */
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return instance.getSQLXML(columnLabel);
	}

	/**
	 * <p>updateSQLXML.</p>
	 *
	 * @param columnIndex a int.
	 * @param xmlObject a {@link java.sql.SQLXML} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		instance.updateSQLXML(columnIndex, xmlObject);
	}

	/**
	 * <p>updateSQLXML.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param xmlObject a {@link java.sql.SQLXML} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
			throws SQLException {
		instance.updateSQLXML(columnLabel, xmlObject);
	}

	/**
	 * <p>getNString.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.lang.String} object.
	 * @throws java.sql.SQLException if any.
	 */
	public String getNString(int columnIndex) throws SQLException {
		return instance.getNString(columnIndex);
	}

	/**
	 * <p>getNString.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 * @throws java.sql.SQLException if any.
	 */
	public String getNString(String columnLabel) throws SQLException {
		return instance.getNString(columnLabel);
	}

	/**
	 * <p>getNCharacterStream.</p>
	 *
	 * @param columnIndex a int.
	 * @return a {@link java.io.Reader} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return instance.getNCharacterStream(columnIndex);
	}

	/**
	 * <p>getNCharacterStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.io.Reader} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return instance.getNCharacterStream(columnLabel);
	}

	/**
	 * <p>updateNCharacterStream.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.io.Reader} object.
	 * @param length a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		instance.updateNCharacterStream(columnIndex, x, length);
	}

	/**
	 * <p>updateNCharacterStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param reader a {@link java.io.Reader} object.
	 * @param length a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		instance.updateNCharacterStream(columnLabel, reader, length);
	}

	/**
	 * <p>updateAsciiStream.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.io.InputStream} object.
	 * @param length a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		instance.updateAsciiStream(columnIndex, x, length);
	}

	/**
	 * <p>updateBinaryStream.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.io.InputStream} object.
	 * @param length a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		instance.updateBinaryStream(columnIndex, x, length);
	}

	/**
	 * <p>updateCharacterStream.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.io.Reader} object.
	 * @param length a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		instance.updateCharacterStream(columnIndex, x, length);
	}

	/**
	 * <p>updateAsciiStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.io.InputStream} object.
	 * @param length a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		instance.updateAsciiStream(columnLabel, x, length);
	}

	/**
	 * <p>updateBinaryStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.io.InputStream} object.
	 * @param length a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		instance.updateBinaryStream(columnLabel, x, length);
	}

	/**
	 * <p>updateCharacterStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param reader a {@link java.io.Reader} object.
	 * @param length a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		instance.updateCharacterStream(columnLabel, reader, length);
	}

	/**
	 * <p>updateBlob.</p>
	 *
	 * @param columnIndex a int.
	 * @param inputStream a {@link java.io.InputStream} object.
	 * @param length a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBlob(int columnIndex, InputStream inputStream, long length)
			throws SQLException {
		instance.updateBlob(columnIndex, inputStream, length);
	}

	/**
	 * <p>updateBlob.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param inputStream a {@link java.io.InputStream} object.
	 * @param length a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBlob(String columnLabel, InputStream inputStream,
			long length) throws SQLException {
		instance.updateBlob(columnLabel, inputStream, length);
	}

	/**
	 * <p>updateClob.</p>
	 *
	 * @param columnIndex a int.
	 * @param reader a {@link java.io.Reader} object.
	 * @param length a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		instance.updateClob(columnIndex, reader, length);
	}

	/**
	 * <p>updateClob.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param reader a {@link java.io.Reader} object.
	 * @param length a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		instance.updateClob(columnLabel, reader, length);
	}

	/**
	 * <p>updateNClob.</p>
	 *
	 * @param columnIndex a int.
	 * @param reader a {@link java.io.Reader} object.
	 * @param length a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		instance.updateNClob(columnIndex, reader, length);
	}

	/**
	 * <p>updateNClob.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param reader a {@link java.io.Reader} object.
	 * @param length a long.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		instance.updateNClob(columnLabel, reader, length);
	}

	/**
	 * <p>updateNCharacterStream.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.io.Reader} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateNCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		instance.updateNCharacterStream(columnIndex, x);
	}

	/**
	 * <p>updateNCharacterStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param reader a {@link java.io.Reader} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		instance.updateNCharacterStream(columnLabel, reader);
	}

	/**
	 * <p>updateAsciiStream.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.io.InputStream} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		instance.updateAsciiStream(columnIndex, x);
	}

	/**
	 * <p>updateBinaryStream.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.io.InputStream} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		instance.updateBinaryStream(columnIndex, x);
	}

	/**
	 * <p>updateCharacterStream.</p>
	 *
	 * @param columnIndex a int.
	 * @param x a {@link java.io.Reader} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		instance.updateCharacterStream(columnIndex, x);
	}

	/**
	 * <p>updateAsciiStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.io.InputStream} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		instance.updateAsciiStream(columnLabel, x);
	}

	/**
	 * <p>updateBinaryStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param x a {@link java.io.InputStream} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		instance.updateBinaryStream(columnLabel, x);
	}

	/**
	 * <p>updateCharacterStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param reader a {@link java.io.Reader} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		instance.updateCharacterStream(columnLabel, reader);
	}

	/**
	 * <p>updateBlob.</p>
	 *
	 * @param columnIndex a int.
	 * @param inputStream a {@link java.io.InputStream} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBlob(int columnIndex, InputStream inputStream)
			throws SQLException {
		instance.updateBlob(columnIndex, inputStream);
	}

	/**
	 * <p>updateBlob.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param inputStream a {@link java.io.InputStream} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateBlob(String columnLabel, InputStream inputStream)
			throws SQLException {
		instance.updateBlob(columnLabel, inputStream);
	}

	/**
	 * <p>updateClob.</p>
	 *
	 * @param columnIndex a int.
	 * @param reader a {@link java.io.Reader} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		instance.updateClob(columnIndex, reader);
	}

	/**
	 * <p>updateClob.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param reader a {@link java.io.Reader} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateClob(String columnLabel, Reader reader)
			throws SQLException {
		instance.updateClob(columnLabel, reader);
	}

	/**
	 * <p>updateNClob.</p>
	 *
	 * @param columnIndex a int.
	 * @param reader a {@link java.io.Reader} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		instance.updateNClob(columnIndex, reader);
	}

	/**
	 * <p>updateNClob.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @param reader a {@link java.io.Reader} object.
	 * @throws java.sql.SQLException if any.
	 */
	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		instance.updateNClob(columnLabel, reader);
	}

	/**
	 * <p>getConnection.</p>
	 *
	 * @return a {@link de.mhus.lib.sql.DbConnection} object.
	 */
	public DbConnection getConnection() {
		return sth.getConnection();
	}

	/** {@inheritDoc} */
	@Override
	public MDate getMDate(String columnLabel) throws Exception {
		return new MDate(getTimestamp(columnLabel));
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getColumnNames() throws Exception {
		if (columnNames == null) {
			LinkedList<String> l = new LinkedList<String>();
			ResultSetMetaData meta = instance.getMetaData();
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				String name = meta.getColumnName(i);
				l.add(name);
			}
			columnNames = Collections.unmodifiableList(l);
		}
		return columnNames;
	}


}
