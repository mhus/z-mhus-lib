package de.mhus.lib.cao.action;

import java.util.Locale;

import de.mhus.lib.cao.CaoList;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.form.ComponentAdapterProvider;
import de.mhus.lib.form.definition.FmText;

public class UpdateConfiguration extends CaoConfiguration {

	public UpdateConfiguration(CaoConfiguration con, CaoList list, IConfig model) {
		super(con, list, model);
	}

	@Override
	protected IConfig createDefaultModel() {
		return new DefRoot(
				);
	}

}
