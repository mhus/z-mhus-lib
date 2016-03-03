package de.mhus.lib.core.pojo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.io.MObjectInputStream;

/**
 * <p>ObjectWrapperPojo class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ObjectWrapperPojo<T> {

	private T object;
	private byte[] stream;

	
	/**
	 * <p>Constructor for ObjectWrapperPojo.</p>
	 */
	public ObjectWrapperPojo() {}
	
	/**
	 * <p>Constructor for ObjectWrapperPojo.</p>
	 *
	 * @param object a T object.
	 */
	public ObjectWrapperPojo(T object) {
		this.object = object;
	}
	
	/**
	 * <p>pojoGetObject.</p>
	 *
	 * @return a T object.
	 * @throws java.io.IOException if any.
	 * @throws java.lang.ClassNotFoundException if any.
	 */
	public T pojoGetObject() throws IOException, ClassNotFoundException {
		return pojoGetObject(MSingleton.get().createActivator());
	}
	
	/**
	 * <p>pojoGetObject.</p>
	 *
	 * @param classLoader a {@link java.lang.ClassLoader} object.
	 * @return a T object.
	 * @throws java.io.IOException if any.
	 * @throws java.lang.ClassNotFoundException if any.
	 */
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

	/**
	 * <p>pojoSetObject.</p>
	 *
	 * @param object a T object.
	 */
	public void pojoSetObject(T object) {
		this.object = object;
	}
	
	/**
	 * <p>getContent.</p>
	 *
	 * @return an array of byte.
	 * @throws java.io.IOException if any.
	 */
	public byte[] getContent() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(object);
		return out.toByteArray();
	}
	
	/**
	 * <p>setContent.</p>
	 *
	 * @param stream an array of byte.
	 */
	public void setContent(byte[] stream) {
		this.stream = stream;
		this.object = null;
	}
	
}
