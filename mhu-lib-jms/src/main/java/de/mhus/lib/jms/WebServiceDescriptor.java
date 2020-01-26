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
package de.mhus.lib.jms;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.pojo.ActionsOnlyStrategy;
import de.mhus.lib.core.pojo.PojoAction;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;

public class WebServiceDescriptor extends ServiceDescriptor {

    private Object service;
    private PojoModel model;

    public WebServiceDescriptor(Object service) {
        super(findIfc(service));
        this.service = service;
        model =
                new PojoParser()
                        .parse(service, new ActionsOnlyStrategy(true, WebMethod.class))
                        .getModel();

        for (String name : model.getActionNames()) {
            PojoAction act = model.getAction(name);
            functions.put(name, new WebServiceFunction(act));
        }
    }

    @Override
    public Object getObject() {
        return service;
    }

    private static Class<?> findIfc(Object service) {
        // TODO traverse thru all ifcs
        Class<?> c = service instanceof Class ? (Class<?>) service : service.getClass();
        if (c.isAnnotationPresent(WebService.class)) return c;
        for (Class<?> i : c.getInterfaces()) {
            if (i.isAnnotationPresent(WebService.class)) return i;
        }
        return c;
    }

    private class WebServiceFunction extends FunctionDescriptor {

        private PojoAction act;

        public WebServiceFunction(PojoAction act) {
            this.act = act;
            //			oneWay = act.getAnnotation(Oneway.class) != null || act.getReturnType() == null;
            oneWay = act.getAnnotation(Oneway.class) != null;
            returnType = act.getReturnType();
            if (returnType == null) returnType = Void.class;
        }

        @Override
        public RequestResult<Object> doExecute(IProperties properties, Object[] obj) {

            // TODO check special case for direct handling

            MProperties p = new MProperties();
            Object res = null;
            Throwable t = null;
            try {
                res = act.doExecute(service, obj);
            } catch (IOException e) {
                t = e.getCause();
                if (t instanceof InvocationTargetException) {
                    t = t.getCause();
                }
                if (t == null) t = e;
            } catch (Throwable e) {
                t = e;
            }
            if (t != null) {
                // TODO move into ServerService and ServerJsonService to define the protocol in ONE
                // place
                p.setString(ClientObjectProxy.PROP_EXCEPION_TYPE, t.getClass().getCanonicalName());
                p.setString(ClientObjectProxy.PROP_EXCEPION_TEXT, t.getMessage());
                p.setString(
                        ClientObjectProxy.PROP_EXCEPION_CLASS,
                        act.getManagedClass().getCanonicalName());
                p.setString(ClientObjectProxy.PROP_EXCEPTION_METHOD, act.getName());

                log().t(act.getManagedClass().getCanonicalName(), act.getName(), t);
            }
            return new RequestResult<Object>(res, p);
        }
    }
}
