package de.mhus.lib.core.lang;

public interface Adaptable<T> {

	<I extends T> I adaptTo(Class<? extends T> ifc);
}
