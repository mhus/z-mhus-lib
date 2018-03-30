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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Map;

import javax.portlet.ResourceRequest;
import javax.portlet.filter.PortletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.util.PortalUtil;

import de.mhus.lib.core.logging.Log;

public class ResourceRequestWrapper extends PortletRequestWrapper implements ResourceRequest {

	private HttpServletRequest servletRequest;
	private static Log log = Log.getLog(ResourceRequestWrapper.class);
	
	public ResourceRequestWrapper(ResourceRequest request) {
		super(request);
		
		try {
//			Method method = request.getClass().getMethod("getOriginalHttpServletRequest");
//			servletRequest = (HttpServletRequest)method.invoke(request);
			servletRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(request));
		} catch (Throwable t) {
			log.e(t);
		}

	}

	public ResourceRequest getOriginalResourceRequest() {
		return (ResourceRequest) getRequest();
	}
	
	public HttpServletRequest getServletRequest() {
		return servletRequest;
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
		return super.getAttributeNames();
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
	public InputStream getPortletInputStream() throws IOException {
		return getOriginalResourceRequest().getPortletInputStream();
	}

	@Override
	public void setCharacterEncoding(String enc)
			throws UnsupportedEncodingException {
		getOriginalResourceRequest().setCharacterEncoding(enc);
	}

	@Override
	public BufferedReader getReader() throws UnsupportedEncodingException,
			IOException {
		return getOriginalResourceRequest().getReader();
	}

	@Override
	public String getCharacterEncoding() {
		return getOriginalResourceRequest().getCharacterEncoding();
	}

	@Override
	public String getContentType() {
		return getOriginalResourceRequest().getContentType();
	}

	@Override
	public int getContentLength() {
		return getOriginalResourceRequest().getContentLength();
	}

	@Override
	public String getMethod() {
		return getOriginalResourceRequest().getMethod();
	}

	@Override
	public String getETag() {
		return getOriginalResourceRequest().getETag();
	}

	@Override
	public String getResourceID() {
		return getOriginalResourceRequest().getResourceID();
	}

	@Override
	public Map<String, String[]> getPrivateRenderParameterMap() {
		return getOriginalResourceRequest().getPrivateParameterMap();
	}

	@Override
	public String getCacheability() {
		return getOriginalResourceRequest().getCacheability();
	}

}
