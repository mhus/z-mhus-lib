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
package de.mhus.lib.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import de.mhus.lib.core.MXml;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.TooDeepStructuresException;

public class XmlConfigBuilder extends IConfigBuilder {

    @Override
    public IConfig read(InputStream is) throws MException {
        try {
            Document doc = MXml.loadXml(is);
            return readFromElement(doc.getDocumentElement());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new MException(e);
        }
    }

    @Override
    public void write(IConfig config, OutputStream os) throws MException {
        try {
            Document doc = MXml.createDocument("iconfig");
            Element element = doc.getDocumentElement();
            element.setAttributeNS(
                    "http://www.w3.org/2000/xmlns/",
                    "xmlns:config",
                    "http://www.mhus.de/schemas/config.html");
            write(doc, element, config, 0);
            MXml.saveXml(doc, os);
        } catch (ParserConfigurationException
                | TransformerFactoryConfigurationError
                | TransformerException e) {
            throw new MException(e);
        }
    }

    private void write(Document doc, Element element, IConfig config, int level) {
        if (level > 100) throw new TooDeepStructuresException();

        for (String key : config.getPropertyKeys()) {
            if (key.startsWith("config:")) continue;
            element.setAttribute(key, config.getString(key, ""));
        }
        for (String key : config.getObjectKeys()) {
            IConfig itemC = config.getObjectOrNull(key);
            Element itemE = doc.createElement(key);
            element.appendChild(itemE);
            itemE.setAttribute("config:type", "object");
            write(doc, itemE, itemC, level + 1);
        }
        for (String key : config.getArrayKeys()) {
            for (IConfig itemC : config.getArrayOrNull(key)) {
                Element itemE = doc.createElement(key);
                element.appendChild(itemE);
                itemE.setAttribute("config:type", "array");
                write(doc, itemE, itemC, level + 1);
            }
        }
    }

    public IConfig readFromElement(Element element) throws MException {
        // first must be an object
        if (element.hasAttribute("xmlns:config")) element.removeAttribute("xmlns:config");
        IConfig config = new MConfig();
        read(config, element, 0);
        return config;
    }

    private void read(IConfig config, Element element, int level) {

        if (level > 100) throw new TooDeepStructuresException();

        for (String key : MXml.getAttributeNames(element)) {
            if (key.startsWith("config:")) continue;
            config.put(key, element.getAttribute(key));
        }
        for (Element itemE : MXml.getLocalElementIterator(element)) {
            String key = itemE.getTagName();
            if ("value".equals(itemE.getAttribute("config:type"))) {
                String value = MXml.getValue(itemE, false);
                config.put(key, value);
            } else if (config.isArray(key) || "array".equals(itemE.getAttribute("config:type"))) {
                ConfigList arrayC = config.getArrayOrCreate(key);
                IConfig itemC = arrayC.createObject();
                read(itemC, itemE, level + 1);
            } else if (config.isObject(key)
                    && !"object".equals(itemE.getAttribute("config:type"))) {
                IConfig firstC = config.getObjectOrNull(key);
                ConfigList arrayC = config.createArray(key);
                if (firstC != null) arrayC.add(firstC);
                IConfig itemC = arrayC.createObject();
                read(itemC, itemE, level + 1);
            } else {
                IConfig itemC = config.createObject(key);
                read(itemC, itemE, level + 1);
            }
        }
    }
}
