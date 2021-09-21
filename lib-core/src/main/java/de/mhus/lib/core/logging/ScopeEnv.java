package de.mhus.lib.core.logging;

import io.opentracing.Scope;
import io.opentracing.Span;

public class ScopeEnv implements Scope {

    private Scope scope;
    private Span span;
    private boolean finishSpan = true;
    
    public ScopeEnv(Scope scope, Span span) {
        this.scope = scope;
        this.span = span;
    }

    public Scope getScope() {
        return scope;
    }

    public Span getSpan() {
        return span;
    }

    public boolean isFinishSpan() {
        return finishSpan;
    }

    public void setFinishSpan(boolean finishSpan) {
        this.finishSpan = finishSpan;
    }

    @Override
    public void close() {
        try {
            if (finishSpan)
                span.finish();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            scope.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
