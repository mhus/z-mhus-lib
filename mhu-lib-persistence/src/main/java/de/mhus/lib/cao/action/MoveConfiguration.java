package de.mhus.lib.cao.action;

import java.util.Locale;

import de.mhus.lib.cao.CaoList;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.form.ComponentAdapterProvider;
import de.mhus.lib.form.definition.FmText;

public class MoveConfiguration extends CaoConfiguration {

	public MoveConfiguration(CaoConfiguration con, CaoList list, IConfig model) {
		super(con, list, model);
	}

	public static final String NEW_PARENT = "parent";
	

	public void setNewParent(CaoNode parent) {
		properties.put(NEW_PARENT, parent);
	}

	@Override
	protected IConfig createDefaultModel() {
		return new DefRoot(
//				new FmText(, "name.name=Name", "name.description=New technical name of the node.")
				);
	}
	
}
