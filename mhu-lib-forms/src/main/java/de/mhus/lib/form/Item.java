package de.mhus.lib.form;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;

public class Item {

	private String key;
	private String caption;
	private MNlsProvider provider;
	private String parent;

	public Item(String parent, String key, String caption) {
		this.key = key;
		this.caption = caption;
		this.parent = parent;
	}
	
	public Item(String key, String caption) {
		this.key = key;
		this.caption = caption;
	}
	
	public void setNlsProvider(MNlsProvider provider) {
		this.provider = provider;
	}
	
	public String toString() {
		return MNls.find(provider, caption);
	}
	
	public String getKey() {
		return key;
	}
	
	public String getParent() {
		return parent;
	}
	
	public boolean equals(Object in) {
		if (in instanceof Item)
			return MSystem.equals( ((Item)in).getKey(), key );
		return key.equals(in);
	}
}
