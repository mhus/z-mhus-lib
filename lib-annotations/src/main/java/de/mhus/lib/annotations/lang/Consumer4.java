package de.mhus.lib.annotations.lang;

@FunctionalInterface
public interface Consumer4<One, Two, Three, Four> {
    public void accept(One one, Two two, Three three, Four four);
}
