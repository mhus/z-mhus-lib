package de.mhus.lib.annotations.lang;

@FunctionalInterface
public interface Consumer2<One, Two> {
    public void accept(One one, Two two);
}
