/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.portlet.actions;

import java.util.HashMap;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.portlet.IllegalCharacterException;

/**
 * ActionRequestHandler class.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ActionRequestHandler implements Action {

    private HashMap<String, Action> registry = new HashMap<String, Action>();

    /** {@inheritDoc} */
    @Override
    public boolean processAction(String path, ActionRequest request, ActionResponse response)
            throws Exception {

        if (path == null) return false;

        String name = null;
        int p = path.indexOf('/');
        if (p > 0) {
            name = path.substring(0, p);
            path = path.substring(p + 1);
        } else {
            name = path;
            path = null;
        }
        Action resource = null;
        synchronized (registry) {
            resource = registry.get(name);
        }
        if (resource == null) return false;

        return resource.processAction(path, request, response);
    }

    /**
     * register.
     *
     * @param name a {@link java.lang.String} object.
     * @param resource a {@link de.mhus.lib.portlet.actions.Action} object.
     */
    public void register(String name, Action resource) {
        if (name == null || name.indexOf('/') >= 0) throw new IllegalCharacterException('/', name);
        synchronized (registry) {
            registry.put(name, resource);
        }
    }

    /**
     * unregister.
     *
     * @param name a {@link java.lang.String} object.
     */
    public void unregister(String name) {
        synchronized (registry) {
            registry.remove(name);
        }
    }

    /**
     * processAction.
     *
     * @param request a {@link javax.portlet.ActionRequest} object.
     * @param response a {@link javax.portlet.ActionResponse} object.
     * @return a boolean.
     * @throws java.lang.Exception if any.
     */
    public boolean processAction(ActionRequest request, ActionResponse response) throws Exception {
        String action = request.getParameter(ActionRequest.ACTION_NAME);
        if (action != null) {
            return processAction(action, request, response);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized IProperties createProperties(ActionRequest request) {
        return null;
    }
}
