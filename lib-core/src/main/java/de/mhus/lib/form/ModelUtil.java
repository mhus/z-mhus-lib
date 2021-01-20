/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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
package de.mhus.lib.form;

import java.util.Map;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MJson;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.errors.MException;

public class ModelUtil {

    private static Log log = Log.getLog(ModelUtil.class);

    public static DefRoot toModel(Element xml) {
        DefRoot root = new DefRoot();

        NamedNodeMap attrList = xml.getAttributes();
        for (int i = 0; i < attrList.getLength(); i++) {
            Node attrXml = attrList.item(i);
            root.addAttribute(attrXml.getNodeName(), xml.getAttribute(attrXml.getNodeName()));
        }

        toConfig(root, xml);
        return root;
    }

    private static void toConfig(DefComponent node, Element xml) {
        for (Element element : MXml.getLocalElementIterator(xml)) {
            DefComponent nextNode = new DefComponent(element.getNodeName());

            NamedNodeMap attrList = element.getAttributes();
            for (int i = 0; i < attrList.getLength(); i++) {
                Node attrXml = attrList.item(i);
                nextNode.addAttribute(
                        attrXml.getNodeName(), element.getAttribute(attrXml.getNodeName()));
            }
            node.addDefinition(nextNode);

            toConfig(nextNode, element);
        }
    }

    public static Document toXml(IConfig model) {

        try {
            Document doc = MXml.createDocument();
            Element rootXml = doc.createElement("root");
            doc.appendChild(rootXml);

            toXml(model, rootXml);

            return doc;
        } catch (Throwable t) {
            log.d(t);
        }
        return null;
    }

    private static void toXml(IConfig node, Element xml) throws DOMException, MException {
        for (String key : node.getPropertyKeys()) xml.setAttribute(key, node.getString(key, ""));

        for (IConfig next : node.getObjects()) {
            Element nextXml = xml.getOwnerDocument().createElement(next.getName());
            xml.appendChild(nextXml);
            toXml(next, nextXml);
        }
    }

    public static ObjectNode toJson(IConfig model) {
        try {
            ObjectNode root = MJson.createObjectNode();
            toJson(model, root);

            return root;
        } catch (Throwable t) {
            log.d(t);
        }
        return null;
    }

    private static void toJson(IConfig node, ObjectNode json) throws DOMException, MException {
        for (String key : node.getPropertyKeys()) json.put(key, node.getString(key, ""));

        for (IConfig next : node.getObjects()) {
            ObjectNode nextJson = MJson.createObjectNode();
            toJson(next, nextJson);
            json.set(next.getName(), nextJson);
        }
    }

    public static DefRoot toModel(ObjectNode json) {
        DefRoot root = new DefRoot();
        toModel(json, root);
        return root;
    }

    private static void toModel(ObjectNode json, DefComponent root) {

        for (Map.Entry<String, JsonNode> field : M.iterate(json.fields())) {
            if (field.getValue().isValueNode()) {
                root.addAttribute(field.getKey(), field.getValue().asText());
            } else if (field.getValue().isObject()) {
                DefComponent nextNode = new DefComponent(field.getKey());
                ObjectNode nextJson = (ObjectNode) field.getValue();
                toModel(nextJson, nextNode);
            }
        }
    }
}
