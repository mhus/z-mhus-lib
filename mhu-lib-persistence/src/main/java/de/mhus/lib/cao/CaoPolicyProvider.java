package de.mhus.lib.cao;

public interface CaoPolicyProvider {

	CaoPolicy getAccessPolicy(CaoNode caoNode);
	
}
