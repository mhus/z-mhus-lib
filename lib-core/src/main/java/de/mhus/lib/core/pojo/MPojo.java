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
package de.mhus.lib.core.pojo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.mhus.lib.annotations.generic.Public;
import de.mhus.lib.basics.consts.Identifier;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.cast.Caster;
import de.mhus.lib.core.json.TransformHelper;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.Base64;

public class MPojo {

    public static final String DEEP = "deep";

    private static final int MAX_LEVEL = 10;
    private static Log log = Log.getLog(MPojo.class);
    private static PojoModelFactory defaultModelFactory;

    public static synchronized PojoModelFactory getDefaultModelFactory() {
        if (defaultModelFactory == null)
            defaultModelFactory =
                    new PojoModelFactory() {

                        @Override
                        public PojoModel createPojoModel(Class<?> pojoClass) {
                            PojoModel model =
                                    new PojoParser()
                                            .parse(pojoClass, "_", null)
                                            .filter(
                                                    new DefaultFilter(
                                                            true, false, false, false, true))
                                            .getModel();
                            return model;
                        }
                    };
        return defaultModelFactory;
    }

    public static synchronized PojoModelFactory getAttributeModelFactory() {
        if (defaultModelFactory == null)
            defaultModelFactory =
                    new PojoModelFactory() {

                        @Override
                        public PojoModel createPojoModel(Class<?> pojoClass) {
                            PojoModel model =
                                    new PojoParser()
                                            .parse(
                                                    pojoClass,
                                                    new AttributesStrategy(true, true, "_", null))
                                            .filter(
                                                    new DefaultFilter(
                                                            true, false, false, false, true))
                                            .getModel();
                            return model;
                        }
                    };
        return defaultModelFactory;
    }

    public static void pojoToJson(Object from, ObjectNode to) throws IOException {
        pojoToJson(from, to, getDefaultModelFactory());
    }

    public static void pojoToJson(Object from, ObjectNode to, PojoModelFactory factory)
            throws IOException {
        pojoToJson(from, to, factory, false, 0);
    }

    public static void pojoToJson(
            Object from, ObjectNode to, PojoModelFactory factory, boolean usePublic)
            throws IOException {
        pojoToJson(from, to, factory, usePublic, 0);
    }

    public static void pojoToJson(
            Object from, ObjectNode to, PojoModelFactory factory, boolean usePublic, int level)
            throws IOException {
        if (level > MAX_LEVEL) return;
        PojoModel model = factory.createPojoModel(from.getClass());
        for (PojoAttribute<?> attr : model) {
            boolean deep = false;
            if (!attr.canRead()) continue;
            if (usePublic) {
                Public pub = attr.getAnnotation(Public.class);
                if (pub != null) {
                    if (!pub.readable()) continue;
                    if (MCollection.contains(pub.hints(), MPojo.DEEP)) deep = true;
                }
            }
            Object value = attr.get(from);
            String name = attr.getName();
            setJsonValue(to, name, value, factory, usePublic, deep, level + 1);
        }
    }

