package de.mhus.lib.core.lang;

import java.io.Serializable;

import de.mhus.lib.core.lang.Value;

public class SerializedValue extends Value<Serializable> {

    private static final long serialVersionUID = 1L;

    public SerializedValue(Serializable value) {
        super(value);
    }

}
