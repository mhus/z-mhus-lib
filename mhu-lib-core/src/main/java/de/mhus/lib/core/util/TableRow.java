package de.mhus.lib.core.util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TableRow {

	LinkedList<Object> data = new LinkedList<>();
	
	public List<Object> getData() {
		return data;
	}
	
	private void writeObject(java.io.ObjectOutputStream out)
		     throws IOException {
		out.writeInt(data.size());
		for (Object d : data)
			out.writeObject(d);
	}
	
	private void readObject(java.io.ObjectInputStream in)
		     throws IOException, ClassNotFoundException {
		int size = in.readInt();
		data.clear();
		for (int i = 0; i < size; i++) {
			Object d = in.readObject();
			data.add(d);
		}
		
	}

	public void appendData(Object ... d) {
		for (Object o : d)
			data.add(o);
	}
	
	public void setData(Object ... d) {
		data.clear();
		for (Object o : d)
			data.add(o);
	}
	
}
