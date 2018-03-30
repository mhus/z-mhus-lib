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
package de.mhus.lib.liferay.portlet;

import java.util.Enumeration;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.filter.PortletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.util.PortalUtil;


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
