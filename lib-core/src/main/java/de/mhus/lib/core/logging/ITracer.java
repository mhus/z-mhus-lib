package de.mhus.lib.core.logging;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import io.opentracing.Scope;
import io.opentracing.Span;

@DefaultImplementation(DummyTracer.class)
public interface ITracer {
	
	public static final String TYPE_HTTP = "http";
	public static final String TYPE_THREAD = "thread";

	/**
	 * This will start a completely new span. Current active spans will be
	 * closed before creation of the new span. It is always a root span.
	 * 
	 * @param name Name of the activity
	 * @param active Set true to force sampling of the span.
	 * @param tagPairs key-value pairs for tags
	 * @return The scope
	 */
	Scope start(String name, boolean active, String ... tagPairs);
	
	/**
	 * Enter a sub span.
	 * 
	 * @param spanName Name of the activity
	 * @param tagPairs key-value pairs for tags
	 * @return The scope
	 */
	Scope enter(String spanName, String ... tagPairs);

	/**
	 * Inject the current span to an object. Supported objects should be
	 * Http Client Request, Thread
	 * 
	 * @param object
	 */
	void inject(String type, Object object);
	
	/**
	 * Extract a new Span from an object.
	 * 
	 * @param object
	 * @return
	 */
	Scope extract(String type, Object object);
	
	Span current();
	
}
