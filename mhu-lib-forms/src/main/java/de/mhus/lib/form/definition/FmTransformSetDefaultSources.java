package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefDefinition;
import de.mhus.lib.core.definition.IDefTransformer;
import de.mhus.lib.errors.MException;

public class FmTransformSetDefaultSources implements IDefTransformer {

	private String dbPackage = null;
	private String memPackage = null;
	
	public FmTransformSetDefaultSources() {
		this(null,null);
	}
	
	public FmTransformSetDefaultSources(String memPackage, String dbPackage) {
		this.dbPackage = dbPackage;
		this.memPackage = memPackage;
	}
	
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
