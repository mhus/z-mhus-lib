package de.mhus.lib.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.directory.WritableResourceNode;
import de.mhus.lib.errors.MException;

/**
 * <p>JsonConfig class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JsonConfig extends IConfig {

	private ObjectNode node;
	protected String name;
	private WritableResourceNode parent;
	// private int index = -1;
	
	/**
	 * <p>Constructor for JsonConfig.</p>
	 *
	 * @param json a {@link java.lang.String} object.
	 * @throws java.lang.Exception if any.
	 */
	public JsonConfig(String json) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode nodex = mapper.readValue(json, JsonNode.class);
		if (nodex instanceof ObjectNode)
			node = (ObjectNode) nodex;
		else {
			node = ((ArrayNode)nodex).objectNode();
			node.put("default", nodex);
		}
	}
	
	/**
	 * <p>Constructor for JsonConfig.</p>
	 *
	 * @param node a {@link org.codehaus.jackson.node.ObjectNode} object.
	 */
	public JsonConfig(ObjectNode node) {
		this(null,null,node);
	}
	
	/**
	 * <p>Constructor for JsonConfig.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param parent a {@link de.mhus.lib.core.directory.WritableResourceNode} object.
	 * @param node a {@link org.codehaus.jackson.node.ObjectNode} object.
	 */
	public JsonConfig(String name, WritableResourceNode parent, ObjectNode node) {
		this.node = node;
		this.name = name;
		this.parent = parent;
	}

	/**
	 * <p>Constructor for JsonConfig.</p>
	 *
	 * @throws java.lang.Exception if any.
	 */
	public JsonConfig() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		node = (ObjectNode)mapper.readValue("{}", JsonNode.class);
	}

	/**
	 * <p>Constructor for JsonConfig.</p>
	 *
	 * @param name2 a {@link java.lang.String} object.
	 * @param jsonConfig a {@link de.mhus.lib.core.config.JsonConfig} object.
	 * @param textNode a {@link org.codehaus.jackson.node.TextNode} object.
	 * @throws java.lang.Exception if any.
	 */
	public JsonConfig(String name2, JsonConfig jsonConfig, TextNode textNode) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		node = (ObjectNode)mapper.readValue("{}", JsonNode.class);
		node.put("_", textNode);
	}

	/** {@inheritDoc} */
	@Override
	public WritableResourceNode getNode(String name) {
		JsonNode child = node.get(name);
		if (child == null || !child.isArray() || child.size() < 1) return null;
		return new JsonConfig(name, this, (ObjectNode)((ArrayNode)child).get(0));
	}

	/** {@inheritDoc} */
	@Override
	public WritableResourceNode[] getNodes(String name) {
		JsonNode child = node.get(name);
		if (child==null || !child.isArray()) return new WritableResourceNode[0];
		WritableResourceNode[] out = new WritableResourceNode[child. size()];
		for (int i = 0; i < child.size(); i++) {
			JsonNode obj = child.get(i);
			if (obj instanceof ObjectNode) {
				out[i] = new JsonConfig(name, this, (ObjectNode)child.get(i));
			} else
			if (obj instanceof org.codehaus.jackson.node.TextNode) {
				try {
					out[i] = new JsonConfig(name, this, (org.codehaus.jackson.node.TextNode)child.get(i));
				} catch (Exception e) {
					log().d(name,e);
				}
			}
			
		}
		return out;
	}

	/** {@inheritDoc} */
	@Override
	public WritableResourceNode[] getNodes() {
		LinkedList<WritableResourceNode> out = new LinkedList<WritableResourceNode>();
		for ( JsonNode  child : node ) {
			if (child !=null && child.isArray()) {
				for (int i = 0; i < child.size(); i++)
					out.add( new JsonConfig(name, this, (ObjectNode)child.get(i)));
			}
			
		}
		if (out == null || out.size() == 0) return new WritableResourceNode[0];
		return out.toArray(new HashConfig[out.size()]);
	}
	
	/** {@inheritDoc} */
	@Override
	public String[] getNodeKeys() {
		LinkedList<String> out = new LinkedList<String>();
		for (Iterator<String> i = node.getFieldNames(); i.hasNext();) {
			String name = i.next();
			JsonNode child = node.get(name);
			if (child.isArray())
				out.add(name);
		}
		return out.toArray(new String[out.size()]);
	}

	/** {@inheritDoc} */
	@Override
	public Object getProperty(String name) {
		JsonNode child = node.get(name);
		if (child==null) return null;
		return child.getValueAsText();
	}

	/** {@inheritDoc} */
	@Override
	public String[] getPropertyKeys() {
		LinkedList<String> out = new LinkedList<String>();
		for (Iterator<String> i = node.getFieldNames(); i.hasNext();) {
			String name = i.next();
			JsonNode child = node.get(name);
			if (!child.isArray())
				out.add(name);
		}
		return out.toArray(new String[out.size()]);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isProperty(String name) {
		JsonNode child = node.get(name);
		return (child!=null && !child.isArray());
	}

	/** {@inheritDoc} */
	@Override
	public void removeProperty(String name) {
		getNode().remove(name);
	}

	/** {@inheritDoc} */
	@Override
	public void setProperty(String name, Object value) {
		getNode().put(name,MCast.objectToString(value));
	}

	/**
	 * <p>Getter for the field <code>node</code>.</p>
	 *
	 * @return a {@link org.codehaus.jackson.node.ObjectNode} object.
	 */
	public ObjectNode getNode() {
		return node;
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return name;
	}

	/** {@inheritDoc} */
	@Override
	public WritableResourceNode createConfig(String key) throws MException {
		
		// find array node, to append new config
		if (node.get(key) != null && !node.get(key).isArray()) {
			node.remove(key);
		}
		ArrayNode array = (ArrayNode) node.get(key);
		if (array == null) { 					// if not, create one
			array = node.arrayNode();
			node.put(key, array);
			
		}
		
		if (! (array instanceof ArrayNode) ) {
			throw new MException(key + " is not an array");
		}
		
		// create new object node in array
		ObjectNode out = array.objectNode();
		array.add(out);
		
		return new JsonConfig(key, this, out);
	}

	/** {@inheritDoc} */
	@Override
	public int moveConfig(ResourceNode config, int newPos) throws MException {
		if (!(config instanceof JsonConfig))
			throw new MException("not JsonConfig");
		
		// find array node, to append new config
		JsonNode array = node.get(config.getName());
		if (array == null)
			throw new MException("config set not found");
		
		int pos = findPosOf((ArrayNode)array,(JsonConfig)config);
		if (pos < 0)
			throw new MException("could not find child");

		// make it simple if only one element is in list
		if (((ArrayNode)array).size() == 1) {
			if (newPos == MOVE_FIRST || newPos == MOVE_LAST || newPos == 0)
				return 0;
			throw new MException("out of range");
		}

		if (newPos == MOVE_FIRST) {
			((ArrayNode)array).remove(pos);
			((ArrayNode)array).insert(0, ((JsonConfig)config).node);
			return 0;
		}
		if(newPos == MOVE_LAST) {
			((ArrayNode)array).remove(pos);
			((ArrayNode)array).add(((JsonConfig)config).node);
			return ((ArrayNode)array).size()-1;
		}
		
		if (newPos == MOVE_DOWN) {
			if (pos == ((ArrayNode)array).size()-1)
				throw new MException("out of range");
				
			((ArrayNode)array).remove(pos);
			((ArrayNode)array).insert(pos+1, ((JsonConfig)config).node);
			return pos+1;
		}
		
		if (newPos == MOVE_UP) {
			if (pos == 0)
				throw new MException("out of range");

			((ArrayNode)array).remove(pos);
			((ArrayNode)array).insert(pos-1, ((JsonConfig)config).node);
			return pos-1;
		}
		
		if (pos == newPos) return pos;
		
		((ArrayNode)array).remove(pos);
		((ArrayNode)array).insert(newPos, ((JsonConfig)config).node);
		
		return newPos;
	}

	/** {@inheritDoc} */
	@Override
	public void removeConfig(ResourceNode config) throws MException {
		
		if (!(config instanceof JsonConfig)) return;
		
		// find array node, to append new config
		JsonNode array = node.get(config.getName());
		if (array == null) return;
		
		int pos = findPosOf((ArrayNode)array,(JsonConfig)config);
		if (pos < 0)
			throw new MException("could not find child");
		((ArrayNode)array).remove( pos );
	}

	/**
	 * <p>findPosOf.</p>
	 *
	 * @param array a {@link org.codehaus.jackson.node.ArrayNode} object.
	 * @param config a {@link de.mhus.lib.core.config.JsonConfig} object.
	 * @return a int.
	 */
	protected int findPosOf(ArrayNode array, JsonConfig config) {
		
		//if (config.index >= 0) return config.index;
		
		int cnt = 0;
		for (JsonNode item : array) {
			if (item == config.node)
				return cnt;
			cnt++;
		}
		
		return -1;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public WritableResourceNode getParent() {
		return parent;
	}
	
	/**
	 * <p>write.</p>
	 *
	 * @param os a {@link java.io.OutputStream} object.
	 * @throws java.io.IOException if any.
	 */
	public void write(OutputStream os) throws IOException {
		ObjectMapper m = new ObjectMapper();
		m.writeValue(os,node);
	}
	
	/**
	 * <p>write.</p>
	 *
	 * @param os a {@link java.io.Writer} object.
	 * @throws java.io.IOException if any.
	 */
	public void write(Writer os) throws IOException {
		ObjectMapper m = new ObjectMapper();
		m.writeValue(os,node);
	}

	/** {@inheritDoc} */
	@Override
	public InputStream getInputStream(String key) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public URL getUrl() {
		return null;
	}

}
