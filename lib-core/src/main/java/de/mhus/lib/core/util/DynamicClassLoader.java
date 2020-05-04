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
package de.mhus.lib.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.MResourceProvider;
import de.mhus.lib.core.logging.Log;

/**
 * This class loader is a distributor. You can dynamically change the list of child loaders.
 *
 * <p>The class loader is thread safe.
 *
 * @author mikehummel
 */
public class DynamicClassLoader extends ClassLoader {

    public enum RESULT {
        NEXT,
        OWN,
        FORWARD
    };

    public static Log log = Log.getLog(DynamicClassLoader.class);

    protected String name = null;

    protected LinkedList<MResourceProvider> classLoaders = new LinkedList<MResourceProvider>();

    protected Rule[] rules = null;
    protected RESULT defaultRule = RESULT.NEXT;

    // protected ClassLoader last;

    public DynamicClassLoader(String name) {
        // super(new EmptyClassLoader());
        // last = getSystemClassLoader();
        this.name = name;
    }

    public DynamicClassLoader(String name, ClassLoader parent) {
        super(parent);
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setRules(List<Rule> list) {
        rules = list.toArray(new Rule[list.size()]);
    }

    public void addRule(Rule rule) {
        LinkedList<Rule> list = new LinkedList<Rule>();
        if (rules != null) {
            for (Rule r : rules) list.add(r);
        }
        list.add(rule);
        setRules(list);
    }

    public void setDefaultRule(RESULT rule) {
        defaultRule = rule;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        log.t("ask for", this, name);

        if (rules != null) {
            for (Rule rule : rules) {
                RESULT res = rule.check(name);
                switch (res) {
                    case OWN:
                        return findAndOwnClass(name);
                    case FORWARD:
                        return super.findClass(name);
                    default:
                        break;
                }
            }
        }

        if (defaultRule == RESULT.OWN) return findAndOwnClass(name);
        if (defaultRule == RESULT.FORWARD) return super.findClass(name);

        String resName = name.replaceAll("\\.", "/") + ".class";
        for (MResourceProvider cl : classLoaders) {
            try {
                InputStream res = cl.getInputStream(resName);
                if (res != null) {
                    log.t("loaded class", this, cl, name);
                    return toClass(name, res);
                }
            } catch (Exception e) {
                log.t(name, e);
            }
        }

        return super.findClass(name);
    }

    public Class<?> findAndOwnClass(String name) throws ClassNotFoundException {

        if (name.startsWith("java.") || name.startsWith("javax.")) {
            return super.loadClass(name);
        }

        String resName = name.replaceAll("\\.", "/") + ".class";
        for (MResourceProvider cl : classLoaders) {
            try {
                InputStream res = cl.getInputStream(resName);
                if (res != null) {
                    log.t("loaded class", this, cl, name);
                    return toClass(name, res);
                }
            } catch (Exception e) {
                log.t(name, e);
            }
        }
        return super.findClass(name);
    }

    private Class<?> toClass(String name, InputStream is) throws ClassNotFoundException {
        try {

            ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
            byte buffer[] = new byte[1024];
            //			int i = 0;
            do {
                int j = is.read(buffer, 0, buffer.length);
                if (j >= 0) {
                    //					i += j;
                    stream.write(buffer, 0, j);
                } else {
                    break;
                }
            } while (true);
            byte[] binary = stream.toByteArray();
            return defineClass(name, binary, 0, binary.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
    }

    @Override
    protected URL findResource(String name) {
        log.t("resource", this, name);
        for (MResourceProvider cl : classLoaders) {
            try {
                URL res = cl.getUrl(name);
                if (res != null) {
                    res.openStream().close();
                    log.t("loaded resource", this, cl, name);
                    return res;
                }
            } catch (Exception e) {
            }
        }
        return super.findResource(name);
    }

    // --- Methods to handle list

    public boolean add(MResourceProvider e) {
        return classLoaders.add(e);
    }

    public boolean remove(MResourceProvider o) {
        return classLoaders.remove(o);
    }

    public void clear() {
        classLoaders.clear();
    }

    public void add(int index, MResourceProvider element) {
        classLoaders.add(index, element);
    }

    //	public void doSetupFromConfig(IConfig config) {
    //
    //		for (IConfig sub : config.getConfigBundle("resource")) {
    //			if (sub.isProperty("jar")) {
    //				String jar = sub.getExtracted("jar");
    //				log.d("add loader for jar",this,jar);
    //				try {
    //					ZipResourceProvider loader = new ZipResourceProvider(new File(jar));
    //					add(loader);
    //				} catch (Exception e) {
    //					log.w("can't load jar",this,jar);
    //				}
    //			} else
    //			if (sub.isProperty("path")) {
    //				String path = sub.getExtracted("path");
    //				log.d("add loader for path",this,path);
    //				try {
    //					PathResourceProvider loader = new PathResourceProvider(new File(path));
    //					add(loader);
    //				} catch (Exception e) {
    //					log.w("can't load path",this,path);
    //				}
    //
    //			}
    //		}

    //
    //		IConfig cRules = config.getConfig("rules");
    //		if (cRules != null) {
    //
    //			String def = cRules.getExtracted("default","OWN").toUpperCase();
    //			log.d("default rule",this,def);
    //			setDefaultRule(RESULT.valueOf(def));
    //
    //			LinkedList<Rule> r = new LinkedList<DynamicClassLoader.Rule>();
    //			for (IConfig sub : cRules.getConfigBundle("rule")) {
    //				log.d("add rule",this,sub.getExtracted("pattern"),sub.getExtracted("result","FORWARD"));
    //				r.add(new Rule(sub.getExtracted("pattern"),
    // RESULT.valueOf(sub.getExtracted("result","FORWARD").toUpperCase()) ));
    //			}
    //			setRules(r);
    //		}
    //	}

    public static class Rule {

        private String pattern;
        private RESULT result;

        public Rule(String pattern, RESULT result) {
            this.pattern = pattern;
            this.result = result;
        }

        public RESULT check(String name) {
            if (MString.compareFsLikePattern(name, pattern)) return result;
            return RESULT.NEXT;
        }
    }
}
