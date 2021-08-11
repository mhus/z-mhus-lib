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
package de.mhus.lib.core.node;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import de.mhus.lib.core.MXml;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.TooDeepStructuresException;

public class XmlNodeBuilder extends INodeBuilder {

    @Override
    public INode read(InputStream is) throws MException {
        try {
            Document doc = MXml.loadXml(is);
            return readFromElement(doc.getDocumentElement());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new MException(e);
        }
    }

    @Override
    public void write(INode node, OutputStream os) throws MException {
        try {
            Document doc = MXml.createDocument("inode");
            Element element = doc.getDocumentElement();
            element.setAttributeNS(
                    "http://www.w3.org/2000/xmlns/",
                    "xmlns:node",
                    "http://www.mhus.de/schemas/node.html");
            write(doc, element, node, 0);
            MXml.saveXml(doc, os);
        } catch (ParserConfigurationException
                | TransformerFactoryConfigurationError
                | TransformerException e) {
            throw new MException(e);
        }
    }

    private void write(Document doc, Element element, INode node, int level) {
        if (level > 100) throw new TooDeepStructuresException();

        for (String key : node.getPropertyKeys()) {
            if (key.startsWith("node:")) continue;
            element.setAttribute(key, node.getString(key, ""));
        }
        for (String key : node.getObjectKeys()) {
            INode itemC = node.getObjectOrNull(key);
            Element itemE = doc.createElement(key);
            element.appendChild(itemE);
            itemE.setAttribute("node:type", "object");
            write(doc, itemE, itemC, level + 1);
        }
        for (String key : node.getArrayKeys()) {
            for (INode itemC : node.getArrayOrNull(key)) {
                Element itemE = doc.createElement(key);
                element.appendChild(itemE);
                itemE.setAttribute("node:type", "array");
                write(doc, itemE, itemC, level + 1);
            }
        }
    }

    public INode readFromElement(Element element) throws MException {
        // first must be an object
        if (element.hasAttribute("xmlns:node")) element.removeAttribute("xmlns:node");
        INode node = new MNode();
        read(node, element, 0);
        return node;
    }

    private void read(INode node, Element element, int level) {

        if (level > 100) throw new TooDeepStructuresException();

        for (String key : MXml.getAttributeNames(element)) {
            if (key.startsWith("node:")) continue;
            node.put(key, element.getAttribute(key));
        }
        org.w3c.dom.NodeList children = element.getChildNodes();
        if (children.getLength() == 1) {
            Node first = children.item(0);
            if (first.getNodeType() == Node.TEXT_NODE)
                node.setString(INode.NAMELESS_VALUE, first.getNodeValue());
            else if (first.getNodeType() == Node.CDATA_SECTION_NODE)
                node.setString(INode.NAMELESS_VALUE, first.getNodeValue());
        }
        for (Element itemE : MXml.getLocalElementIterator(element)) {
            String key = itemE.getTagName();
            if ("value".equals(itemE.getAttribute("node:type"))) {
                String value = MXml.getValue(itemE, false);
                node.put(key, value);
            } else if (node.isArray(key) || "array".equals(itemE.getAttribute("node:type"))) {
                NodeList arrayC = node.getArrayOrCreate(key);
                INode itemC = arrayC.createObject();
                read(itemC, itemE, level + 1);
            } else if (node.isObject(key) && !"object".equals(itemE.getAttribute("node:type"))) {
                INode firstC = node.getObjectOrNull(key);
                NodeList arrayC = node.createArray(key);
                if (firstC != null) arrayC.add(firstC);
                INode itemC = arrayC.createObject();
                read(itemC, itemE, level + 1);
            } else {
                INode itemC = node.createObject(key);
                read(itemC, itemE, level + 1);
            }
        }
    }
}
