package de.mhus.lib.portlet.callback;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;

import org.codehaus.jackson.JsonGenerator;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.portlet.resource.AjaxResource;

public abstract class AbstractAjaxCallback extends AjaxResource implements MNlsProvider {

	private Log log = Log.getLog(this);
	private MNls nls = MNls.lookup(this);

	@Override
	protected void doRequest(ResourceRequest request, JsonGenerator response)
			throws IOException, PortletException {
				
		CallContext context = createContext(request, response);
		try {
			doRequest(context);
		} catch (Throwable t) {
			log.d(t);
			context.setRuntimeError(t);
		}
		
		context.doSend();
	}

	protected CallContext createContext(ResourceRequest request,
			JsonGenerator response) {
		return  new CallContext(this, request, response);
	}

	protected abstract void doRequest(CallContext context) throws Exception;

	@Override
	public MNls getNls() {
		return nls;
	}

	public void setNls(MNls nls) {
		this.nls = nls;
	}

	public Log getLog() {
		return log;
	}
	
}
