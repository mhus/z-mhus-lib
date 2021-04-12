package de.mhus.lib.annotations.lang;

@FunctionalInterface
public interface Function4<One, Two, Three, Four, Retour> {
    public Retour apply(One one, Two two, Three three, Four four);
}
