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
package de.mhus.lib.portlet.resource;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import de.mhus.lib.core.IProperties;

/**
 * <p>Resource interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface Resource {

	/**
	 * <p>serveResource.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @param request a {@link javax.portlet.ResourceRequest} object.
	 * @param response a {@link javax.portlet.ResourceResponse} object.
	 * @return a boolean.
	 * @throws java.io.IOException if any.
	 * @throws javax.portlet.PortletException if any.
	 */
	public boolean serveResource(String path, ResourceRequest request,
			ResourceResponse response) throws IOException,
			PortletException;
	
	/**
	 * <p>createProperties.</p>
	 *
	 * @param request a {@link javax.portlet.ResourceRequest} object.
	 * @return a {@link de.mhus.lib.core.IProperties} object.
	 */
	public IProperties createProperties(ResourceRequest request);

}
