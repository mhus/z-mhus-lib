package de.mhus.lib.core.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Table implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	LinkedList<TableColumn> columns = new LinkedList<>();
	LinkedList<TableRow> rows = new LinkedList<>();
	
	public List<TableColumn> getColumns() {
		return columns;
	}
	
	public List<TableRow> getRows() {
		return rows;
	}

	public TableColumn addHeader(String name, String type) {
		TableColumn col = new TableColumn();
		col.setName(name);
		col.setType(type);
		columns.add(col);
		return col;
	}
	
	public TableRow addRow(Object ... data) {
		TableRow row = new TableRow();
		row.setData(data);
		rows.add(row);
		return row;
	}
	
	private void writeObject(java.io.ObjectOutputStream out)
		     throws IOException {
		
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
			columns.clear();
			for (int i = 0; i < size; i++) {
				TableColumn col = (TableColumn) in.readObject();
				columns.add(col);
			}
		}
		{
			int size = in.readInt();
			rows.clear();
			for (int i = 0; i < size; i++) {
				TableRow row = (TableRow) in.readObject();
				rows.add(row);
			}
		}
	}
	/*
	 private void readObjectNoData()
	     throws ObjectStreamException {
		 
	 }
	 */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
