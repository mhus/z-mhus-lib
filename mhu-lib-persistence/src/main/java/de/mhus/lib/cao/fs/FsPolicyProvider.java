package de.mhus.lib.cao.fs;

import de.mhus.lib.cao.CaoException;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.CaoPolicy;
import de.mhus.lib.cao.CaoPolicyProvider;

public class FsPolicyProvider implements CaoPolicyProvider {

	@Override
	public CaoPolicy getAccessPolicy(CaoNode node) {
		try {
			return new CaoPolicy(node, true, node.isEditable());
		} catch (CaoException e) {
		}
		return null;
	}

}
