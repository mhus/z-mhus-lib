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
package de.mhus.lib.form;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import de.mhus.lib.core.MXml;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

public class ModelUtil {

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

    public static Document toXml(ResourceNode<?> model) {

        try {
            Document doc = MXml.createDocument();
            Element rootXml = doc.createElement("root");
            doc.appendChild(rootXml);

            toXml(model, rootXml);

            return doc;
        } catch (Throwable t) {

        }
        return null;
    }

    private static void toXml(ResourceNode<?> node, Element xml) throws DOMException, MException {
        for (String key : node.getPropertyKeys()) xml.setAttribute(key, node.getString(key, ""));

        for (ResourceNode<?> next : node.getNodes()) {
            Element nextXml = xml.getOwnerDocument().createElement(next.getName());
            xml.appendChild(nextXml);
            toXml(next, nextXml);
        }
    }
}
