package de.mhus.lib.portlet.callback;

import java.util.LinkedList;

import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.errors.MException;

public class CallContext implements MNlsProvider {

	public static final String DEFAULT_DATA_NAME = "data";
	
	private JsonGenerator response;
	private ObjectMapper mapper;
	private ObjectNode node;
	private MNls nls;
	private LinkedList<Alert> alerts;
	private boolean success = true;
	private ArrayNode result;
	private Log log;
	private ResourceRequest request;
	private AbstractProperties properties;

	public CallContext(AbstractAjaxCallback callback, ResourceRequest request, JsonGenerator response) {
		this.request = request;
		this.response = response;
//		this.callback = callback;
		this.log = callback.getLog();
		this.nls = callback.getNls();
		
		mapper = new ObjectMapper();
		node = mapper.createObjectNode();
		result = node.putArray("results");
		properties =callback.createProperties(request);

	}

	public void addSuccess(String msg) {
		addAlert(new Alert(Alert.PRIORITY.SUCCESS, msg), true);
	}
	
	public void addError(String error) {
		addAlert(new Alert(Alert.PRIORITY.ERROR, error), true);
	}

	public void addWarning(String warning) {
		addAlert(new Alert(Alert.PRIORITY.WARNING, warning), true);
	}

	public void addInforamtion(String info) {
		addAlert(new Alert(Alert.PRIORITY.INFO, info), true);
	}
	
	public void addAlert(Alert alert, boolean useNls) {
		
		String msg = alert.msg;
		alert.msg = MNls.find(this, msg);
		
		if (alerts == null)
			alerts = new LinkedList<Alert>();
		alerts.add(alert);
	}
	
	public void setRuntimeError(Throwable t) {
		log.e(t);
		if (alerts != null) alerts.clear();
		addError(t.toString());
		setSuccess(false);
	}

	public void setSuccess(boolean success) {
		this.success  = success;
	}

	public void doSend() {
		if (!success) {
			result.removeAll();
		}
		node.put("success", success ? 1 : 0);
		
		if (alerts != null) {
			ArrayNode sub = node.putArray("alerts");
			for (Alert alert : alerts) {
				ObjectNode line = sub.addObject();
				alert.print(line);
			}
		}

		response.setCodec(mapper);
		try {
			response.writeTree(node);
		} catch (Exception e) {
			log.e(e);
		}

	}

	@Override
	public MNls getNls() {
		return nls;
	}

	public void setNls(MNls mNls) {
		this.nls = mNls;
	}

	public ResourceRequest getRequest() {
		return request;
	}
	
	public Log log() {
		return log;
	}
	
	public ObjectNode addResult() {
		return result.addObject();
	}
	
	public static class Alert {
		private PRIORITY type;
		private String msg;

		public Alert(PRIORITY type, String msg) {
			this.type = type;
			this.msg = msg;
		}

		public void print(ObjectNode line) {
			line.put("message", msg);
			line.put("priority", type.name().toLowerCase());
			if (type == PRIORITY.ERROR) {
				line.put("fade", 0);
			}
		}

		public enum PRIORITY {ERROR,INFO,WARNING,SUCCESS};
	}
	
	public PortletSession getSession() {
		return getRequest().getPortletSession();
	}
	
	public AbstractProperties getProperties() {
		return properties;
	}

	public JsonNode getData() throws MException {
		return getData(DEFAULT_DATA_NAME);
	}
	
	public JsonNode getData(String attributeName) throws MException {
		JsonNode in = null;
		String dataStr = getProperties().getString(attributeName);
		if (dataStr != null && !dataStr.equals("undefined")) {
			try {
				JsonFactory f = new JsonFactory();
				ObjectMapper mapper = new ObjectMapper();
				JsonParser parser = f.createJsonParser(dataStr);
				in = mapper.readTree(parser);
			} catch (Throwable t) {}
		}
		return in;
	}

}
