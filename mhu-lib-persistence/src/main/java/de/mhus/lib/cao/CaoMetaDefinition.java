package de.mhus.lib.cao;


/**
 * <p>CaoMetaDefinition class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class CaoMetaDefinition {

	public enum TYPE { STRING, BOOLEAN, LONG, DOUBLE, DATETIME, LIST, OBJECT, TEXT, BINARY, ELEMENT }

	private TYPE type;
	private String name;
	private String nls;
	private long size;
	private String[] categories;
	private CaoDriver driver;

	/**
	 * <p>Constructor for CaoMetaDefinition.</p>
	 *
	 * @param meta a {@link de.mhus.lib.cao.CaoMetadata} object.
	 * @param name a {@link java.lang.String} object.
	 * @param type a {@link de.mhus.lib.cao.CaoMetaDefinition.TYPE} object.
	 * @param nls a {@link java.lang.String} object.
	 * @param size a long.
	 * @param categories a {@link java.lang.String} object.
	 */
	public CaoMetaDefinition(CaoMetadata meta, String name, TYPE type, String nls, long size, String ... categories ) {
		this.name = name;
		this.type = type;
		this.nls = nls;
		this.size = size;
		this.categories = categories;
		this.driver = meta.getDriver();
	}

	/**
	 * <p>Getter for the field <code>type</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.cao.CaoMetaDefinition.TYPE} object.
	 */
	public TYPE getType() {
		return type;
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Getter for the field <code>nls</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getNls() {
		return nls;
	}

	/**
	 * <p>Getter for the field <code>size</code>.</p>
	 *
	 * @return a long.
	 */
	public long getSize() {
		return size;
	}

	/**
	 * <p>Getter for the field <code>categories</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getCategories() {
		return categories;
	}

	/**
	 * <p>Getter for the field <code>driver</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.cao.CaoDriver} object.
	 */
	public final CaoDriver getDriver() {
		return driver;
	}

	/**
	 * <p>hasCategory.</p>
	 *
	 * @param category a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean hasCategory(String category) {
		if (category == null || categories == null)
			return false;
		for (String c : categories)
			if (c != null && c.equals(category)) return true;
		return false;
	}

}
