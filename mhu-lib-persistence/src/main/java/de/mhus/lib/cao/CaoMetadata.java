package de.mhus.lib.cao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CaoMetadata implements Iterable<CaoMetaDefinition>{

	protected LinkedList<CaoMetaDefinition> definition = new LinkedList<CaoMetaDefinition>();
	protected HashMap<String, CaoMetaDefinition> index = null;
	
	private CaoDriver driver;
	
	public CaoMetadata(CaoDriver driver) {
		this.driver = driver;
	}
	
	@Override
	public Iterator<CaoMetaDefinition> iterator() {
		return definition.iterator();
	};

	public int getCount() {
		return definition.size();
	}
	
	public CaoMetaDefinition getDefinitionAt(int index) {
		return definition.get(index);
	}
	
	public final CaoDriver getDriver() {
		return driver;
	}

	public CaoMetaDefinition getDefinition(String name) {
		synchronized (this) {
			if (index == null) {
				index = new HashMap<String, CaoMetaDefinition>();
				for (CaoMetaDefinition d : this) {
					index.put(d.getName(), d);
				}
			}
		}
		return index.get(name);
	}

	public List<CaoMetaDefinition> getDefinitionsWithCategory(String category) {
		LinkedList<CaoMetaDefinition> out = new LinkedList<CaoMetaDefinition>();
		for (CaoMetaDefinition meta : this) {
			if (meta.hasCategory(category))
				out.add(meta);
		}
		return out;
	}

}
