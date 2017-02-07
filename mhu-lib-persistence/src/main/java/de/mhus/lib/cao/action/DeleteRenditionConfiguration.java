package de.mhus.lib.cao.action;

import de.mhus.lib.cao.CaoList;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.form.definition.FmText;

public class DeleteRenditionConfiguration extends CaoConfiguration {

	public DeleteRenditionConfiguration(CaoConfiguration con, CaoList list, IConfig model) {
		super(con, list, model);
	}


	public static final String RENDITION = "rendition";
	
	@Override
	protected IConfig createDefaultModel() {
		return new DefRoot(
				new FmText(RENDITION, "rendition.name=Rendition", "rendition.description=Name of the rendition to delete")
				);
	}


	public void setRendition(String name) {
		properties.setString(RENDITION, name);
	}
	
}
