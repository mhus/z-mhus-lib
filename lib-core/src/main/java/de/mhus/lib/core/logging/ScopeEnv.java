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
            if (scope != null) scope.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        scope = null;
        try {
            if (span != null && finishSpan) span.finish();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        span = null;
    }
}
