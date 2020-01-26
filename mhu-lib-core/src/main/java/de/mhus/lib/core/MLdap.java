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
package de.mhus.lib.core;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.errors.MRuntimeException;

public class MLdap {

    @SuppressWarnings("unused")
    private static final Log log = Log.getLog(MLdap.class);

    public static final String KEY_NAME = ".name";
    public static final String KEY_FQDN = ".fqdn";
    public static final String KEY_CLASS = ".class";
    public static final String FILTER_ALL_CLASSES = "(objectclass=*)";

    @SuppressWarnings("unchecked")
    public static DirContext getConnection(String url, String principal, String password)
            throws NamingException {
        @SuppressWarnings("rawtypes")
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, principal);
        env.put(Context.SECURITY_CREDENTIALS, password);

        DirContext ctx = new InitialDirContext(env);

        return ctx;
    }

    public static SearchControls getSimpleSearchControls() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setTimeLimit(30000);
        return searchControls;
    }

    public static Map<String, Object> resultToMap(SearchResult result) throws NamingException {
        Map<String, Object> map = new HashMap<>();
        NamingEnumeration<? extends Attribute> attrs = result.getAttributes().getAll();
        while (attrs.hasMore()) {
            Attribute attr = attrs.next();
            String attrId = attr.getID();
            if (attr.size() > 1) {
                LinkedList<Object> list = new LinkedList<>();
                for (int i = 0; i < attr.size(); i++) {
                    String attrValue = String.valueOf(attr.get(i));
                    list.add(attrValue);
                }
                map.put(attrId, list);
            } else {
                String attrValue = String.valueOf(attr.get());
                map.put(attrId, attrValue);
            }
        }

        map.put(KEY_NAME, result.getName());
        map.put(KEY_FQDN, result.getNameInNamespace());
        map.put(KEY_CLASS, result.getClassName());

        return map;
    }

    public static Map<String, Object> getFirst(NamingEnumeration<SearchResult> res) {
        try {
            Map<String, Object> next = getNext(res);
            res.close();
            return next;
        } catch (Exception t) {
            throw new MRuntimeException(t);
        }
    }

    public static Map<String, Object> getNext(NamingEnumeration<SearchResult> res) {
        try {
            if (!res.hasMore()) return null;
            SearchResult result = (SearchResult) res.next();
            return resultToMap(result);
        } catch (Exception t) {
            throw new MRuntimeException(t);
        }
    }

    public static Iterable<Map<String, Object>> iterate(final NamingEnumeration<SearchResult> res) {
        return new Iterable<Map<String, Object>>() {

            @Override
            public Iterator<Map<String, Object>> iterator() {
                return new Iterator<Map<String, Object>>() {

                    @Override
                    public boolean hasNext() {
                        try {
                            return res.hasMore();
                        } catch (Exception t) {
                            throw new MRuntimeException(t);
                        }
                    }

                    @Override
                    public Map<String, Object> next() {
                        try {
                            SearchResult result = (SearchResult) res.next();
                            Map<String, Object> map = resultToMap(result);
                            return map;
                        } catch (Exception t) {
                            throw new MRuntimeException(t);
                        }
                    }
                };
            }
        };
    }

    public static List<String> getNames(NamingEnumeration<SearchResult> res) {
        LinkedList<String> out = new LinkedList<>();
        try {
            while (res.hasMore()) {
                SearchResult result = res.next();
                out.add(result.getName());
            }
        } catch (Exception t) {
            throw new MRuntimeException(t);
        }
        return out;
    }

    public static List<String> getFQDNs(NamingEnumeration<SearchResult> res) {
        LinkedList<String> out = new LinkedList<>();
        try {
            while (res.hasMore()) {
                SearchResult result = res.next();
                out.add(result.getNameInNamespace());
            }
        } catch (Exception t) {
            throw new MRuntimeException(t);
        }
        return out;
    }
}
