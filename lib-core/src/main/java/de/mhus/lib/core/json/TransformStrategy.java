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
package de.mhus.lib.core.json;

import com.fasterxml.jackson.databind.JsonNode;

import de.mhus.lib.core.MLog;
import de.mhus.lib.errors.NotSupportedException;

public abstract class TransformStrategy extends MLog {

    public Object jsonToPojo(JsonNode node) throws NotSupportedException {
        return jsonToPojo(node, null, new TransformHelper());
    }

    public Object jsonToPojo(JsonNode node, Class<?> type) throws NotSupportedException {
        return jsonToPojo(node, type, new TransformHelper());
    }

    /**
     * Transform the json into an object, some implementations need a type hint to create the
     * object.
     *
     * @param node The object data
     * @param type The type hint, can also be null. If this is not supportet throw the exception
     * @param helper The helper to create objects
     * @return The object, can also be null if the object originally was null.
     * @throws NotSupportedException Thrown if the object could not be created.
     */
    public abstract Object jsonToPojo(JsonNode node, Class<?> type, TransformHelper helper)
            throws NotSupportedException;

    public JsonNode pojoToJson(Object obj) throws NotSupportedException {
        return pojoToJson(obj, new TransformHelper());
    }

    /**
     * Transform an object into an json representation.
     *
     * @param obj The object to transform
     * @param helper
     * @return The json representation, never return null. Throw the exception if problems occur.
     * @throws NotSupportedException
     */
    public abstract JsonNode pojoToJson(Object obj, TransformHelper helper)
            throws NotSupportedException;
}
