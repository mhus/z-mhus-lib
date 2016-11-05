package de.mhus.lib.cao.aspect;

import de.mhus.lib.cao.CaoAspectFactory;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.CaoPolicy;

public interface CaoPolicyAspectFactory extends CaoAspectFactory<CaoPolicy>{

	@Override
	CaoPolicy getAspectFor(CaoNode node);
	
}
