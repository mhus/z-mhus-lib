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
package de.mhus.lib.liferay;

import com.liferay.portal.kernel.servlet.PortalDelegateServlet;

/**
 * This is a wrapper for the Liferay PortalDelegateServlet to have a workaround for a fix described in
 * https://issues.liferay.com/browse/LPS-66660
 * 
 * Thx to Gustavo Segovia for this workaround. I added this class to provide the fix to all servlets.
 * 
 * @author mikehummel
 *
 */
/*
<?xml version="1.0" encoding="ISO-8859-1"?>
<web-app xmlns="http://java.sun.com/xml/ns/j2ee"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
     version="2.5">
     
    <display-name>NAME</display-name> 
	<servlet>
	    <servlet-name>Name</servlet-name>
	    <servlet-class>de.mhus.lib.liferay.MPortalDelegateServlet</servlet-class>
	    <init-param>
	        <param-name>servlet-class</param-name>
	        <param-value>de....HttpServlet</param-value>
	    </init-param>
	    <init-param>
	        <param-name>sub-context</param-name>
	        <param-value>alias</param-value>
	    </init-param> 
	    <load-on-startup>1</load-on-startup>
	</servlet>
</web-app>
 */
public class MPortalDelegateServlet extends PortalDelegateServlet {
    @Override
    public String getServletInfo() {
        if (servlet == null) {
            return "";
        }
        String ret = super.getServletInfo();
        if (ret == null) return "";
        return ret;
    }

	@Override
	protected void doPortalDestroy() {
		System.out.println("*** Destroy Servlet " + getServletName());
		super.doPortalDestroy();
	}

	@Override
	protected void doPortalInit() throws Exception {
		System.out.println("*** Init Servlet " + getServletName());
		super.doPortalInit();
	}
    
    
}