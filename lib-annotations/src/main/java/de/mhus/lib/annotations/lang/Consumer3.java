package de.mhus.lib.annotations.lang;

@FunctionalInterface
public interface Consumer3<One, Two, Three> {
    public void accept(One one, Two two, Three three);
}
