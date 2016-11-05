package de.mhus.lib.cao;

public interface CaoPolicyProvider extends CaoAspectFactory<CaoPolicy>{

	@Override
	CaoPolicy getAspectFor(CaoNode node);
	
}
