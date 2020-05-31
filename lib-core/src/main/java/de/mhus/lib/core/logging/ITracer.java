package de.mhus.lib.core.logging;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;

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
	 * Enter a sub span and set the given parent span.
	 * If parent is null the behavior is not clear. Could
	 * be a unbound span or a sub span from the current.
	 * 
	 * @param parent
	 * @param spanName
	 * @param tagPairs
	 * @return
	 */
	Scope enter(Span parent, String spanName, String ... tagPairs);

	/**
	 * Return the current active span or null if not exists.
	 * 
	 * @return The span or null
	 */
	Span current();

	/**
	 * Return the current tracer. Could return null if no tracer is available.
	 * 
	 * @return The tracer or null
	 */
	Tracer tracer();
		
}
