package de.mhus.lib.liferay.portlet;

import java.util.Enumeration;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.filter.PortletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.util.PortalUtil;

public class RenderRequestWrapper extends PortletRequestWrapper implements RenderRequest {

	private HttpServletRequest servletRequest;

	public RenderRequestWrapper(RenderRequest renderRequest) {
		super(renderRequest);
		try {
			servletRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(renderRequest));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public RenderRequest getOriginalRenderRequest() {
		return (RenderRequest) getRequest();
	}
	
	@Override
	public String getParameter(String name) {
		if (servletRequest != null)
			return servletRequest.getParameter(name);
		return super.getParameter(name);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		if (servletRequest != null)
			return servletRequest.getParameterNames();
		return super.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		if (servletRequest != null)
			return servletRequest.getParameterValues(name);
		return super.getParameterValues(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		if (servletRequest != null)
			return servletRequest.getParameterMap();
		return super.getParameterMap();
	}

	@Override
	public String getETag() {
		return getOriginalRenderRequest().getETag();
	}


}
