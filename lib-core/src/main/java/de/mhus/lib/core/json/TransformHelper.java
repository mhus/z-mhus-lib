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
package de.mhus.lib.core.json;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.databind.JsonNode;

import de.mhus.lib.core.MJson;
import de.mhus.lib.core.pojo.DefaultFilter;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;

public class TransformHelper {
    int level = 0;
    protected String prefix = "";
    protected TransformStrategy strategy = MJson.DEFAULT_STRATEGY;
    protected boolean force = false;

    public TransformHelper incLevel() {
        level++;
        return this;
    }

    public void postToJson(Object from, JsonNode to) {}

    public Class<?> getType(String clazz) throws IllegalAccessException {
        try {

            if (clazz.endsWith("[]")) {
                clazz = clazz.substring(0, clazz.length() - 2);
            }

            switch (clazz) {
                case "byte":
                    return byte.class;
                case "int":
                    return int.class;
                case "float":
                    return float.class;
                case "double":
                    return double.class;
                case "boolean":
                    return boolean.class;
                case "short":
                    return short.class;
                case "char":
                    return char.class;
            }

            return getClassLoader().loadClass(clazz);
        } catch (Exception e) {
        }
        return null;
    }

    public boolean isArrayType(String type) {
        return type.endsWith("[]");
    }

    public ClassLoader getClassLoader() {
        return TransformHelper.class.getClassLoader();
    }

    public TransformHelper decLevel() {
        level--;
        return this;
    }

    public Object createObject(Class<?> type)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
                    InvocationTargetException, NoSuchMethodException, SecurityException {
        return type.getDeclaredConstructor().newInstance();
    }

    public void log(String string, Throwable t) {
        System.out.println(string);
        t.printStackTrace();
    }

    public String getPrefix() {
        return prefix;
    }

    public TransformHelper setPrefix(String in) {
        prefix = in;
        return this;
    }

    public PojoModel createPojoModel(Object from) {
        PojoModel model =
                new PojoParser()
                        .parse(from, "_", null)
                        .filter(new DefaultFilter(true, false, true, true, true))
                        .getModel();
        return model;
    }

    public void log(String msg) {
        System.out.println(msg);
    }
    /**
     * Return true if the level is ok
     *
     * @return true if level is ok
     */
    public boolean checkLevel() {
        return level < 10;
    }

    public Object createArray(int length, Class<?> type) {
        return Array.newInstance(type, length);
    }

    public TransformStrategy getStrategy() {
        return strategy;
    }

    public void postToPojo(JsonNode from, Object to) {
        // TODO Auto-generated method stub

    }

    public boolean isForce() {
        return force;
    }

    public TransformHelper setForce(boolean force) {
        this.force = force;
        return this;
    }
}
