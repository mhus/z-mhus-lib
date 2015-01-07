package de.mhus.lib.sql;

import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import de.mhus.lib.core.MDate;


public abstract class DbResult {

	public abstract void close() throws Exception;
	
	public abstract String getString(String columnLabel) throws Exception;

	public abstract boolean next() throws Exception;

	public abstract InputStream getBinaryStream(String columnLabel) throws Exception;
	
	public abstract boolean getBoolean(String columnLabel) throws Exception;
	
	public abstract int getInt(String columnLabel) throws Exception;
	
	public abstract long getLong(String columnLabel) throws Exception;
	
	public abstract float getFloat(String columnLabel) throws Exception;
	
	public abstract double getDouble(String columnLabel) throws Exception;
	
	public abstract Date getDate(String columnLabel) throws Exception;

	public abstract MDate getMDate(String columnLabel) throws Exception;

	public abstract Time getTime(String columnLabel) throws Exception;

	public abstract Timestamp getTimestamp(String columnLabel) throws Exception;

	public abstract List<String> getColumnNames() throws Exception;
	
}
