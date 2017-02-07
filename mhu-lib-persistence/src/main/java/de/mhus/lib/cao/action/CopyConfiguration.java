package de.mhus.lib.cao.action;

import de.mhus.lib.cao.CaoList;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.form.definition.FmCheckbox;

public class CopyConfiguration extends CaoConfiguration {

	public CopyConfiguration(CaoConfiguration con, CaoList list, IConfig model) {
		super(con, list, model);
	}

	public static final String NEW_PARENT = "parent";
	public static final String RECURSIVE = "recursive";
	

	public void setNewParent(CaoNode parent) {
		properties.put(NEW_PARENT, parent);
	}

	public void setRecursive(boolean recursive) {
		properties.setBoolean(RECURSIVE, recursive);
	}
	
	@Override
	protected IConfig createDefaultModel() {
		return new DefRoot(
//				new FmText(, "name.name=Name", "name.description=New technical name of the node.")
				new FmCheckbox(RECURSIVE, "recursive.name=Recursive", "recursive.description=Delete also sub structures")
				);
	}
	
}
