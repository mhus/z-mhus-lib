package de.mhus.lib.core.logging;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.mhus.lib.core.MProperties;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;

public class DummyTracer implements ITracer {
	
	private ThreadLocal<Span> current = new ThreadLocal<>();
	
	@Override
	public Scope start(String spanName, boolean active, String ... tagPairs) {
		Span span = new DummySpan(spanName, null);
		for (int i = 0; i < tagPairs.length-1; i=i+2)
			span.setTag(tagPairs[i], tagPairs[i+1]);
		return new DummyScope(span);
	}

	@Override
	public Scope enter(String spanName, String ... tagPairs ) {
		Span span = new DummySpan(spanName, current.get());
		for (int i = 0; i < tagPairs.length-1; i=i+2)
			span.setTag(tagPairs[i], tagPairs[i+1]);
		return new DummyScope(span);
	}

	@Override
	public Scope enter(Span parent, String spanName, String... tagPairs) {
		Span span = new DummySpan(spanName, parent);
		for (int i = 0; i < tagPairs.length-1; i=i+2)
			span.setTag(tagPairs[i], tagPairs[i+1]);
		return new DummyScope(span);
	}

	private class DummySpan implements Span {

		private String name;
		private DummyContext context;
		private MProperties tags;
		
		public DummySpan(String spanName, Span parent) {
			name = spanName;
			synchronized (current) {
				if (parent != null)
					context = (DummyContext) parent.context();
				else
					context = new DummyContext();
			}
		}

		@Override
		public SpanContext context() {
			return context;
		}

		@Override
		public Span setTag(String key, String value) {
			getTags().setString(key, value);
			return this;
		}

		private MProperties getTags() {
			if (tags == null)
				tags = new MProperties();
			return tags;
		}

		@Override
		public Span setTag(String key, boolean value) {
			getTags().put(key, value);
			return this;
		}

		@Override
		public Span setTag(String key, Number value) {
			getTags().put(key, value);
			return this;
		}

		@Override
		public Span log(Map<String, ?> fields) {
			log(System.currentTimeMillis(), fields);
			return this;
		}

		@Override
		public Span log(long timestampMicroseconds, Map<String, ?> fields) {
			return this;
		}

		@Override
		public Span log(String event) {
			log(System.currentTimeMillis(), event);
			return this;
		}

		@Override
		public Span log(long timestampMicroseconds, String event) {
			return this;
		}

		@Override
		public Span setBaggageItem(String key, String value) {
			context.getBaggage().put(key, value);
			return this;
		}

		@Override
		public String getBaggageItem(String key) {
			return context.getBaggage().get(key);
		}

		@Override
		public Span setOperationName(String operationName) {
			name = operationName;
			return this;
		}

		@Override
		public void finish() {
			finish(System.currentTimeMillis());
		}

		@Override
		public void finish(long finishMicros) {
			
		}
		
		public String toString() {
			return name + (tags == null ? "" : tags);
		}
		
	}
	
	private class DummyContext implements SpanContext {
		
		private HashMap<String,String> baggage = null;
		
		@Override
		public Iterable<Entry<String, String>> baggageItems() {
			return getBaggage().entrySet();
		}

		public synchronized HashMap<String, String> getBaggage() {
			if (baggage == null)
				baggage = new HashMap<>();
			return baggage;
		}
		
	}
	
	private class DummyScope implements Scope {

		private Span span;
		private Span last;

		public DummyScope(Span span) {
			synchronized (current) {
				last = current.get();
				current.set(span);
			}
			this.span = span;
		}

		@Override
		public void close() {
			if (span == null) return;
			synchronized (current) {
				if (current == span)
					current.set(last);
			}
			span.finish();
			span = null;
			last = null;
		}

		@Override
		public Span span() {
			return span;
		}
		
	}
	
	@Override
	public Span current() {
		return current.get();
	}

	@Override
	public Tracer tracer() {
		return null;
	}

}
