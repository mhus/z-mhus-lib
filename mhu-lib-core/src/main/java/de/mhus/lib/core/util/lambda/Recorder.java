package de.mhus.lib.core.util.lambda;

/**
 * Adapted from https://github.com/benjiman/benjiql
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class Recorder<T> {

    private T t;
    private RecordingObject recorder;

    /**
     * <p>Constructor for Recorder.</p>
     *
     * @param t a T object.
     * @param recorder a {@link de.mhus.lib.core.util.lambda.RecordingObject} object.
     */
    public Recorder(T t, RecordingObject recorder) {
        this.t = t;
        this.recorder = recorder;
    }

    /**
     * <p>getCurrentMethodName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCurrentMethodName() {
        return recorder.getCurrentMethodName();
    }

    /**
     * <p>getObject.</p>
     *
     * @return a T object.
     */
    public T getObject() {
        return t;
    }
}
