package de.mhus.lib.core.logging;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.service.IdentUtil;
import de.mhus.lib.core.shiro.AccessUtil;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.Tracer.SpanBuilder;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;

public class DefaultTracer extends MLog implements ITracer {
		
	@Override
	public Scope start(String spanName, boolean active, String ... tagPairs) {
		SpanBuilder span = GlobalTracer.get().buildSpan(spanName);
		for (int i = 0; i < tagPairs.length-1; i=i+2)
			span.withTag(tagPairs[i],tagPairs[i+1]);
    	span.withTag("ident", IdentUtil.getFullIdent());
    	span.withTag("pricipal", AccessUtil.getPrincipal());
    	
		Scope scope = span.ignoreActiveSpan().startActive(true);
		if (active)
			Tags.SAMPLING_PRIORITY.set(scope.span(), 1);
		return scope;

	}

	@Override
	public Scope enter(String spanName, String ... tagPairs ) {
		return enter(null, spanName, tagPairs);
	}

	@Override
	public Scope enter(Span parent, String spanName, String... tagPairs) {
		SpanBuilder span = GlobalTracer.get().buildSpan(spanName);
		for (int i = 0; i < tagPairs.length-1; i=i+2)
			span.withTag(tagPairs[i],tagPairs[i+1]);
    	span.withTag("ident", IdentUtil.getFullIdent());
    	span.withTag("pricipal", AccessUtil.getPrincipal());
		if (parent != null)
			span.asChildOf(parent);
		Scope scope = span.startActive(true);
		return scope;
	}
	
	@Override
	public Span current() {
		return GlobalTracer.get().activeSpan();
	}

	@Override
	public Tracer tracer() {
		return GlobalTracer.get();
	}

}
