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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MProperties;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.Tracer.SpanBuilder;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;

@DefaultImplementation(DefaultTracer.class)
public interface ITracer {

    public static final String TYPE_HTTP = "http";
    public static final String TYPE_THREAD = "thread";

    /**
     * This will start a completely new span. Current active spans will be closed before creation of
     * the new span. It is always a root span.
     *
     * @param name Name of the activity
     * @param activation
     * @param active Set true to force sampling of the span.
     * @param tagPairs key-value pairs for tags
     * @return The scope
     */
    Scope start(String name, String activation, Object... tagPairs);

    /**
     * Enter a sub span.
     *
     * @param spanName Name of the activity
     * @param tagPairs key-value pairs for tags
     * @return The scope
     */
    Scope enter(String spanName, Object... tagPairs);

    /**
     * Enter a sub span and set the given parent span. If parent is null the behavior is not clear.
     * Could be a unbound span or a sub span from the current.
     *
     * @param parent
     * @param spanName
     * @param tagPairs
     * @return The new Scope
     */
    Scope enter(Span parent, String spanName, Object... tagPairs);

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

    /**
     * Fast access to tracer
     *
     * @return The current tracer
     */
    public static ITracer get() {
        return MApi.lookup(ITracer.class);
    }

    void activate(String activation);

    SpanBuilder createSpan(Span parent, String spanName, Object... tagPairs);

    public static MProperties serialize(SpanContext context) {
        MProperties out = new MProperties();
        get().tracer()
                .inject(
                        context,
                        Format.Builtin.TEXT_MAP,
                        new TextMap() {

                            @Override
                            public Iterator<Entry<String, String>> iterator() {
                                return null;
                            }

                            @Override
                            public void put(String key, String value) {
                                out.put(key, value);
                            }
                        });
        return out;
    }

    public static SpanContext deserialize(IProperties prop) {
        Map<String, String> copy = new HashMap<String, String>();
        prop.forEach((k, v) -> copy.put(k, String.valueOf(v)));
        SpanContext parentSpanCtx =
                ITracer.get()
                        .tracer()
                        .extract(
                                Format.Builtin.TEXT_MAP,
                                new TextMap() {

                                    @Override
                                    public Iterator<Entry<String, String>> iterator() {
                                        return copy.entrySet().iterator();
                                    }

                                    @Override
                                    public void put(String key, String value) {}
                                });
        return parentSpanCtx;
    }

    default void cleanup() {
        try {
            Span cur = current();
            if (cur != null) cur.finish();
        } catch (Throwable t) {
            MApi.dirtyLogDebug(t);
        }
    }

    default String getCurrentId() {
        TraceUberIdMap tracer = new TraceUberIdMap();
        try {
                    tracer()
                    .inject(
                            current().context(),
                            Format.Builtin.TEXT_MAP,
                            tracer);
        } catch (Throwable t2) {
            MLogUtil.log().d(t2);
        }
        return tracer.getId();
    }

}
