package de.mhus.lib.core;

public interface MRegistry<L> {

	public void register(L listener);
	public void registerWeak(L listener);
	public void unregister(L listener);


}
