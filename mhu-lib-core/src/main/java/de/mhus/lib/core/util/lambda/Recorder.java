package de.mhus.lib.core.util.lambda;

/**
 * Adapted from https://github.com/benjiman/benjiql
 * 
 * @author mikehummel
 *
 */

public class Recorder<T> {

    private T t;
    private RecordingObject recorder;

    public Recorder(T t, RecordingObject recorder) {
        this.t = t;
        this.recorder = recorder;
    }

    public String getCurrentMethodName() {
        return recorder.getCurrentMethodName();
    }

    public T getObject() {
        return t;
    }
}