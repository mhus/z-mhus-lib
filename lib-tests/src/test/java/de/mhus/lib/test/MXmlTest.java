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
package de.mhus.lib.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import de.mhus.lib.core.MXml;
import de.mhus.lib.tests.TestCase;

public class MXmlTest extends TestCase {

    static final String
            // prepared the xml to exactly be reproduced
            plain =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                    + "<!--\n"
                    + "\n"
                    + "    Copyright 2018 Mike Hummel\n"
                    + "\n"
                    + "    Licensed under the Apache License, Version 2.0 (the \"License\");\n"
                    + "    you may not use this file except in compliance with the License.\n"
                    + "    You may obtain a copy of the License at\n"
                    + "\n"
                    + "        http://www.apache.org/licenses/LICENSE-2.0\n"
                    + "\n"
                    + "    Unless required by applicable law or agreed to in writing, software\n"
                    + "    distributed under the License is distributed on an \"AS IS\" BASIS,\n"
                    + "    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
                    + "    See the License for the specific language governing permissions and\n"
                    + "    limitations under the License.\n"
                    + "\n"
                    + "-->"
                    + "<blueprint xmlns=\"http://www.osgi.org/xmlns/blueprint/v1.0.0\">\n"
                    + "\n"
                    + "    <bean class=\"de.mhus.osgi.commands.db.DelegateDataSource\" id=\"dataSource\">\n"
                    + "        <property name=\"source\" value=\"jdbc/db_vault\"/>\n"
                    + "        <property name=\"context\" ref=\"blueprintBundleContext\"/>\n"
                    + "    </bean>\n"
                    + "    <!-- Comment -->\n"
                    + "    <service interface=\"javax.sql.DataSource\" ref=\"dataSource\">\n"
                    + "        <service-properties>\n"
                    + "            <entry key=\"osgi.jndi.service.name\" value=\"db_sop\"/>\n"
                    + "        </service-properties>\n"
                    + "    </service>\n"
                    + "</blueprint>";

    @Test
    public void testLoadAndSaveSeamless() throws Exception {

        Document doc = MXml.loadXml(plain);

        String created = MXml.toString(doc, false);

        System.out.println(created);
        assertEquals(plain.trim(), created.trim());
    }

    @Test
    public void testLoad() throws ParserConfigurationException, SAXException, IOException {

        Document doc = MXml.loadXml(plain);
        assertNotNull(doc);
        assertNotNull(doc.getDocumentElement());
        assertNotNull(doc.getDocumentElement().getElementsByTagName("bean"));
    }

    @Test
    public void testEntityDisabled() throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        try {
            Document doc =
                    MXml.loadXml(
                            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                                    + "<!DOCTYPE rdf:RDF[\n"
                                    + "<!ENTITY wiki 'http://example.org/index.php'>\n"
                                    + "]>"
                                    + "<test><rdf wiki=\"&wiki;\"></rdf></test>");
            assertNotNull(doc);
            System.out.println(MXml.toString(doc, true));
            String val = MXml.getAttributeValue(doc.getDocumentElement(), "/rdf@wiki", null);
            System.out.println(val);
            // assertNotEquals("http://example.org/index.php", val);
        } catch (SAXParseException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().contains("DOCTYPE is disallowed"));
        }
    }

    @Test
    public void testEntityExpansion() throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        try {
            Document doc =
                    MXml.loadXml(
                            "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
                                    + "<!DOCTYPE lolz [\n"
                                    + "  <!ENTITY lol \"lollollollollollollol[...]\">\n"
                                    + "  <!ENTITY lol2 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n"
                                    + "  <!ENTITY lol3 \"&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;\">\n"
                                    + "  <!ENTITY lol4 \"&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;\">\n"
                                    + "  <!ENTITY lol5 \"&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;\">\n"
                                    + "  <!ENTITY lol6 \"&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;\">\n"
                                    + "]>\n"
                                    + "<Quote> \n"
                                    + "<fName>FIRST NAME &lol6;</fName> \n"
                                    + "</Quote>");

            System.out.println(MXml.toString(doc, true));
        } catch (org.xml.sax.SAXParseException e) {
            System.out.println(e.getMessage());
            //            assertTrue(e.getMessage().contains("more than \"1\" entity expansions"));
            assertTrue(e.getMessage().contains("DOCTYPE is disallowed"));
            // fine !!!
        }
    }
}
