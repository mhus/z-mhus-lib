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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.directory.ClassLoaderResourceProvider;
import de.mhus.lib.core.directory.MResourceProvider;

public class MNlsFactory extends MNlsBundle {

    @SuppressWarnings("unused")
    private IConfig config;

    public MNlsFactory() {
        this(null);
        //		forkBase();
    }

    public MNlsFactory(IConfig config) {
        this.config = config;
    }

    public MNls create(Object owner) {
        return load(null, null, toResourceName(owner), null);
    }

    public MNls load(Class<?> owner) {
        return load(null, owner, null, null);
    }

    public static String toResourceName(Object owner) {
        if (owner == null) return null;
        if (owner instanceof String) return (String) owner;
        return MSystem.getClassName(owner).replace('.', '/');
    }

    public MNls load(Class<?> owner, Locale locale) {
        return load(null, owner, null, locale == null ? null : locale.toString());
    }

    public MNls load(MResourceProvider res, Class<?> owner, String resourceName, String locale) {
        return load(res, owner, resourceName, locale, true);
    }

    public MNls load(
            MResourceProvider res,
            Class<?> owner,
            String resourceName,
            String locale,
            boolean searchAlternatives) {
        return load(res, owner, resourceName, locale, searchAlternatives, 0);
    }

    protected MNls load(
            MResourceProvider res,
            Class<?> owner,
            String resourceName,
            String locale,
            boolean searchAlternatives,
            int level) {
        if (level > 50) return null;
        try {
            // if (res == null) res = base(MDirectory.class);

            if (resourceName == null) {
                if (owner.getCanonicalName() != null)
                    resourceName = owner.getCanonicalName().replace('.', '/');
                else resourceName = owner.getEnclosingClass().getCanonicalName().replace('.', '/');
            }

            if (res == null) {
                res = findResourceProvider(owner);
            }

            if (locale == null) locale = getDefaultLocale();

            InputStream is = null;
            Properties properties = new Properties();

            is =
                    res
                            .getInputStream(locale.toString() + "/" + resourceName + ".properties");
            String prefix = getResourcePrefix();

            if (searchAlternatives) {

                if (prefix != null && is == null)
                    is =
                            res.getInputStream(
                                            prefix
                                                    + "/"
                                                    + getDefaultLocale()
                                                    + "/"
                                                    + resourceName
                                                    + ".properties");
                if (is == null)
                    is =
                            res.getInputStream(
                                            getDefaultLocale() + "/" + resourceName + ".properties");
                if (prefix != null && is == null)
                    is =
                            res.getInputStream(prefix + "/" + resourceName + ".properties");
                if (is == null)
                    is = res.getInputStream(resourceName + ".properties");
            }

            if (is != null) {
                log().t("Load Resource", resourceName, locale);
                InputStreamReader r = new InputStreamReader(is, MString.CHARSET_UTF_8);
                properties.load(r);
                is.close();

                for (String include : properties.getProperty(".include", "").split(",")) {
                    include = include.trim();
                    MNls parent = load(null, null, include, locale, false, level + 1);
                    if (parent != null) {
                        for (Map.Entry<Object, Object> entry : parent.properties.entrySet()) {
                            if (!properties.containsKey(entry.getKey()))
                                properties.put(entry.getKey(), entry.getValue());
                        }
                    }
                }

                return new MNls(properties, "");
            } else {
                log().d("Resource not found", resourceName, locale);
            }

        } catch (Throwable e) {
            log().e(e);
        }

        return new MNls();
    }

    protected String getResourcePrefix() {
        return null;
    }

    protected MResourceProvider findResourceProvider(Class<?> owner) {
        if (owner != null) return new ClassLoaderResourceProvider(owner.getClassLoader());
        else return new ClassLoaderResourceProvider();
    }

    public String getDefaultLocale() {
        return Locale.getDefault().toString();
    }

    public MNls load(InputStream is) {
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            log().e(e);
        }
        return new MNls(properties, "");
    }

    public static MNlsFactory lookup(Object owner) {
        return M.l(MNlsFactory.class);
    }

    @Override
    public MNls createNls(String locale) {
        return load(null, null, getPath(), locale, false);
    }
}
