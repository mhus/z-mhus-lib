package de.mhus.lib.cao.action;

import java.io.File;
import java.util.Locale;

import de.mhus.lib.cao.CaoList;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.form.ComponentAdapterProvider;
import de.mhus.lib.form.definition.FmText;

public class UploadRenditionConfiguration extends CaoConfiguration {

	public UploadRenditionConfiguration(CaoConfiguration con, CaoList list, IConfig model) {
		super(con, list, model);
	}


	public static final String RENDITION = "rendition";
	public static final String FILE = "file";
	
	@Override
	protected IConfig createDefaultModel() {
		return new DefRoot(
				new FmText(RENDITION, "rendition.name=Rendition", "rendition.description=Name of the rendition to delete"),
				new FmText(FILE, "file.name=File", "file.description=Path to the file to import")
				);
	}


	public void setRendition(String name) {
		properties.setString(RENDITION, name);
	}

	public void setFile(String path) {
		properties.setString(FILE, path);
	}
	
	public void setFile(File file) {
		setFile(file.getAbsolutePath());
	}
	
}
