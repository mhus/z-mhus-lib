package de.mhus.lib.annotations.lang;

@FunctionalInterface
public interface Function1<One, Retour> {
    public Retour apply(One one);
}
