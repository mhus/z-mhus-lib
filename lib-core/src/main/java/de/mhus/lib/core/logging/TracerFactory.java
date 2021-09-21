package de.mhus.lib.core.logging;

import io.opentracing.Tracer;

public interface TracerFactory {

    Tracer create();

}
