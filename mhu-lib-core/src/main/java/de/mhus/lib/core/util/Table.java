package de.mhus.lib.core.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.core.logging.MLogUtil;

/**
 * <p>Table class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class Table implements Serializable, Externalizable {

	private static final long serialVersionUID = 1L;

	private String name;
	LinkedList<TableColumn> columns = new LinkedList<>();
	HashMap<String, Integer> columnsIndex = new HashMap<>();
	LinkedList<TableRow> rows = new LinkedList<>();
	
	/**
	 * <p>Constructor for Table.</p>
	 */
	public Table() {
	}
	
	/**
	 * <p>Constructor for Table.</p>
	 *
	 * @param res a {@link java.sql.ResultSet} object.
	 * @throws java.sql.SQLException if any.
	 */
	public Table(ResultSet res) throws SQLException {
		ResultSetMetaData meta = res.getMetaData();
		int count = meta.getColumnCount();
		for (int i = 0; i < count; i++) {
			addHeader(meta.getColumnName(i+1), meta.getColumnTypeName(i+1));
		}
		
		while (res.next()) {
			TableRow row = new TableRow();
			row.setTable(this);
			for (int i = 0; i < count; i++) {
				try {
					row.appendData(res.getObject(i+1));
				} catch (Throwable t) {
					MLogUtil.log().t(t);
					row.appendData((String)null);
				}
			}
			getRows().add(row);
		}
		res.close();
	}
	
	/**
	 * <p>Getter for the field <code>columns</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<TableColumn> getColumns() {
		return columns;
	}
	
	/**
	 * <p>Getter for the field <code>rows</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<TableRow> getRows() {
		return rows;
	}

	/**
	 * <p>addHeader.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param type a {@link java.lang.Class} object.
	 * @return a {@link de.mhus.lib.core.util.TableColumn} object.
	 */
	public TableColumn addHeader(String name, Class<?> type) {
		return addHeader(name, type.getCanonicalName());
	}
	
	/**
	 * <p>addHeader.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param type a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.core.util.TableColumn} object.
	 */
	public TableColumn addHeader(String name, String type) {
		TableColumn col = new TableColumn();
		col.setName(name);
		col.setType(type);
		columnsIndex.put(name, columns.size());
		columns.add(col);
		return col;
	}
	
	/**
	 * <p>addRow.</p>
	 *
	 * @param data a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.core.util.TableRow} object.
	 */
	public TableRow addRow(Object ... data) {
		TableRow row = new TableRow();
		row.setTable(this);
		row.setData(data);
		rows.add(row);
		return row;
	}
	
	private void writeObject(java.io.ObjectOutputStream out)
		     throws IOException {
		
		if (name == null) name="";
		out.writeUTF(name);
		
		out.writeInt(columns.size());
		for (TableColumn col : columns)
			out.writeObject(col);
		
		out.writeInt(rows.size());
		for (TableRow row : rows)
			out.writeObject(row);
	}
	private void readObject(java.io.ObjectInputStream in)
		     throws IOException, ClassNotFoundException {
		
		name = in.readUTF();
		{
			int size = in.readInt();
			//columns.clear();
			columns = new LinkedList<>();
			// columnsIndex.clear();
			columnsIndex = new HashMap<>();
			for (int i = 0; i < size; i++) {
				TableColumn col = (TableColumn) in.readObject();
				columnsIndex.put(col.getName(), columns.size());
				columns.add(col);
			}
		}
		{
			int size = in.readInt();
			// rows.clear();
			rows = new LinkedList<>();
			for (int i = 0; i < size; i++) {
				TableRow row = (TableRow) in.readObject();
				row.setTable(this);
				rows.add(row);
			}
		}
	}
	/*
	 private void readObjectNoData()
	     throws ObjectStreamException {
		 
	 }
	 */

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * <p>getColumnIndex.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a int.
	 */
	public int getColumnIndex(String name) {
		Integer ret = columnsIndex.get(name);
		return ret == null ? -1 : ret;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return columns.toString() + rows.toString();
	}

	/**
	 * <p>getColumnSize.</p>
	 *
	 * @return a int.
	 */
	public int getColumnSize() {
		return columns.size();
	}
	
	/**
	 * <p>getRowSize.</p>
	 *
	 * @return a int.
	 */
	public int getRowSize() {
		return rows.size();
	}

	/** {@inheritDoc} */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		if (name == null) name="";
		out.writeUTF(name);
		
		out.writeInt(columns.size());
		for (TableColumn col : columns)
			out.writeObject(col);
		
		out.writeInt(rows.size());
		for (TableRow row : rows)
			out.writeObject(row);
	}

	/** {@inheritDoc} */
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		name = in.readUTF();
		{
			int size = in.readInt();
			//columns.clear();
			columns = new LinkedList<>();
			// columnsIndex.clear();
			columnsIndex = new HashMap<>();
			for (int i = 0; i < size; i++) {
				TableColumn col = (TableColumn) in.readObject();
				columnsIndex.put(col.getName(), columns.size());
				columns.add(col);
			}
		}
		{
			int size = in.readInt();
			// rows.clear();
			rows = new LinkedList<>();
			for (int i = 0; i < size; i++) {
				TableRow row = (TableRow) in.readObject();
				row.setTable(this);
				rows.add(row);
			}
		}
	}
	
}
