package de.mhus.lib.core.logging;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
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
	public Scope start(String spanName, String activation, Object ... tagPairs) {
		SpanBuilder span = createSpan(null, spanName, tagPairs);
		Scope scope = span.ignoreActiveSpan().startActive(true);
		activate(activation);
		return scope;

	}
	
	@Override
	public void activate(String activation) {
		if (MString.isEmpty(activation)) return;
		Span span = current();
		if (activation.equals("-")) {
			Tags.SAMPLING_PRIORITY.set(span, 0);
			return;
		}
		Tags.SAMPLING_PRIORITY.set(span, 1);
		int p = activation.indexOf(':');
		if (p >= 0) {
			MProperties options = IProperties.explodeToMProperties(activation.substring(p+1).split(","));
			{
    			String value = options.getString("level", null);
    			if (value != null) {
    				span.setBaggageItem(MLog.LOG_LEVEL_MAPPING, value);
    			}
			}
			activation = activation.substring(0,p);
		}
		span.setTag("activation", activation);
	}

	@Override
	public Scope enter(String spanName, Object ... tagPairs ) {
		return enter(null, spanName, tagPairs);
	}

	@Override
	public Scope enter(Span parent, String spanName, Object... tagPairs) {
		SpanBuilder span = createSpan(parent, spanName, tagPairs);
		Scope scope = span.startActive(true);
		return scope;
	}
	
	@Override
	public SpanBuilder createSpan(Span parent, String spanName, Object... tagPairs) {
		SpanBuilder span = GlobalTracer.get().buildSpan(spanName);
		for (int i = 0; i < tagPairs.length-1; i=i+2)
			span.withTag(String.valueOf(tagPairs[i]),MString.toString(tagPairs[i+1]));
    	span.withTag("ident", IdentUtil.getFullIdent());
    	span.withTag("pricipal", AccessUtil.getPrincipal());
		if (parent != null)
			span.asChildOf(parent);
		return span;
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
