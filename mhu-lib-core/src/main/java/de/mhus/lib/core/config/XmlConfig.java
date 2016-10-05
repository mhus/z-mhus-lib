package de.mhus.lib.core.config;

import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.HashSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.directory.WritableResourceNode;
import de.mhus.lib.errors.MException;

/**
 * Implements a configuration loaded from xml structures.
 * 
 * @author mhu
 *
 */
public class XmlConfig extends IConfig {

	// private static Logger log = Logger.getLogger(XmlConfig.class);
	protected Element element;
	protected boolean changed = false;
	protected XmlConfig parent;
	protected String name;
	
	public XmlConfig() {
		try {
			Document doc = MXml.createDocument();
			element = doc.createElement("config");
			if (element!=null) name = element.getNodeName();
			doc.appendChild(element);
		} catch (Exception e) {}
	}
	
	public Document getDocument() {
		return element.getOwnerDocument();
	}
	
	public XmlConfig(Element elementByPath) {
		this(null,elementByPath);
	}
	
	public XmlConfig(XmlConfig parent,Element elementByPath) {
		this.parent = parent;
		element = elementByPath;
		if (element!=null) name = element.getNodeName();
	}

	public void readConfig(Reader file) throws Exception {
		Document config = MXml.loadXml(file);
		file.close();
		if (element!=null) element = config.getDocumentElement();
		name = element.getNodeName();
	}

	@Override
	public WritableResourceNode getNode(String key) {
		Element e = MXml.getElementByPath(element, key);
		if (e == null) return null;
		return new XmlConfig(this, e);
	}

	@Override
	public WritableResourceNode[] getNodes() {
		NodeList list = MXml.getLocalElements(element);
		WritableResourceNode[] out = new WritableResourceNode[list.getLength()];
		for ( int i = 0; i < list.getLength(); i++ )
			out[i] = new XmlConfig(this,(Element)list.item(i));
		return out;		
	}
	
	@Override
	public WritableResourceNode[] getNodes(String key) {
		NodeList list = MXml.getLocalElements(element, key);
		WritableResourceNode[] out = new WritableResourceNode[list.getLength()];
		for ( int i = 0; i < list.getLength(); i++ )
			out[i] = new XmlConfig(this,(Element)list.item(i));
		return out;
	}

	@Override
	public String[] getNodeKeys() {
		NodeList list = MXml.getLocalElements(element);
		HashSet<String> set = new HashSet<String>();
		for ( int i = 0; i < list.getLength(); i++ )
			set.add(list.item(i).getNodeName());
				
		return set.toArray(new String[set.size()]);
	}

	@Override
	public String[] getPropertyKeys() {
		NamedNodeMap list = element.getAttributes();
		String[] out = new String[ list.getLength()];
		for ( int i = 0; i < list.getLength(); i++ )
			out[i] = list.item(i).getNodeName();
		return out;
	}

	@Override
	public void removeProperty(String name) {
		element.removeAttribute(name);
	}

	@Override
	public void setProperty(String key, Object val) {
		element.setAttribute(MXml.normalizeName(key), MCast.objectToString(val) );
	}

	public void writeConfig(Writer configStream) throws Exception {
		writeConfig(configStream,false);
	}
	public void writeConfig(Writer configStream,boolean intend) throws Exception {
		MXml.trim(element);
		MXml.saveXml(element, configStream, intend);
	}
	
	@Override
	public XmlConfig createConfig(String name) {
		Element ele = element.getOwnerDocument().createElement(name);
		element.appendChild(ele);
		return new XmlConfig(this,ele);
	}

	@Override
	public String getProperty(String key) {
		if (element==null || key==null) return null;
		String out = element.getAttribute(key);
		if ( out == null || out.length() == 0 ) return null;
		return out;
	}

	@Override
	public boolean isProperty(String name) {
		return getProperty(name) != null;
	}

	public boolean isConfigChanged() {
		return parent == null ? changed : (changed || parent.isConfigChanged());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int moveConfig(ResourceNode config, int newPos) throws MException {
		if (!(config instanceof XmlConfig)) 
			throw new MException("not a xmlconfig");
		
		if ( ((XmlConfig)config).element.getParentNode() != element)
			throw new MException("not child of this config");

		NodeList list = MXml.getLocalElements(element);
		if (list.getLength() == 1) {
			if (newPos == MOVE_FIRST || newPos == MOVE_LAST || newPos == 0)
				return 0;
			throw new MException("out of range");			
		}

		if (newPos == MOVE_FIRST) {
			element.removeChild(((XmlConfig)config).element);
			list = MXml.getLocalElements(element);
			element.insertBefore(((XmlConfig)config).element, list.item(0));
			return 0;
		}
		if(newPos == MOVE_LAST) {
			element.removeChild(((XmlConfig)config).element);
			element.appendChild(((XmlConfig)config).element);
			return list.getLength()-1;
		}
		if (newPos == MOVE_DOWN) {
			for (int i = 0; i < list.getLength(); i++) {
				Node item = list.item(i);
				if (item == ((XmlConfig)config).element) { 	// find element in children list
					if (i == list.getLength()-1)			// already last one
						throw new MException("out of range");
					Node next = list.item(i+1);
					element.removeChild(((XmlConfig)config).element);
					element.insertBefore(((XmlConfig)config).element, next);
					return i+1;
				}
			}
		}
		if (newPos == MOVE_UP) {
			for (int i = 0; i < list.getLength(); i++) {
				Node item = list.item(i);
				if (item == ((XmlConfig)config).element) { 	// find element in children list
					if (i == 0)								// already first one
						throw new MException("out of range");
					Node before = list.item(i-1);
					element.removeChild(((XmlConfig)config).element);
					element.insertBefore(((XmlConfig)config).element, before);
					return i-1;
				}
			}
		}
		
		if (newPos < 0 || newPos >= list.getLength())
			throw new MException("out of range");			

		Node before = list.item(newPos);
		if (before == ((XmlConfig)config).element) // already on this pos
			return newPos;
		
		element.removeChild(((XmlConfig)config).element);
		element.insertBefore(((XmlConfig)config).element, before);
		
		return newPos;
	}

	@Override
	public void removeConfig(ResourceNode config) throws MException {
		if (!(config instanceof XmlConfig)) 
			throw new MException("not a xmlconfig");
		
		if ( ((XmlConfig)config).element.getParentNode() != element)
			throw new MException("not child of this config");

		element.removeChild(((XmlConfig)config).element);
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public WritableResourceNode getParent() {
		return parent;
	}
	
	public Element getXmlElement() {
		return element;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof XmlConfig) {
			return element.equals(((XmlConfig)obj).element );
		}
		return super.equals(obj);
	}

	@Override
	public InputStream getInputStream(String key) {
		return null;
	}

	@Override
	public URL getUrl() {
		return null;
	}

}
