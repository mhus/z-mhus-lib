/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.w3c.dom.Document;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.config.ConfigBuilder;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.JsonConfig;
import de.mhus.lib.core.config.PropertiesConfig;
import de.mhus.lib.core.config.XmlConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import junit.framework.TestCase;

public class ConfigTest extends TestCase {

	public void testProperties() throws MException {
		Properties p = new Properties();
		p.setProperty("test1", "wow");
		p.setProperty("test2", "alf");
		PropertiesConfig c = new PropertiesConfig(p);

		derTeschd(c, false);

	}
	
	public void testXml() throws Exception {
		
		String xml = "<start test1='wow' test2='alf'><sub test1='wow1' test2='alf1'/><sub test1='wow2' test2='alf2'/><sub test1='wow3' test2='alf3'/></start>";
		Document doc = MXml.loadXml(xml);
		
		XmlConfig c = new XmlConfig(doc.getDocumentElement());
		
		derTeschd(c, true);

	}
	
	public void testJson() throws Exception {
		
		String json = MString.replaceAll("{'test1':'wow','test2':'alf'," +
				"'sub': [  " +
					"{'test1':'wow1','test2':'alf1'} , " +
					"{'test1':'wow2','test2':'alf2'} , " +
					"{'test1':'wow3','test2':'alf3'}  " +
				"] }", "'", "\"");
		
		JsonConfig c = new JsonConfig(json);
		
		derTeschd(c, true);
	}

	public void testHash() throws Exception {
	
		HashConfig c = new HashConfig();
		c.setString("test1", "wow");
		c.setString("test2", "alf");
		ResourceNode<?> s = c.createConfig("sub");
		s.setString("test1", "wow1");
		s.setString("test2", "alf1");
		s = c.createConfig("sub");
		s.setString("test1", "wow2");
		s.setString("test2", "alf2");
		s = c.createConfig("sub");
		s.setString("test1", "wow3");
		s.setString("test2", "alf3");

		derTeschd(c, true);
	}
	
	public void testClone() throws Exception {
		
		ConfigBuilder builder = new ConfigBuilder();
		
		String xml = "<start test1='wow' test2='alf'><sub test1='wow1' test2='alf1'/><sub test1='wow2' test2='alf2'/><sub test1='wow3' test2='alf3'/></start>";
		Document doc = MXml.loadXml(xml);
		
		XmlConfig src = new XmlConfig(doc.getDocumentElement());

		HashConfig tar1 = new HashConfig();
		JsonConfig tar2 = new JsonConfig();
		XmlConfig  tar3 = new XmlConfig();
		
		builder.cloneConfig(src, tar1);
		builder.cloneConfig(src, tar2);
		builder.cloneConfig(src, tar3);
		
		derTeschd(src, true);
		derTeschd(tar1, true);
		derTeschd(tar2, true);
		derTeschd(tar3, true);
		
		
	}
	
	private void derTeschd(IConfig c, boolean testsub) throws MException {
		
		assertEquals("wow", c.getString("test1", "no") );
		assertEquals("alf", c.getString("test2", "no") );
		assertEquals("no",  c.getString("test3", "no") );

		assertNull(c.getNode("test4"));
		
		if (!testsub) return;
		
		// sub config tests
		
		assertEquals( 1, c.getNodeKeys().size() );
		assertEquals( "sub", c.getNodeKeys().iterator().next() );
		
		Collection<IConfig> list = c.getNodes("sub");
		assertEquals(3, list.size());
		
		Iterator<IConfig> listIter = list.iterator();
		ResourceNode<?> sub = listIter.next();
		assertEquals("wow1", sub.getString("test1", "no") );
		assertEquals("alf1", sub.getString("test2", "no") );
		assertEquals("no",  sub.getString("test3", "no") );
		
		sub = listIter.next();
		assertEquals("wow2", sub.getString("test1", "no") );
		assertEquals("alf2", sub.getString("test2", "no") );
		assertEquals("no",  sub.getString("test3", "no") );
		
		sub = listIter.next();
		assertEquals("wow3", sub.getString("test1", "no") );
		assertEquals("alf3", sub.getString("test2", "no") );
		assertEquals("no",  sub.getString("test3", "no") );
		
		// change properties
		
		c.setProperty("test1", "aloa");
		c.setProperty("test3", "nix");
		assertEquals("aloa", c.getString("test1", "no") );
		assertEquals("alf", c.getString("test2", "no") );
		assertEquals("nix",  c.getString("test3", "no") );

		// change config
		
		sub = c.createConfig("sub");
		sub.setProperty("test1", "aloa4");
		sub.setProperty("test2", "alf4");
		assertEquals("aloa4", sub.getString("test1", "no") );
		assertEquals("alf4", sub.getString("test2", "no") );
		assertEquals("no",  sub.getString("test3", "no") );
		
//		assertEquals( 2, c.moveConfig(sub, WritableResourceNode.MOVE_UP) );
//		assertEquals( 3, c.moveConfig(sub, WritableResourceNode.MOVE_DOWN) );		
//		assertEquals( 0, c.moveConfig(sub, WritableResourceNode.MOVE_FIRST) );
//		assertEquals( 3, c.moveConfig(sub, WritableResourceNode.MOVE_LAST) );
		
	}
}
