package de.mhus.lib.core.strategy.util;

import java.util.Map;

import de.mhus.lib.core.lang.Value;

public class MapValue extends Value<Map<?,?>> {

    private static final long serialVersionUID = 1L;

    public MapValue(Map<?,?> value) {
        super(value);
    }

}
