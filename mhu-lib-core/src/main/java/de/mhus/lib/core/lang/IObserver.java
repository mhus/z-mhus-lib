package de.mhus.lib.core.lang;

/**
 * Use this interface if you need a fast interface in inner structures. Do not use it in official APIs.
 * The interface replace the java Observer interface since it is be deprecated in jdk9. The implementation
 * is more lazy then the strict Observer - Observable implementation from java.
 * 
 * @author mikehummel
 *
 * @param <T>
 */
public interface IObserver<T> {

    /**
     * Called if an something happens.
     * 
     * @param source
     * @param reason 
     * @param arg
     */
    void update(Object source, Object reason, T arg);
}
