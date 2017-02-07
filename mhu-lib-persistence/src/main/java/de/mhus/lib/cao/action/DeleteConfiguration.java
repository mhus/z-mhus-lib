package de.mhus.lib.cao.action;

import de.mhus.lib.cao.CaoList;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.form.definition.FmCheckbox;

public class DeleteConfiguration extends CaoConfiguration {

	public DeleteConfiguration(CaoConfiguration con, CaoList list, IConfig model) {
		super(con, list, model);
	}


	public static final String RECURSIVE = "recursive";
	
	@Override
	protected IConfig createDefaultModel() {
		return new DefRoot(
				new FmCheckbox(RECURSIVE, "recursive.name=Recursive", "recursive.description=Delete also sub structures")
				);
	}

	public void setRecursive(boolean recursive) {
		properties.setBoolean(RECURSIVE, recursive);
	}
	
}
