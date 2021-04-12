package de.mhus.lib.annotations.lang;

@FunctionalInterface
public interface Function2<One, Two, Retour> {
    public Retour apply(One one, Two two);
}
