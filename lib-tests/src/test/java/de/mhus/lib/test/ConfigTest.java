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
package de.mhus.lib.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.config.ConfigList;
import de.mhus.lib.core.config.DefaultConfigFactory;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.MConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.tests.TestCase;

public class ConfigTest extends TestCase {

    @Test
    public void testProperties() throws MException {
        {
            IConfig c = new MConfig();
            c.setString("test1", "wow");
            c.setString("test2", "alf");

            derTeschd(c, false);
        }
        {
            IConfig c = new MConfig();
            c.setString("test1", "wow");
            c.setString("test2", "alf");

            // save
            File file = new File("target/config.properties");
            DefaultConfigFactory dcf = new DefaultConfigFactory();
            System.out.println("C1: " + c);
            dcf.write(c, file);

            // read
            IConfig c2 = dcf.read(file);
            System.out.println("C2: " + c2);
            derTeschd(c2, false);
        }
    }

    @Test
    public void testXml() throws Exception {

        String xml =
                "<start test1='wow' test2='alf'><sub test1='wow1' test2='alf1'/><sub test1='wow2' test2='alf2'/><sub test1='wow3' test2='alf3'/></start>";
        {
            Document doc = MXml.loadXml(xml);

            IConfig c = IConfig.readFromXmlString(doc.getDocumentElement());

            derTeschd(c, true);
        }
        {
            Document doc = MXml.loadXml(xml);
            IConfig c = IConfig.readFromXmlString(doc.getDocumentElement());

            // save
            File file = new File("target/config.xml");
            DefaultConfigFactory dcf = new DefaultConfigFactory();
            System.out.println("C1: " + c);
            dcf.write(c, file);

            // read
            IConfig c2 = dcf.read(file);
            System.out.println("C2: " + c2);
            derTeschd(c2, true);
        }
    }

    @Test
    public void testYaml() throws Exception {
        String yaml =
                "test1: wow\n"
                        + "test2: alf\n"
                        + "sub:\n"
                        + "- test1: wow1\n"
                        + "  test2: alf1\n"
                        + "- test1: wow2\n"
                        + "  test2: alf2\n"
                        + "- test1: wow3\n"
                        + "  test2: alf3\n"
                        + "projects:\n"
                        + "- properties:\n"
                        + "    name: name\n"
                        + "    url: http://test.de";

        {
            IConfig c = IConfig.readFromYamlString(yaml);
            derTeschd(c, true);
        }
        {
            IConfig c = IConfig.readFromYamlString(yaml);
            File file = new File("target/config.yaml");
            DefaultConfigFactory dcf = new DefaultConfigFactory();
            System.out.println("C1: " + c);
            dcf.write(c, file);

            // read
            IConfig c2 = dcf.read(file);
            System.out.println("C2: " + c2);
            derTeschd(c2, true);

            // check file
            String content = MFile.readFile(file);
            System.out.println(content);
            assertFalse(content.contains("{"));
        }
    }

    @Test
    public void testJson() throws Exception {

        String json =
                MString.replaceAll(
                        "{'test1':'wow','test2':'alf',"
                                + "'sub': [  "
                                + "{'test1':'wow1','test2':'alf1'} , "
                                + "{'test1':'wow2','test2':'alf2'} , "
                                + "{'test1':'wow3','test2':'alf3'}  "
                                + "] }",
                        "'",
                        "\"");
        {
            IConfig c = IConfig.readFromJsonString(json);
            derTeschd(c, true);
        }
        {
            IConfig c = IConfig.readFromJsonString(json);
            File file = new File("target/config.json");
            DefaultConfigFactory dcf = new DefaultConfigFactory();
            System.out.println("C1: " + c);
            dcf.write(c, file);

            // read
            IConfig c2 = dcf.read(file);
            System.out.println("C2: " + c2);
            derTeschd(c2, true);
        }
    }

    @Test
    public void testHash() throws Exception {

        IConfig c = new MConfig();
        c.setString("test1", "wow");
        c.setString("test2", "alf");
        ConfigList a = c.createArray("sub");
        IConfig s = a.createObject();
        s.setString("test1", "wow1");
        s.setString("test2", "alf1");
        s = a.createObject();
        s.setString("test1", "wow2");
        s.setString("test2", "alf2");
        s = a.createObject();
        s.setString("test1", "wow3");
        s.setString("test2", "alf3");

        derTeschd(c, true);
    }

    //    @Test
    //    public void testClone() throws Exception {
    //
    //        String xml =
    //                "<start test1='wow' test2='alf'><sub test1='wow1' test2='alf1'/><sub
    // test1='wow2' test2='alf2'/><sub test1='wow3' test2='alf3'/></start>";
    //        Document doc = MXml.loadXml(xml);
    //
    //        IConfig src = IConfig.createFromXml(doc.getDocumentElement());
    //
    //        IConfig tar1 = new IConfig();
    //        JsonConfig tar2 = new JsonConfig();
    //        XmlConfig tar3 = new XmlConfig();
    //
    //        builder.cloneConfig(src, tar1);
    //        builder.cloneConfig(src, tar2);
    //        builder.cloneConfig(src, tar3);
    //
    //        derTeschd(src, true);
    //        derTeschd(tar1, true);
    //        derTeschd(tar2, true);
    //        derTeschd(tar3, true);
    //    }

    @Test
    private void derTeschd(IConfig c, boolean testsub) throws MException {
        System.out.println(MSystem.findCallingMethod(3) + ": " + c);
        assertEquals("wow", c.getString("test1", "no"));
        assertEquals("alf", c.getString("test2", "no"));
        assertEquals("no", c.getString("test3", "no"));

        assertNull(c.getObjectOrNull("test4"));

        if (!testsub) return;

        // sub config tests

        Collection<IConfig> list = c.getArray("sub");
        assertEquals(3, list.size());

        Iterator<IConfig> listIter = list.iterator();
        IConfig sub = listIter.next();
        assertEquals("wow1", sub.getString("test1", "no"));
        assertEquals("alf1", sub.getString("test2", "no"));
        assertEquals("no", sub.getString("test3", "no"));

        sub = listIter.next();
        assertEquals("wow2", sub.getString("test1", "no"));
        assertEquals("alf2", sub.getString("test2", "no"));
        assertEquals("no", sub.getString("test3", "no"));

        sub = listIter.next();
        assertEquals("wow3", sub.getString("test1", "no"));
        assertEquals("alf3", sub.getString("test2", "no"));
        assertEquals("no", sub.getString("test3", "no"));

        // change properties

        c.setString("test1", "aloa");
        c.setString("test3", "nix");
        assertEquals("aloa", c.getString("test1", "no"));
        assertEquals("alf", c.getString("test2", "no"));
        assertEquals("nix", c.getString("test3", "no"));

        // change config

        sub = c.createObject("sub");
        sub.setString("test1", "aloa4");
        sub.setString("test2", "alf4");
        assertEquals("aloa4", sub.getString("test1", "no"));
        assertEquals("alf4", sub.getString("test2", "no"));
        assertEquals("no", sub.getString("test3", "no"));

        //		assertEquals( 2, c.moveConfig(sub, WritableResourceNode.MOVE_UP) );
        //		assertEquals( 3, c.moveConfig(sub, WritableResourceNode.MOVE_DOWN) );
        //		assertEquals( 0, c.moveConfig(sub, WritableResourceNode.MOVE_FIRST) );
        //		assertEquals( 3, c.moveConfig(sub, WritableResourceNode.MOVE_LAST) );

    }
}
