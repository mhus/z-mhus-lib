package de.mhus.lib.annotations.lang;

@FunctionalInterface
public interface Function5<One, Two, Three, Four, Five, Retour> {
    public Retour apply(One one, Two two, Three three, Four four, Five five);
}
