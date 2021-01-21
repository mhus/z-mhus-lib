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
package de.mhus.lib.core.parser;

import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import de.mhus.lib.core.util.MObject;

public class DefaultScriptPart extends MObject implements StringPart {

    private String part;
    ScriptEngineManager manager = new ScriptEngineManager();

    public DefaultScriptPart(String part) {
        if (part.startsWith(">js:")) part = part.substring(4);
        this.part = part;
    }

    @Override
    public void execute(StringBuilder out, Map<String, Object> attributes) {
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            if (attributes != null) {
                Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
                for (Map.Entry<String, Object> entry : attributes.entrySet())
                    bindings.put(entry.getKey(), entry.getValue());
            }
            Object ret = engine.eval(part);
            if (ret != null) {
                out.append(ret);
            }
        } catch (ScriptException e) {
            log().d(e, part);
        }
    }

    @Override
    public void dump(int level, StringBuilder out) {}
}
