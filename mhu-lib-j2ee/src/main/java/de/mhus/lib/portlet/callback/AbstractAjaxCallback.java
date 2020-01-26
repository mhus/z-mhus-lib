/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.portlet.callback;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;

import org.codehaus.jackson.JsonGenerator;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.portlet.resource.AjaxResource;

/**
 * Abstract AbstractAjaxCallback class.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class AbstractAjaxCallback extends AjaxResource implements MNlsProvider {

    private Log log = Log.getLog(this);
    private MNls nls = MNls.lookup(this);

    /** {@inheritDoc} */
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

    /**
     * createContext.
     *
     * @param request a {@link javax.portlet.ResourceRequest} object.
     * @param response a {@link org.codehaus.jackson.JsonGenerator} object.
     * @return a {@link de.mhus.lib.portlet.callback.CallContext} object.
     */
    protected CallContext createContext(ResourceRequest request, JsonGenerator response) {
        return new CallContext(this, request, response);
    }

    /**
     * doRequest.
     *
     * @param context a {@link de.mhus.lib.portlet.callback.CallContext} object.
     * @throws java.lang.Exception if any.
     */
    protected abstract void doRequest(CallContext context) throws Exception;

    /** {@inheritDoc} */
    @Override
    public MNls getNls() {
        return nls;
    }

    /**
     * Setter for the field <code>nls</code>.
     *
     * @param nls a {@link de.mhus.lib.core.util.MNls} object.
     */
    public void setNls(MNls nls) {
        this.nls = nls;
    }

    /**
     * Getter for the field <code>log</code>.
     *
     * @return a {@link de.mhus.lib.core.logging.Log} object.
     */
    public Log getLog() {
        return log;
    }
}
