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
package de.mhus.lib.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MApi;

public class MObjectInputStream extends ObjectInputStream {

	private ClassLoader cl = null;
	private MActivator act = null;

	public MObjectInputStream() throws IOException, SecurityException {
		super();
	}

	public MObjectInputStream(InputStream in, MActivator act) throws IOException {
		super(in);
		setActivator(act);
	}
	
	public MObjectInputStream(InputStream in, ClassLoader cl) throws IOException {
		super(in);
		setClassLoader(cl);
	}
	
	public MObjectInputStream(InputStream in) throws IOException {
		super(in);
	}
	
	public void setActivator(MActivator activator) {
		act = activator;
	}
	
	public void setClassLoader(ClassLoader cl) {
		this.cl = cl;
	}

    @Override
	protected Class<?> resolveClass(ObjectStreamClass desc)
            throws IOException, ClassNotFoundException
    {
    	String name = desc.getName();
    	
    	if (act == null && cl == null) {
    		act = MApi.get().base().lookup(MActivator.class); // load default activator
    	}
    	
    	try {
	    	if (act != null)
	    		return act.loadClass(name);
        } catch (ClassNotFoundException ex) {
        }
    	
        try {
            return Class.forName(name, true, cl );
        } catch (ClassNotFoundException ex) {
        	return super.resolveClass(desc);
        }
    }

    @Override
	protected Class<?> resolveProxyClass(String[] interfaces)
            throws IOException, ClassNotFoundException
    {
        ClassLoader latestLoader = cl;
        ClassLoader nonPublicLoader = null;
        boolean hasNonPublicInterface = false;

        // define proxy in class loader of non-public interface(s), if any
        @SuppressWarnings("rawtypes")
		Class[] classObjs = new Class[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            @SuppressWarnings("rawtypes")
			Class cl = Class.forName(interfaces[i], false, latestLoader);
            if ((cl.getModifiers() & Modifier.PUBLIC) == 0) {
                if (hasNonPublicInterface) {
                    if (nonPublicLoader != cl.getClassLoader()) {
                        throw new IllegalAccessError(
                            "conflicting non-public interface class loaders");
                    }
                } else {
                    nonPublicLoader = cl.getClassLoader();
                    hasNonPublicInterface = true;
                }
            }
            classObjs[i] = cl;
        }
        try {
            return Proxy.getProxyClass(
                hasNonPublicInterface ? nonPublicLoader : latestLoader,
                classObjs);
        } catch (IllegalArgumentException e) {
            throw new ClassNotFoundException(null, e);
        }
    }
    
}
