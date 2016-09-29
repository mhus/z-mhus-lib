package de.mhus.lib.form;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;

/**
 * <p>Item class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class Item {

	private String key;
	private String caption;
	private MNlsProvider provider;
	private String parent;

	/**
	 * <p>Constructor for Item.</p>
	 *
	 * @param parent a {@link java.lang.String} object.
	 * @param key a {@link java.lang.String} object.
	 * @param caption a {@link java.lang.String} object.
	 */
	public Item(String parent, String key, String caption) {
		this.key = key;
		this.caption = caption;
		this.parent = parent;
	}
	
	/**
	 * <p>Constructor for Item.</p>
	 *
	 * @param key a {@link java.lang.String} object.
	 * @param caption a {@link java.lang.String} object.
	 */
	public Item(String key, String caption) {
		this.key = key;
		this.caption = caption;
	}
	
	/**
	 * <p>setNlsProvider.</p>
	 *
	 * @param provider a {@link de.mhus.lib.core.util.MNlsProvider} object.
	 */
	public void setNlsProvider(MNlsProvider provider) {
		this.provider = provider;
	}
	
	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return MNls.find(provider, caption);
	}
	
	/**
	 * <p>Getter for the field <code>key</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * <p>Getter for the field <code>parent</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getParent() {
		return parent;
	}
	
	/** {@inheritDoc} */
	public boolean equals(Object in) {
		if (in instanceof Item)
			return MSystem.equals( ((Item)in).getKey(), key );
		return key.equals(in);
	}
}
