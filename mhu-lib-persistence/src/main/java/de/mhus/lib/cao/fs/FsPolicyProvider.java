package de.mhus.lib.cao.fs;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.CaoPolicy;
import de.mhus.lib.cao.aspect.CaoPolicyAspectFactory;
import de.mhus.lib.cao.util.MutableActionList;
import de.mhus.lib.errors.MException;

public class FsPolicyProvider implements CaoPolicyAspectFactory {

	@Override
	public CaoPolicy getAspectFor(CaoNode node) {
		try {
			return new CaoPolicy(node, true, node.isEditable());
		} catch (MException e) {
		}
		return null;
	}

	@Override
	public void doInitialize(CaoConnection caoConnection, MutableActionList actionList) {
	}

}
