package de.mhus.lib.sql;

import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import de.mhus.lib.core.MDate;


/**
 * <p>Abstract DbResult class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class DbResult {

	/**
	 * <p>close.</p>
	 *
	 * @throws java.lang.Exception if any.
	 */
	public abstract void close() throws Exception;

	/**
	 * <p>getString.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 * @throws java.lang.Exception if any.
	 */
	public abstract String getString(String columnLabel) throws Exception;

	/**
	 * <p>next.</p>
	 *
	 * @return a boolean.
	 * @throws java.lang.Exception if any.
	 */
	public abstract boolean next() throws Exception;

	/**
	 * <p>getBinaryStream.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.io.InputStream} object.
	 * @throws java.lang.Exception if any.
	 */
	public abstract InputStream getBinaryStream(String columnLabel) throws Exception;

	/**
	 * <p>getBoolean.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a boolean.
	 * @throws java.lang.Exception if any.
	 */
	public abstract boolean getBoolean(String columnLabel) throws Exception;

	/**
	 * <p>getInt.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a int.
	 * @throws java.lang.Exception if any.
	 */
	public abstract int getInt(String columnLabel) throws Exception;

	/**
	 * <p>getLong.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a long.
	 * @throws java.lang.Exception if any.
	 */
	public abstract long getLong(String columnLabel) throws Exception;

	/**
	 * <p>getFloat.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a float.
	 * @throws java.lang.Exception if any.
	 */
	public abstract float getFloat(String columnLabel) throws Exception;

	/**
	 * <p>getDouble.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a double.
	 * @throws java.lang.Exception if any.
	 */
	public abstract double getDouble(String columnLabel) throws Exception;

	/**
	 * <p>getDate.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.sql.Date} object.
	 * @throws java.lang.Exception if any.
	 */
	public abstract Date getDate(String columnLabel) throws Exception;

	/**
	 * <p>getMDate.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.core.MDate} object.
	 * @throws java.lang.Exception if any.
	 */
	public abstract MDate getMDate(String columnLabel) throws Exception;

	/**
	 * <p>getTime.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.sql.Time} object.
	 * @throws java.lang.Exception if any.
	 */
	public abstract Time getTime(String columnLabel) throws Exception;

	/**
	 * <p>getTimestamp.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.sql.Timestamp} object.
	 * @throws java.lang.Exception if any.
	 */
	public abstract Timestamp getTimestamp(String columnLabel) throws Exception;

	/**
	 * <p>getColumnNames.</p>
	 *
	 * @return a {@link java.util.List} object.
	 * @throws java.lang.Exception if any.
	 */
	public abstract List<String> getColumnNames() throws Exception;

	/**
	 * <p>getObject.</p>
	 *
	 * @param columnLabel a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 */
	public abstract Object getObject(String columnLabel) throws Exception;

}
