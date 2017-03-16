package de.mhus.lib.core.pojo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.io.MObjectInputStream;

public class ObjectWrapperPojo<T> {

	private T object;
	private byte[] stream;

	
	public ObjectWrapperPojo() {}
	
	public ObjectWrapperPojo(T object) {
		this.object = object;
	}
	
	public T pojoGetObject() throws IOException, ClassNotFoundException {
		return pojoGetObject(MApi.get().createActivator());
	}
	
	@SuppressWarnings("unchecked")
	public synchronized T pojoGetObject(ClassLoader classLoader) throws IOException, ClassNotFoundException {
		if (object == null && stream != null) {
			
			ByteArrayInputStream in = new ByteArrayInputStream(stream);
			MObjectInputStream ois = new MObjectInputStream(in);
			ois.setClassLoader(classLoader);
			object = (T) ois.readObject();
			ois.close();
			
			stream = null;
		}
		return object;
	}

	public void pojoSetObject(T object) {
		this.object = object;
	}
	
	public byte[] getContent() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(object);
		return out.toByteArray();
	}
	
	public void setContent(byte[] stream) {
		this.stream = stream;
		this.object = null;
	}
	
}
