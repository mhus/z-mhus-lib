package de.mhus.lib.form;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import de.mhus.lib.core.MXml;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.definition.IDefDefinition;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

/**
 * <p>ModelUtil class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class ModelUtil {

	/**
	 * <p>toModel.</p>
	 *
	 * @param xml a {@link org.w3c.dom.Element} object.
	 * @return a {@link de.mhus.lib.core.definition.DefRoot} object.
	 */
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
		for ( Element element : MXml.getLocalElementIterator(xml)) {
			DefComponent nextNode = new DefComponent( element.getNodeName() );
			
			NamedNodeMap attrList = element.getAttributes();
			for (int i = 0; i < attrList.getLength(); i++) {
				Node attrXml = attrList.item(i);
				nextNode.addAttribute(attrXml.getNodeName(), element.getAttribute(attrXml.getNodeName()));
			}
			node.addDefinition(nextNode);
			
			toConfig(nextNode, element);
		}
	}

	/**
	 * <p>toXml.</p>
	 *
	 * @param model a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @return a {@link org.w3c.dom.Document} object.
	 */
	public static Document toXml(ResourceNode model) {
		
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

	private static void toXml(ResourceNode node, Element xml) throws DOMException, MException {
		for (String key : node.getPropertyKeys())
			xml.setAttribute(key, node.getString(key, "") );
		
		for (ResourceNode next : node.getNodes()) {
			Element nextXml = xml.getOwnerDocument().createElement(next.getName());
			xml.appendChild(nextXml);
			toXml(next, nextXml);
		}
			
		
	}
	
	
}
