package de.mhus.lib.cao.fsdb;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoCore;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.CaoPolicy;
import de.mhus.lib.cao.aspect.CaoPolicyAspectFactory;
import de.mhus.lib.cao.util.MutableActionList;
import de.mhus.lib.errors.MException;

public class FdPolicyProvider implements CaoPolicyAspectFactory {

	private CaoCore core;

	@Override
	public CaoPolicy getAspectFor(CaoNode node) {
		try {
			return new CaoPolicy(core, node, true, node.isEditable());
		} catch (MException e) {
		}
		return null;
	}

	@Override
	public void doInitialize(CaoCore core, MutableActionList actionList) {
		this.core = core;
	}

}
