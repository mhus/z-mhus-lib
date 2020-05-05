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
package de.mhus.lib.jms;

import java.util.LinkedList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MJson;
import de.mhus.lib.core.json.SecurityTransformHelper;

public abstract class ServerJsonObject extends ServerJson {

    private SecurityTransformHelper helper = new SecurityTransformHelper(null, log());

    public ServerJsonObject(JmsDestination dest) {
        super(dest);
    }

    public abstract void receivedOneWay(IProperties properties, Object... obj);

    public abstract RequestResult<Object> received(IProperties properties, Object... obj);

    @Override
    public void receivedOneWay(IProperties properties, JsonNode node) {
        try {
            JsonNode array = node.get("array");
            LinkedList<Object> out = new LinkedList<>();
            if (array != null) {
                for (JsonNode item : array) {
                    Object to = MJson.jsonToPojo((ObjectNode) item, helper);
                    out.add(to);
                }
            }
            receivedOneWay(properties, out.toArray(new Object[out.size()]));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public RequestResult<JsonNode> received(IProperties properties, JsonNode node) {
        try {
            //			JsonNode array = node.get("array");
            //			LinkedList<Object> out = new LinkedList<>();
            //			if (array != null) {
            //				for (JsonNode item : array) {
            //					Object to = MJson.jsonToPojo((ObjectNode) item, helper);
            //					out.add(to);
            //				}
            //			}
            //			RequestResult<Object> res = received(properties, out.toArray(new
            // Object[out.size()]));
            Object[] attr = (Object[]) MJson.jsonToPojo(node, helper);

            RequestResult<Object> res = received(properties, attr);
            if (res == null) return null;

            JsonNode to = MJson.pojoToJson(res.getResult(), helper);

            return new RequestResult<JsonNode>(to, res.getProperties());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    public SecurityTransformHelper getHelper() {
        return helper;
    }

    public void setHelper(SecurityTransformHelper helper) {
        this.helper = helper;
    }
}
