/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.logging;

import java.lang.reflect.Field;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.aaa.Aaa;
import de.mhus.lib.core.service.IdentUtil;
import io.opentracing.Scope;
import io.opentracing.ScopeManager;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.Tracer.SpanBuilder;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import io.opentracing.util.ThreadLocalScope;
import io.opentracing.util.ThreadLocalScopeManager;

public class DefaultTracer extends MLog implements ITracer {

    private TracerFactory tracerFactory;
    private Field tlsScopeField;

    @Override
    public Scope start(String spanName, String activation, Object... tagPairs) {
        try {
            SpanBuilder builder = createSpan(null, spanName, tagPairs);
            builder.ignoreActiveSpan();
            Span span = builder.start();
            Scope scope = tracer().scopeManager().activate(span);
            activate(activation);
            return new ScopeEnv(scope, span);
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
            return null;
        }
    }

    @Override
    public void activate(String activation) {
        if (MString.isEmpty(activation)) return;
        try {
            Span span = current();
            if (activation.equals("-")) {
                Tags.SAMPLING_PRIORITY.set(span, 0);
                return;
            }
            Tags.SAMPLING_PRIORITY.set(span, 1);
            int p = activation.indexOf(':');
            if (p >= 0) {
                MProperties options =
                        IProperties.explodeToMProperties(activation.substring(p + 1).split(","));
                {
                    String value = options.getString("level", null);
                    if (value != null) {
                        span.setBaggageItem(MLog.LOG_LEVEL_MAPPING, value);
                    }
                }
                activation = activation.substring(0, p);
            }
            span.setTag("activation", activation);
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
        }
    }

    @Override
    public Scope enter(String spanName, Object... tagPairs) {
        Span parent = current();
        return enter(parent, spanName, tagPairs);
    }

    @Override
    public Scope enter(Span parent, String spanName, Object... tagPairs) {
        try {
            SpanBuilder builder = createSpan(parent, spanName, tagPairs);
            Span span = builder.start();
            Scope scope = tracer().scopeManager().activate(span);
            return new ScopeEnv(scope, span);
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
            return null;
        }
    }

    @Override
    public SpanBuilder createSpan(Span parent, String spanName, Object... tagPairs) {
        try {
            SpanBuilder span = GlobalTracer.get().buildSpan(spanName);
            for (int i = 0; i < tagPairs.length - 1; i = i + 2)
                span.withTag(String.valueOf(tagPairs[i]), MString.toString(tagPairs[i + 1]));
            span.withTag("ident", IdentUtil.getFullIdent());
            span.withTag("principal", Aaa.getPrincipal());
            span.withTag("thread", Thread.currentThread().getId());
            if (parent != null) span.asChildOf(parent);
            return span;
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
            return null;
        }
    }

    @Override
    public Span current() {
        try {
            return GlobalTracer.get().activeSpan();
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
            return null;
        }
    }

    @Override
    public Tracer tracer() {
        try {
            return GlobalTracer.get();
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void reset() {

        Tracer tracer = tracerFactory.create();

        try {
            Field field = GlobalTracer.class.getDeclaredField("tracer");
            if (!field.isAccessible()) field.setAccessible(true);
            field.set(null, tracer);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public Tracer getEncapsulatedTracer() {
        try {
            Field field = GlobalTracer.class.getDeclaredField("tracer");
            if (!field.isAccessible()) field.setAccessible(true);
            return (Tracer) field.get(null);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    @Override
    public void cleanup() {
        try {
            //            Tracer tracer = ITracer.get().tracer();
            //            while (tracer.scopeManager().activeSpan() != null)
            //                tracer.scopeManager().activeSpan().finish();
            // hack-o-mania
            ScopeManager manager = tracer().scopeManager();
            if (manager instanceof ThreadLocalScopeManager) {
                if (tlsScopeField == null
                        || tlsScopeField.getDeclaringClass() != ThreadLocalScopeManager.class) {
                    tlsScopeField = ThreadLocalScopeManager.class.getDeclaredField("tlsScope");
                    if (!tlsScopeField.canAccess(manager)) tlsScopeField.setAccessible(true);
                }
                @SuppressWarnings("unchecked")
                ThreadLocal<ThreadLocalScope> tlsScope =
                        (ThreadLocal<ThreadLocalScope>) tlsScopeField.get(manager);
                ThreadLocalScope current = tlsScope.get();
                if (current != null) {
                    current.close();
                    tlsScope.remove();
                }
            } else {
                Span cur = current();
                if (cur != null) cur.finish();
            }
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
        }
    }

    public TracerFactory getTracerFactory() {
        return tracerFactory;
    }

    public void setTracerFactory(TracerFactory tracerFactory) {
        this.tracerFactory = tracerFactory;
    }

    @Override
    public Scope activate(Span span) {
        Scope scope = tracer().activateSpan(span);
        return new ScopeEnv(scope, span);
    }
}
