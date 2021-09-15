package de.mhus.lib.core.logging;

import de.mhus.lib.core.MLog;
import io.opentracing.ScopeManager;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;

public abstract class TracerProxy extends MLog implements Tracer {

    protected Tracer instance;
    
    protected abstract Tracer create();

    public TracerProxy() {
        instance = create();
    }
    
    public void reset() {
        instance = create();
    }
    
    @Override
    public ScopeManager scopeManager() {
        return instance.scopeManager();
    }

    @Override
    public Span activeSpan() {
        return instance.activeSpan();
    }

    @Override
    public SpanBuilder buildSpan(String operationName) {
        return instance.buildSpan(operationName);
    }

    @Override
    public <C> void inject(SpanContext spanContext, Format<C> format, C carrier) {
        instance.inject(spanContext, format, carrier);
    }

    @Override
    public <C> SpanContext extract(Format<C> format, C carrier) {
        return instance.extract(format, carrier);
    }
    
}
