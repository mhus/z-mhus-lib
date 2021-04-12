package de.mhus.lib.annotations.lang;

@FunctionalInterface
public interface Function3<One, Two, Three, Retour> {
    public Retour apply(One one, Two two, Three three);
}
