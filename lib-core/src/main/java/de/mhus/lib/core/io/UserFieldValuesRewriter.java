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
package de.mhus.lib.core.io;

import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.core.MXml;

/*
<text:user-field-decls>
  <text:user-field-decl office:value-type="string" office:string-value="test" text:name="v2"/>
</text:user-field-decls>
<text:user-field-get text:name="v2">test</text:user-field-get>
 */
public class UserFieldValuesRewriter implements StreamRewriter {

    private IReadProperties values;

    public UserFieldValuesRewriter(IReadProperties values) {
        this.values = values;
    }

    @Override
    public InputStream rewriteContent(String file, InputStream in) throws Exception {

        if (!file.equals(SOfficeConnector.SOFFICE_CONTENT)) return in;

        Document content = MXml.loadXml(in);

        NodeList list = content.getDocumentElement().getElementsByTagName("text:user-field-decl");
        for (int i = 0; i < list.getLength(); i++) {
            Element node = (Element) list.item(i);
            String name = node.getAttribute("text:name");
            String value = values.getString(name, null);
            if (value != null) {
                node.setAttribute("office:string-value", value);
            }
        }

        StreamBuffer buffer = new StreamBuffer();
        MXml.saveXml(content, buffer.getOutputStream());
        return buffer.getInputStream();
    }
}
