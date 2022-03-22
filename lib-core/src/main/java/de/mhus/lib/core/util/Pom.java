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
package de.mhus.lib.core.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.util.MMaven.Artifact;

public class Pom {
    private File pomFile;
    private Document pomDoc;
    private Element pomE;
    private Element parentE;
    private Artifact artifact;
    private Artifact parent;

    public Pom(File pomFile) throws ParserConfigurationException, SAXException, IOException {
        this.pomFile = pomFile;
        pomDoc = MXml.loadXml(pomFile);
        pomE = pomDoc.getDocumentElement();
        parentE = MXml.getElementByPath(pomE, "parent");

        if (parentE != null) parent = new Artifact(parentE);
        artifact = new Artifact(pomE);
    }

    public File getPomFile() {
        return pomFile;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public Artifact getParent() {
        return parent;
    }

    public IReadProperties getLocalProperties() {
        MProperties out = new MProperties();
        Element propertiesE = MXml.getElementByPath(pomE, "properties");
        for (Element itemE : MXml.getLocalElementIterator(propertiesE)) {
            out.setString(itemE.getNodeName(), MXml.getValue(itemE, false));
        }
        return out;
    }
}
