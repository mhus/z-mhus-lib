package de.mhus.lib.core.lang;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Used to mark a string value as - not a string - example in the SimpleQueryCompiler.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class Raw implements Serializable {

	private static final long serialVersionUID = -7999465299200662577L;

	private String value;

	/**
	 * <p>Constructor for Raw.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 */
	public Raw(String value) {
		this.value = value;
	}
	
	/**
	 * <p>Constructor for Raw.</p>
	 */
	public Raw() {
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return value;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
//		out.writeUTF(value);
		out.writeObject(value);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
//		value = in.readUTF();
		value = (String)in.readObject();
	}

}
