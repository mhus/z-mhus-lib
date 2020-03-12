/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.json;

import org.codehaus.jackson.JsonNode;

import de.mhus.lib.core.MJson;
import de.mhus.lib.errors.NotSupportedException;

public class JacksonTransformer extends TransformStrategy {

    @Override
    public Object jsonToPojo(JsonNode node, Class<?> type, TransformHelper helper)
            throws NotSupportedException {

        try {
            return MJson.getMapper().readValue(node, type);
        } catch (Exception e) {
            throw new NotSupportedException(e);
        }
    }

    @Override
    public JsonNode pojoToJson(Object obj, TransformHelper helper) throws NotSupportedException {
        JsonNode x;
        try {
            x = MJson.load(MJson.getMapper().writeValueAsString(obj));
        } catch (Exception e) {
            throw new NotSupportedException(e);
        }
        return x;
    }
}
