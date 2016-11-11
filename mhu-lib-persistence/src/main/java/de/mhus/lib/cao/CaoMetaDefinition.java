package de.mhus.lib.cao;


public class CaoMetaDefinition implements CaoAspect {

	public enum TYPE { STRING, BOOLEAN, LONG, DOUBLE, DATETIME, LIST, OBJECT, TEXT, BINARY, ELEMENT }

	private TYPE type;
	private String name;
	private String nls;
	private long size;
	private String[] categories;
	private CaoDriver driver;

	public CaoMetaDefinition(CaoMetadata meta, String name, TYPE type, String nls, long size, String ... categories ) {
		this.name = name;
		this.type = type;
		this.nls = nls;
		this.size = size;
		this.categories = categories;
		this.driver = meta.getDriver();
	}

	public TYPE getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getNls() {
		return nls;
	}

	public long getSize() {
		return size;
	}

	public String[] getCategories() {
		return categories;
	}

	public final CaoDriver getDriver() {
		return driver;
	}

	public boolean hasCategory(String category) {
		if (category == null || categories == null)
			return false;
		for (String c : categories)
			if (c != null && c.equals(category)) return true;
		return false;
	}

}
