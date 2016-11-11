package de.mhus.lib.cao.fdb;

import de.mhus.lib.cao.CaoAspectFactory;
import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoCore;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.CaoPolicy;
import de.mhus.lib.cao.util.MutableActionList;
import de.mhus.lib.errors.MException;

public class FdbPolicyProvider implements CaoAspectFactory<CaoPolicy> {

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
