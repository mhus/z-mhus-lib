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

import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;

import de.mhus.lib.basics.consts.Identifier;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.pojo.MPojo;

/**
 * This is a shortcut class to call methods without obfuscating the source code. For some reasons
 * this makes sense.
 *
 * @author mikehummel
 */
public class M {

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
}
