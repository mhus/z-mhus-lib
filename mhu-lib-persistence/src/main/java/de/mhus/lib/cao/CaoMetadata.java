package de.mhus.lib.cao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>CaoMetadata class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class CaoMetadata implements Iterable<CaoMetaDefinition>{

	protected LinkedList<CaoMetaDefinition> definition = new LinkedList<CaoMetaDefinition>();
	protected HashMap<String, CaoMetaDefinition> index = null;

	private CaoDriver driver;

	/**
	 * <p>Constructor for CaoMetadata.</p>
	 *
	 * @param driver a {@link de.mhus.lib.cao.CaoDriver} object.
	 */
	public CaoMetadata(CaoDriver driver) {
		this.driver = driver;
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<CaoMetaDefinition> iterator() {
		return definition.iterator();
	};

	/**
	 * <p>getCount.</p>
	 *
	 * @return a int.
	 */
	public int getCount() {
		return definition.size();
	}

	/**
	 * <p>getDefinitionAt.</p>
	 *
	 * @param index a int.
	 * @return a {@link de.mhus.lib.cao.CaoMetaDefinition} object.
	 */
	public CaoMetaDefinition getDefinitionAt(int index) {
		return definition.get(index);
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
	 * <p>Getter for the field <code>definition</code>.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.cao.CaoMetaDefinition} object.
	 */
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

	/**
	 * <p>getDefinitionsWithCategory.</p>
	 *
	 * @param category a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<CaoMetaDefinition> getDefinitionsWithCategory(String category) {
		LinkedList<CaoMetaDefinition> out = new LinkedList<CaoMetaDefinition>();
		for (CaoMetaDefinition meta : this) {
			if (meta.hasCategory(category))
				out.add(meta);
		}
		return out;
	}

}
