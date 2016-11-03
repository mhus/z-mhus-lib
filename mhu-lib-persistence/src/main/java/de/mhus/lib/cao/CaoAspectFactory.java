package de.mhus.lib.cao;

public interface CaoAspectFactory<T extends CaoAspect> {

	T getAspectFor(CaoNode node);
}
