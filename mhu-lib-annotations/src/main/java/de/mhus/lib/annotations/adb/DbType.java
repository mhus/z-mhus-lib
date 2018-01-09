package de.mhus.lib.annotations.adb;

public class DbType {

	public static enum TYPE {INT,LONG,BOOL,DOUBLE,FLOAT,STRING,DATETIME,UUID,BLOB, UNKNOWN}

	private TYPE type;
	private int size;
	private String[] options;

	public DbType(TYPE type, int size, String[] options) {
		this.type = type;
		this.size = size;
		this.options = options;
	}
	
	public TYPE getType() {
		return type;
	}

	public int getSize() {
		return size;
	}

	public String[] getOptions() {
		return options;
	}
	
	/**
	 * Return the option corresponding to the index or the def value if this is not possible.
	 * 
	 * @param index index of the option
	 * @param def default value
	 * @return option
	 */
	public String getOption(int index, String def) {
		if (options == null || index < 0 || index >= options.length)
			return def;
		return options[index];
	}
	
}
