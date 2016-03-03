package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefDefinition;
import de.mhus.lib.core.definition.IDefTransformer;
import de.mhus.lib.errors.MException;

/**
 * <p>FmTransformSetDefaultSources class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FmTransformSetDefaultSources implements IDefTransformer {

	private String dbPackage = null;
	private String memPackage = null;
	
	/**
	 * <p>Constructor for FmTransformSetDefaultSources.</p>
	 */
	public FmTransformSetDefaultSources() {
		this(null,null);
	}
	
	/**
	 * <p>Constructor for FmTransformSetDefaultSources.</p>
	 *
	 * @param memPackage a {@link java.lang.String} object.
	 * @param dbPackage a {@link java.lang.String} object.
	 */
	public FmTransformSetDefaultSources(String memPackage, String dbPackage) {
		this.dbPackage = dbPackage;
		this.memPackage = memPackage;
	}
	
	/** {@inheritDoc} */
	@Override
	public IDefDefinition transform(IDefDefinition component) throws MException {

		if (component instanceof FmElement) {
			boolean isSource = false;
			for (IDefDefinition def : ((FmElement)component).definitions()) {
				if (def instanceof FmSource) {
					isSource = true;
					break;
				}
			}
			if (!isSource)
				((FmElement)component).addDefinition(new FmDefaultSources(memPackage,dbPackage));
		}
		
		if (component instanceof DefComponent) {
			for ( IDefDefinition def : ((DefComponent)component).definitions()) {
				transform(def);
			}
		}
		
		
		return component;
	}

}
