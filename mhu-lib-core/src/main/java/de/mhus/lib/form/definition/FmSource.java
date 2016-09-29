package de.mhus.lib.form.definition;

import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;

/**
 * <p>FmSource class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class FmSource extends HashConfig implements IDefAttribute {

	private String tag;

	/**
	 * <p>Constructor for FmSource.</p>
	 *
	 * @param tag a {@link java.lang.String} object.
	 * @param name a {@link java.lang.String} object.
	 */
	public FmSource(String tag, String name) {
		super(tag, null);
		this.tag = tag;
		setString("name", name);
	}
	
	/** {@inheritDoc} */
	@Override
	public void inject(DefComponent root) throws MException {
		HashConfig sources = (HashConfig) root.getNode("sources");
		if (sources == null) {
			sources = (HashConfig) root.createConfig("sources");
		}
		sources.setConfig(tag, this);
	}

}