    @SuppressWarnings("unchecked")
    public static void addJsonValue(
            ArrayNode to,
            Object value,
            PojoModelFactory factory,
            boolean usePublic,
            boolean deep,
            int level)
            throws IOException {
        if (level > MAX_LEVEL) return;
        try {
            if (value == null) to.addNull();
            else if (value instanceof Boolean) to.add((boolean) value);
            else if (value instanceof Integer) to.add((int) value);
            else if (value instanceof String) to.add((String) value);
            else if (value instanceof Long) to.add((Long) value);
            else if (value instanceof byte[]) to.add((byte[]) value);
            else if (value instanceof Float) to.add((Float) value);
            else if (value instanceof BigDecimal) to.add((BigDecimal) value);
            else if (value instanceof JsonNode) to.add((JsonNode) value);
            else if (value.getClass().isEnum()) {
                to.add(((Enum<?>) value).ordinal());
                //			to.put(name + "_", ((Enum<?>)value).name());
            } else if (value instanceof Map) {
                ObjectNode obj = to.objectNode();
                to.add(obj);
                for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) value).entrySet()) {
                    setJsonValue(
                            obj,
                            String.valueOf(entry.getKey()),
                            entry.getValue(),
                            factory,
                            usePublic,
                            true,
                            level + 1);
                }
            } else if (value instanceof Collection) {
                ArrayNode array = to.arrayNode();
                to.add(array);
                for (Object o : ((Collection<Object>) value)) {
                    addJsonValue(array, o, factory, usePublic, true, level + 1);
                }
            } else {
                if (deep) {
                    ObjectNode too = to.objectNode();
                    to.add(too);
                    pojoToJson(value, too, factory, usePublic, level + 1);
                } else {
                    to.add(String.valueOf(value));
                }
            }
        } catch (Throwable t) {
            log.t(t);
        }
    }

    @SuppressWarnings("unchecked")
    public static void setJsonValue(
            ObjectNode to,
            String name,
            Object value,
            PojoModelFactory factory,
            boolean usePublic,
            boolean deep,
            int level)
            throws IOException {
        if (level > MAX_LEVEL) return;
        try {
            if (value == null) to.putNull(name);
            else if (value instanceof Boolean) to.put(name, (boolean) value);
            else if (value instanceof Integer) to.put(name, (int) value);
            else if (value instanceof String) to.put(name, (String) value);
            else if (value instanceof Long) to.put(name, (long) value);
            else if (value instanceof byte[]) to.put(name, (byte[]) value);
            else if (value instanceof Float) to.put(name, (float) value);
            else if (value instanceof Double) to.put(name, (double) value);
            else if (value instanceof Short) to.put(name, (short) value);
            else if (value instanceof Character)
                to.put(name, Character.toString((Character) value));
            else if (value instanceof Date) {
                to.put(name, ((Date) value).getTime());
                to.put(name + "_", MDate.toIso8601((Date) value));
            } else if (value instanceof BigDecimal) to.put(name, (BigDecimal) value);
            else if (value instanceof JsonNode) to.set(name, (JsonNode) value);
            else if (value.getClass().isEnum()) {
                to.put(name, ((Enum<?>) value).ordinal());
                to.put(name + "_", ((Enum<?>) value).name());
            } else if (value instanceof Map) {
                ObjectNode obj = to.objectNode();
                to.set(name, obj);
                for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) value).entrySet()) {
                    setJsonValue(
                            obj,
                            String.valueOf(entry.getKey()),
                            entry.getValue(),
                            factory,
                            usePublic,
                            true,
                            level + 1);
                }
            } else if (value.getClass().isArray()) {
                ArrayNode array = to.arrayNode();
                to.set(name, array);
                for (Object o : (Object[]) value) {
                    addJsonValue(array, o, factory, usePublic, true, level + 1);
                }
            } else if (value instanceof Collection) {
                ArrayNode array = to.arrayNode();
                to.set(name, array);
                for (Object o : ((Collection<Object>) value)) {
                    addJsonValue(array, o, factory, usePublic, true, level + 1);
                }
            } else {
                if (deep) {
                    ObjectNode too = to.objectNode();
                    to.set(name, too);
                    pojoToJson(value, too, factory, usePublic, level + 1);
                } else {
                    to.put(name, String.valueOf(value));
                }
            }
        } catch (Throwable t) {
            log.t(t);
        }
    }

    public static void jsonToPojo(JsonNode from, Object to) throws IOException {
        jsonToPojo(from, to, getDefaultModelFactory(), false);
    }

    public static void jsonToPojo(JsonNode from, Object to, boolean force) throws IOException {
        jsonToPojo(from, to, getDefaultModelFactory(), force);
    }

    public static void jsonToPojo(JsonNode from, Object to, PojoModelFactory factory)
            throws IOException {
        jsonToPojo(from, to, factory, false);
    }

    @SuppressWarnings("unchecked")
    public static void jsonToPojo(JsonNode from, Object to, PojoModelFactory factory, boolean force)
            throws IOException {
        PojoModel model = factory.createPojoModel(to.getClass());
        for (PojoAttribute<Object> attr : model) {

            if (!attr.canWrite()) continue;

            String name = attr.getName();
            Class<?> type = attr.getType();
            JsonNode json = from.get(name);

            try {
                if (json == null || !attr.canWrite()) {

                } else if (type == Boolean.class || type == boolean.class)
                    attr.set(to, json.asBoolean(false), force);
                else if (type == Integer.class || type == int.class)
                    attr.set(to, json.asInt(0), force);
                else if (type == Long.class || type == long.class)
                    attr.set(to, json.asLong(0), force);
                else if (type == Double.class || type == double.class)
                    attr.set(to, json.asDouble(0), force);
                else if (type == Float.class || type == float.class)
                    attr.set(to, (float) json.asDouble(0), force);
                else if (type == Byte.class || type == byte.class)
                    attr.set(to, (byte) json.asInt(0), force);
                else if (type == Short.class || type == short.class)
                    attr.set(to, (short) json.asInt(0), force);
                else if (type == Character.class || type == char.class)
                    attr.set(to, (char) json.asInt(0), force);
                else if (type == String.class) attr.set(to, json.asText(), force);
                else if (type == UUID.class)
                    try {
                        attr.set(to, UUID.fromString(json.asText()), force);
                    } catch (IllegalArgumentException e) {
                        attr.set(to, null, force);
                    }
                else if (type.isEnum()) {
                    Object[] cons = type.getEnumConstants();
                    int ord = json.asInt(0);
                    Object c = cons.length > 0 ? cons[0] : null;
                    if (ord >= 0 && ord < cons.length) c = cons[ord];
                    attr.set(to, c, force);
                } else attr.set(to, json.asText(), force);
            } catch (Throwable t) {
                log.d(MSystem.getClassName(to), name, t);
            }
        }
    }

    public static void pojoToXml(Object from, Element to) throws IOException {
        pojoToXml(from, to, getDefaultModelFactory());
    }

    public static void pojoToXml(Object from, Element to, PojoModelFactory factory)
            throws IOException {
        pojoToXml(from, to, factory, 0);
    }

    public static void pojoToXml(Object from, Element to, PojoModelFactory factory, int level)
            throws IOException {
        if (level > MAX_LEVEL) return;
        PojoModel model = factory.createPojoModel(from.getClass());
        for (PojoAttribute<?> attr : model) {

            try {
                if (!attr.canRead()) continue;

                Object value = attr.get(from);
                String name = attr.getName();

                Element a = to.getOwnerDocument().createElement("attribute");
                to.appendChild(a);
                a.setAttribute("name", name);

                if (value == null) {
                    a.setAttribute("null", "true");
                    // to.setAttribute(name, (String)null);
                } else if (value instanceof Boolean)
                    a.setAttribute("boolean", MCast.toString((boolean) value));
                else if (value instanceof Integer)
                    a.setAttribute("int", MCast.toString((int) value));
                else if (value instanceof Long)
                    a.setAttribute("long", MCast.toString((long) value));
                else if (value instanceof Date)
                    a.setAttribute("date", MCast.toString(((Date) value).getTime()));
                else if (value instanceof String) {
                    if (hasValidChars((String) value)) a.setAttribute("string", (String) value);
                    else {
                        a.setAttribute("encoding", "base64");
                        a.setAttribute("string", Base64.encode((String) value));
                    }
                } else if (value.getClass().isEnum()) {
                    a.setAttribute("enum", MCast.toString(((Enum<?>) value).ordinal()));
                    a.setAttribute("value", ((Enum<?>) value).name());
                } else if (value instanceof UUID) {
                    a.setAttribute("uuid", ((UUID) value).toString());
                } else if (value instanceof Serializable) {
                    a.setAttribute("serializable", "true");

                    CDATASection cdata = a.getOwnerDocument().createCDATASection("");
                    String data = MCast.toBinaryString(MCast.toBinary(value));
                    cdata.setData(data);
                    a.appendChild(cdata);
                } else {
                    a.setAttribute("type", value.getClass().getCanonicalName());
                    pojoToXml(value, a, factory, level + 1);
                }

            } catch (Throwable t) {
                log.d(MSystem.getClassName(from), attr.getName(), t);
            }
        }
    }

    private static boolean hasValidChars(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\n' || c == '\r' || c == '\t' || c >= 32 && c <= 55295) {
            } else {
                return false;
            }
        }
        return true;
    }

    public static void xmlToPojo(Element from, Object to, MActivator act) throws IOException {
        xmlToPojo(from, to, getDefaultModelFactory(), act, false);
    }

    public static void xmlToPojo(Element from, Object to, MActivator act, boolean force)
            throws IOException {
        xmlToPojo(from, to, getDefaultModelFactory(), act, force);
    }

    public static void xmlToPojo(Element from, Object to, PojoModelFactory factory, MActivator act)
            throws IOException {
        xmlToPojo(from, to, factory, act, false);
    }

    @SuppressWarnings("unchecked")
    public static void xmlToPojo(
            Element from, Object to, PojoModelFactory factory, MActivator act, boolean force)
            throws IOException {
        PojoModel model = factory.createPojoModel(to.getClass());

        HashMap<String, Element> index = new HashMap<>();
        for (Element e : MXml.getLocalElementIterator(from, "attribute"))
            index.put(e.getAttribute("name"), e);

        for (PojoAttribute<Object> attr : model) {

            try {
                if (!attr.canWrite()) continue;

                String name = attr.getName();
                //			Class<?> type = attr.getType();
                Element a = index.get(name);
                if (a == null) {
                    log.d("attribute not found", name, to.getClass());
                    continue;
                }
                {
                    String value = a.getAttribute("null");
                    if (MString.isSet(value) && value.equals("true")) {
                        attr.set(to, null, force);
                        continue;
                    }
                }
                if (a.hasAttribute("string")) {
                    String data = a.getAttribute("encoding");
                    if ("base64".equals(data)) {
                        String value = new String(Base64.decode(a.getAttribute("string")));
                        attr.set(to, value, force);
                    } else {
                        String value = a.getAttribute("string");
                        attr.set(to, value, force);
                    }
                    continue;
                }
                if (a.hasAttribute("boolean")) {
                    String value = a.getAttribute("boolean");
                    attr.set(to, MCast.toboolean(value, false), force);
                    continue;
                }
                if (a.hasAttribute("int")) {
                    String value = a.getAttribute("int");
                    attr.set(to, MCast.toint(value, 0), force);
                    continue;
                }
                if (a.hasAttribute("long")) {
                    String value = a.getAttribute("long");
                    attr.set(to, MCast.tolong(value, 0), force);
                    continue;
                }
                if (a.hasAttribute("date")) {
                    String value = a.getAttribute("date");
                    Date obj = new Date();
                    obj.setTime(MCast.tolong(value, 0));
                    attr.set(to, obj, force);
                    continue;
                }
                if (a.hasAttribute("uuid")) {
                    String value = a.getAttribute("uuid");
                    try {
                        attr.set(to, UUID.fromString(value), force);
                    } catch (Throwable t) {
                        log.d(name, t);
                    }
                    continue;
                }
                if (a.hasAttribute("enum")) {
                    String value = a.getAttribute("enum");
                    attr.set(to, MCast.toint(value, 0), force);
                    continue;
                }
                if ("true".equals(a.getAttribute("serializable"))) {
                    CDATASection cdata = MXml.findCDataSection(a);
                    if (cdata != null) {
                        String data = cdata.getData();
                        try {
                            Object obj = MCast.fromBinary(MCast.fromBinaryString(data));
                            attr.set(to, obj, force);
                        } catch (ClassNotFoundException e1) {
                            throw new IOException(e1);
                        }
                    }
                }
                if (a.hasAttribute("type")) {
                    String value = a.getAttribute("type");
                    try {
                        Object obj = act.createObject(value);
                        xmlToPojo(a, obj, factory, act);
                        attr.set(to, obj, force);
                    } catch (Exception e1) {
                        log.d(name, to.getClass(), e1);
                    }
                    continue;
                }

            } catch (Throwable t) {
                log.d(MSystem.getClassName(to), attr.getName(), t);
            }
        }
    }

    /**
     * Functionize a String. Remove bad names and set first characters to upper. Return def if the
     * name can't be created, e.g. only numbers.
     *
     * @param in
     * @param firstUpper
     * @param def
     * @return The function name
     */
    public static String toFunctionName(String in, boolean firstUpper, String def) {
        if (MString.isEmpty(in)) return def;
        boolean first = firstUpper;
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_') {
                if (first) c = Character.toUpperCase(c);
                first = false;
                out.append(c);
            } else if (!first && c >= '0' && c <= '9') {
                out.append(c);
            } else {
                first = true;
            }
        }

        if (out.length() == 0) return def;
        return out.toString();
    }

    public static IProperties pojoToProperties(Object from) throws IOException {
        return pojoToProperties(from, getDefaultModelFactory());
    }

    public static IProperties pojoToProperties(Object from, PojoModelFactory factory)
            throws IOException {
        MProperties out = new MProperties();
        PojoModel model = factory.createPojoModel(from.getClass());

        for (PojoAttribute<?> attr : model) {

            try {
                if (!attr.canRead()) continue;

                Object value = attr.get(from);

                String name = attr.getName();
                Class<?> type = attr.getType();
                if (type == int.class) out.setInt(name, (int) value);
                else if (type == Integer.class) out.setInt(name, (Integer) value);
                else if (type == long.class) out.setLong(name, (long) value);
                else if (type == Long.class) out.setLong(name, (Long) value);
                else if (type == float.class) out.setFloat(name, (float) value);
                else if (type == Float.class) out.setFloat(name, (Float) value);
                else if (type == double.class) out.setDouble(name, (double) value);
                else if (type == Double.class) out.setDouble(name, (Double) value);
                else if (type == boolean.class) out.setBoolean(name, (boolean) value);
                else if (type == Boolean.class) out.setBoolean(name, (Boolean) value);
                else if (type == String.class) out.setString(name, (String) value);
                else if (type == Date.class) out.setDate(name, (Date) value);
                else out.setString(name, String.valueOf(value));

            } catch (Throwable t) {
                log.d(MSystem.getClassName(from), attr.getName(), t);
            }
        }
        return out;
    }

    public static void propertiesToPojo(IProperties from, Object to) throws IOException {
        propertiesToPojo(from, to, getDefaultModelFactory(), null, false);
    }

    public static void propertiesToPojo(IProperties from, Object to, boolean force)
            throws IOException {
        propertiesToPojo(from, to, getDefaultModelFactory(), null, force);
    }

    public static void propertiesToPojo(IProperties from, Object to, PojoModelFactory factory)
            throws IOException {
        propertiesToPojo(from, to, factory, null, false);
    }

    @SuppressWarnings("unchecked")
    public static void propertiesToPojo(
            IProperties from,
            Object to,
            PojoModelFactory factory,
            Caster<Object, Object> unknownHadler,
            boolean force)
            throws IOException {
        PojoModel model = factory.createPojoModel(to.getClass());
        for (PojoAttribute<Object> attr : model) {

            if (!attr.canWrite()) continue;

            String name = attr.getName();
            Class<?> type = attr.getType();
            try {
                if (!from.isProperty(name) || !attr.canWrite()) {

                } else if (type == Boolean.class || type == boolean.class)
                    attr.set(to, from.getBoolean(name, false), force);
                else if (type == Integer.class || type == int.class)
                    attr.set(to, from.getInt(name, 0), force);
                else if (type == String.class) attr.set(to, from.getString(name, null), force);
                else if (type == UUID.class)
                    try {
                        attr.set(to, UUID.fromString(from.getString(name)), force);
                    } catch (IllegalArgumentException e) {
                        attr.set(to, null, force);
                    }
                else if (type.isEnum()) {
                    Object[] cons = type.getEnumConstants();
                    int ord = from.getInt(name, 0);
                    Object c = cons.length > 0 ? cons[0] : null;
                    if (ord >= 0 && ord < cons.length) c = cons[ord];
                    attr.set(to, c, force);
                } else
                    attr.set(
                            to,
                            unknownHadler == null
                                    ? from.getString(name)
                                    : unknownHadler.cast(from.get(name), null),
                            force);
            } catch (Throwable t) {
                log.d(MSystem.getClassName(to), name, t);
            }
        }
    }

    /**
     * toAttributeName.
     *
     * @param idents
     * @return a {@link java.lang.String} object.
     * @since 3.3.0
     */
    public static String toAttributeName(Identifier... idents) {
        if (idents == null) return null;
        if (idents.length == 0) return "";
        if (idents.length == 1) return idents[0].getPojoName();
        StringBuilder out = new StringBuilder();
        for (Identifier ident : idents) {
            if (out.length() > 0) out.append('_');
            out.append(ident.getPojoName());
        }
        return out.toString();
    }

    @SuppressWarnings("unchecked")
    public static void propertiesToPojo(Map<String, String> from, Object to, TransformHelper helper)
            throws IOException {
        PojoModel model = helper.createPojoModel(from);
        for (PojoAttribute<Object> attr : model) {
            String name = attr.getName();
            String value = from.get(name);
            if (value != null) {
                attr.set(to, value, helper.isForce());
            }
        }
    }

    public static void pojoToObjectStream(Object from, ObjectOutputStream to) throws IOException {
        pojoToObjectStream(from, to, getDefaultModelFactory());
    }

    public static void pojoToObjectStream(
            Object from, ObjectOutputStream to, PojoModelFactory factory) throws IOException {
        PojoModel model = factory.createPojoModel(from.getClass());
        for (PojoAttribute<?> attr : model) {
            String name = attr.getName();
            Object value = attr.get(from);
            to.writeObject(name);
            to.writeObject(value);
        }
        to.writeObject(" ");
    }

    public static void objectStreamToPojo(ObjectInputStream from, Object to)
            throws IOException, ClassNotFoundException {
        objectStreamToPojo(from, to, getDefaultModelFactory(), false);
    }

    public static void objectStreamToPojo(ObjectInputStream from, Object to, boolean force)
            throws IOException, ClassNotFoundException {
        objectStreamToPojo(from, to, getDefaultModelFactory(), force);
    }

    public static void objectStreamToPojo(
            ObjectInputStream from, Object to, PojoModelFactory factory)
            throws IOException, ClassNotFoundException {
        objectStreamToPojo(from, to, factory, false);
    }

    @SuppressWarnings("unchecked")
    public static void objectStreamToPojo(
            ObjectInputStream from, Object to, PojoModelFactory factory, boolean force)
            throws IOException, ClassNotFoundException {
        PojoModel model = factory.createPojoModel(to.getClass());
        while (true) {
            String name = (String) from.readObject();
            if (name.equals(" ")) break;
            Object value = from.readObject();
            @SuppressWarnings("rawtypes")
            PojoAttribute attr = model.getAttribute(name);
            if (attr != null) attr.set(to, value, force);
        }
    }

    public static void base64ToObject(String content, Object obj, PojoModelFactory factory)
            throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(content));
        ObjectInputStream ois = new ObjectInputStream(bais);
        MPojo.objectStreamToPojo(ois, obj, factory);
    }

    public static String objectToBase64(Object obj, PojoModelFactory factory) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        MPojo.pojoToObjectStream(obj, oos, factory);

        return Base64.encode(baos.toByteArray());
    }
}
