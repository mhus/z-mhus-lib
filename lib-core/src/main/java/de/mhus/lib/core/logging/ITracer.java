package de.mhus.lib.core.logging;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import io.opentracing.Scope;

@DefaultImplementation(DummyTracer.class)
public interface ITracer {

	Scope start(String name, boolean active, String ... tagPairs);
	
	Scope enter(String spanName, String ... tagPairs);

	
}
