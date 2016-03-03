package de.mhus.lib.annotations.adb;

/**
 * <p>DbType class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DbType {

	public static enum TYPE {INT,LONG,BOOL,DOUBLE,FLOAT,STRING,DATETIME,UUID,BLOB, UNKNOWN}

	private TYPE type;
	private int size;
	private String[] options;

	/**
	 * <p>Constructor for DbType.</p>
	 *
	 * @param type a {@link de.mhus.lib.annotations.adb.DbType.TYPE} object.
	 * @param size a int.
	 * @param options an array of {@link java.lang.String} objects.
	 */
	public DbType(TYPE type, int size, String[] options) {
		this.type = type;
		this.size = size;
		this.options = options;
	}
	
	/**
	 * <p>Getter for the field <code>type</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.annotations.adb.DbType.TYPE} object.
	 */
	public TYPE getType() {
		return type;
	}

	/**
	 * <p>Getter for the field <code>size</code>.</p>
	 *
	 * @return a int.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * <p>Getter for the field <code>options</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getOptions() {
		return options;
	}
	
	/**
	 * Return the option corresponding to the index or the def value if this is not possible.
	 *
	 * @param index index of the option
	 * @param def default value
	 * @return a {@link java.lang.String} object.
	 */
	public String getOption(int index, String def) {
		if (options == null || index < 0 || index >= options.length)
			return def;
		return options[index];
	}
	
}
