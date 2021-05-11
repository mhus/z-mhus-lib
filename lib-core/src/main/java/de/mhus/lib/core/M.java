/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
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
package de.mhus.lib.core;

import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import de.mhus.lib.basics.consts.Identifier;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.pojo.MPojo;
import de.mhus.lib.core.util.EmptyList;
import de.mhus.lib.core.util.EnumerationIterator;
import de.mhus.lib.core.util.Iterate;

/**
 * This is a shortcut class to call methods without obfuscating the source code. For some reasons
 * this makes sense.
 *
 * @author mikehummel
 */
public class M {

    public static final UUID EMPTY_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final List<?> EMPTY_LIST = new EmptyList<>();

    public static final Locale LOCALE_DE_DE = new Locale("de", "DE");
    public static final Locale LOCALE_EN_US = new Locale("en", "US");

    public static final String PROP_PREFIX = "mhus.lib.";

    public static final String PROP_DIRTY_TRACE = "mhu.lib.api.trace";
    public static final String PROP_API_FACTORY_CLASS = "mhus.api.factory";
    //  public static final String PROP_FILE_WATCH = "mhus.config.watch";
    public static final String PROP_CONFIG_FILE = PROP_PREFIX + "config.file";

    public static final String DEFAULT_MHUS_CONFIG_FILE = "mhus-config.xml";

    public static final String PROP_LOG_FACTORY_CLASS = "log.factory";

    public static final String PROP_LOG_MLOG_FACTORY_CLASS = "log.mlog.factory";

    public static final String PROP_LOG_LEVEL_MAPPER_CLASS = "level.mapper";

    public static final String PROP_LOG_PARAMETER_MAPPER_CLASS = "parameter.mapper";

    public static final String PROP_LOG_PARAMETER_ENTRY_MAPPER_CLASS = "parameter.entry.mapper";

    public static final String PROP_BASE_DIR = "base.dir";

    public static final String PROP_LOG_CONSOLE_REDIRECT = "log.console.redirect";

    public static final String DEFAULT_MHUS_TIMER_CONFIG_FILE = "mhus-timer.properties";

    public static final String PROP_TIMER_CONFIG_FILE = "timer.config.file";

    public static final String PROP_LOG_MAX_MESSAGE_SIZE = "log.max.message.size";

    public static final String PROP_LOG_MAX_MESSAGE_SIZE_EXCEPTIONS =
            "log.max.message.size.exceptions";

    public static final String PROP_LOG_LEVEL = "log.level";
    public static final String PROP_LOG_VERBOSE = "log.verbose";

    public static final int MAX_DEPTH_LEVEL = 20;

    public static final String ADDR_EMAIL = "email";
    public static final String ADDR_SALUTATION = "salutation";
    public static final String ADDR_NAME_TITLE = "nameTitle";
    public static final String ADDR_FIRST_NAME = "firstName";
    public static final String ADDR_NAME_MID = "nameMid";
    public static final String ADDR_LAST_NAME = "lastName";
    public static final String ADDR_NAME_AFFIX = "nameAffix";
    public static final String ADDR_TOWN = "town";
    public static final String ADDR_ZIP = "zip";
    public static final String ADDR_STREET = "street";
    public static final String ADDR_HOUSE_NUMBER = "houseNumber";
    public static final String ADDR_PHONE = "phone";
    public static final String ADDR_MOBILE = "mobile";
    public static final String ADDR_COUNTRY = "country";

    public static final String ADDR_DISPLAY_NAME = "displayName";

    public enum DEBUG {NO,YES,TRACE}
    
    public static enum ADDR_SCOPE {
        PRIVATE,
        WORK
    }

    public static final String TYPE_DATE = "date";
    public static final String TYPE_RAW = "raw";
    public static final String TYPE_BOOL = "bool";
    public static final String TYPE_INT = "int";
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_LONG = "long";
    public static final String TYPE_FLOAT = "float";
    public static final String TYPE_DOUBLE = "double";

    public static final String CFG_SYSTEM = "system";
    public static final String PARAM_AUTH_TOKEN = "auth_token";
    
    /**
     * Return a string cascading the names of the getters (without 'get' prefix). and joined with
     * underscore.
     *
     * <p>This is used to create identifiers for MForm or Adb.
     *
     * @param idents
     * @return combined name of the getters e.g. address_firstname
     */
    //	@SuppressWarnings("unchecked")
    //	public static <T> String n(Function<T,?> ... getters ) {
    //		return MPojo.toAttributeName(getters);
    //	}

    public static <T, U> String n(Identifier... idents) {
        return MPojo.toAttributeName(idents);
    }

    /**
     * Truncate the string by length characters.
     *
     * @param in String to truncate
     * @param length Max length
     * @return Same or truncated string
     */
    public static String trunc(String in, int length) {
        return MString.truncate(in, length);
    }

    /**
     * Cast to default type
     *
     * @param in
     * @param def
     * @return Integer
     */
    public static int to(Object in, int def) {
        return MCast.toint(in, def);
    }

    public static int toint(Object in, int def) {
        return MCast.toint(in, def);
    }

    public static long to(Object in, long def) {
        return MCast.tolong(in, def);
    }

    public static long tolong(Object in, long def) {
        return MCast.tolong(in, def);
    }

    public static double to(Object in, double def) {
        return MCast.todouble(in, def);
    }

    public static double todouble(Object in, double def) {
        return MCast.todouble(in, def);
    }

    public static boolean to(Object in, boolean def) {
        return MCast.toboolean(in, def);
    }

    public static Date to(Object in, Date def) {
        return MCast.toDate(in, def);
    }

    public static Date todate(Object in, Date def) {
        return MCast.toDate(in, def);
    }

    public static String to(Object in, String def) {
        return MCast.toString(in, def);
    }

    /**
     * M.l()
     *
     * @param class1
     * @return the service
     */
    public static <T> T l(Class<T> class1) {
        return MApi.lookup(class1);
    }

    /**
     * M.l()
     *
     * @param class1
     * @param def
     * @return the service
     */
    public static <T, D extends T> T l(Class<T> class1, Class<D> def) {
        return MApi.lookup(class1, def);
    }

    /**
     * Return the node value as string or default.
     *
     * @param node
     * @param path
     * @param def
     * @return value of the node
     */
    public static String get(JsonNode node, String path, String def) {
        return MJson.getText(node, path, def);
    }

    /**
     * Fast access to logger.
     *
     * @param owner
     * @return a log instance for the owner
     */
    public static Log log(Object owner) {
        try {
            return MApi.get().lookupLog(owner);
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    public static <E> Iterable<E> iterate(Iterator<E> iterator) {
        return new Iterate<>(iterator);
    }

    public static <E> Iterable<E> iterate(Enumeration<E> enu) {
        return new EnumerationIterator<E>(enu);
    }
}
