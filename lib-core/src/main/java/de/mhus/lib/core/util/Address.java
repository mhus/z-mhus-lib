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
package de.mhus.lib.core.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Locale;

import de.mhus.lib.annotations.generic.Public;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.M;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.node.INode;
import de.mhus.lib.core.node.INodeFactory;
import de.mhus.lib.errors.MException;

public class Address implements Externalizable {

    public enum SALUTATION {
        OTHER,
        MR,
        MRS,
        COMPANY,
        AGENCY,
        COUPLE
    }

    private static INode definition;

    private static HashMap<String, SALUTATION> mapping;

    private static HashMap<String, INode> locales;

    @Public protected IProperties attributes;

    public Address() {}

    public Address(IProperties attributes) {
        this.attributes = attributes;
    }

    public Address.SALUTATION getSalutation() {
        return toSalutation(attributes.getString(M.ADDR_SALUTATION, null));
    }

    public String getEmail() {
        return attributes.getString(M.ADDR_EMAIL, null);
    }

    public String getNameTitle() {
        return attributes.getString(M.ADDR_NAME_TITLE, null);
    }

    public String getFirstName() {
        return attributes.getString(M.ADDR_FIRST_NAME, null);
    }

    public String getLastName() {
        return attributes.getString(M.ADDR_LAST_NAME, null);
    }

    public String getNameMid() {
        return attributes.getString(M.ADDR_NAME_MID, null);
    }

    public String getNameAffix() {
        return attributes.getString(M.ADDR_NAME_AFFIX, null);
    }

    public String getStreet() {
        return attributes.getString(M.ADDR_STREET, null);
    }

    public String getHouseNumber() {
        return attributes.getString(M.ADDR_HOUSE_NUMBER, null);
    }

    public String getTown() {
        return attributes.getString(M.ADDR_TOWN, null);
    }

    public String getCountry() {
        return attributes.getString(M.ADDR_COUNTRY, null);
    }

    public String getZip() {
        return attributes.getString(M.ADDR_ZIP, null);
    }

    public String getPhone() {
        return attributes.getString(M.ADDR_PHONE, null);
    }

    public String getMobile() {
        return attributes.getString(M.ADDR_MOBILE, null);
    }

    public String getFullName(Locale l) {
        INode locale = getLocaleConfig(l);
        if (locale != null) {
            INode cAssemble = locale.getObjectOrNull("assemble");
            if (cAssemble != null) {
                INode cSal = cAssemble.getObjectOrNull(getSalutation().name().toLowerCase());
                if (cSal != null) {
                    String template = cSal.getString(INode.NAMELESS_VALUE, "");
                    String out = MString.compileAndExecute(template, getAttributes(), "");
                    while (out.indexOf("  ") > -1) out = out.replace("  ", " ");
                    return out;
                }
            }
        }

        // fallback
        return getFirstName() + " " + getLastName();
    }

    private static INode getLocaleConfig(Locale l) {
        getDefinition();
        if (l == null) l = Locale.getDefault();
        INode locale = locales.get(l.getLanguage());
        if (locale == null) locale = locales.get("en");
        return locale;
    }

    public static SALUTATION toSalutation(int salutation) {
        if (salutation >= SALUTATION.values().length || salutation < 0) salutation = 0;
        return SALUTATION.values()[salutation];
    }

    public static int toSalutationInt(String salStr) {
        getDefinition();
        int salutation = MCast.toint(salStr, -1);
        if (salStr != null && salutation == -1) {
            salStr = salStr.trim().toLowerCase();
            getDefinition();
            SALUTATION sal = mapping.get(salStr);
            if (sal == null) return 0;
            return sal.ordinal();
        }
        if (salutation >= SALUTATION.values().length || salutation < 0) salutation = 0;
        return salutation;
    }

    public static String toSalutationString(SALUTATION salutation, Locale l) {
        INode locale = getLocaleConfig(l);
        if (locale != null) {
            INode cSingular = locale.getObjectOrNull("singular");
            if (cSingular != null) {
                INode cSal = cSingular.getObjectOrNull(salutation.name().toLowerCase());
                if (cSal != null) {
                    return cSal.getString(INode.NAMELESS_VALUE, "");
                }
            }
        }

        // fallback
        switch (salutation) {
            case AGENCY:
                return "Agency";
            case COMPANY:
                return "Company";
            case COUPLE:
                return "Couple";
            case MR:
                return "Mr";
            case MRS:
                return "Mrs";
            case OTHER:
            default:
                return "";
        }
    }

    public String getLetterSalutation(Locale l) {
        INode locale = getLocaleConfig(l);
        if (locale != null) {
            INode cLetter = locale.getObjectOrNull("letter");
            if (cLetter != null) {
                INode cSal = cLetter.getObjectOrNull(getSalutation().name().toLowerCase());
                if (cSal != null) {
                    String template = cSal.getString(INode.NAMELESS_VALUE, "");
                    return MString.compileAndExecute(template, getAttributes(), "");
                }
            }
        }

        // fallback
        switch (getSalutation()) {
            case AGENCY:
            case COMPANY:
            case COUPLE:
            case OTHER:
            default:
                return "Dear Sir or Madam";
            case MR:
                return "Dear Sir";
            case MRS:
                return "Dear Madam";
        }
    }

    private IProperties getAttributes() {
        return attributes;
    }

    public static SALUTATION toSalutation(String salStr) {
        int salutation = toSalutationInt(salStr);
        if (salutation < 0 || salutation >= SALUTATION.values().length) salutation = 0;
        return SALUTATION.values()[salutation];
    }

    public static synchronized INode getDefinition() {
        if (definition == null) {
            try {
                definition = M.l(INodeFactory.class).read(Address.class, "address.xml");
            } catch (MException e) {
                MLogUtil.log().w(Address.class, e);
                definition = M.l(INodeFactory.class).create(); // empty config
            }

            // load data
            mapping = new HashMap<>();
            locales = new HashMap<>();
            for (INode locale : definition.getObjectList("locale")) {
                String lang = locale.getString("language", "").toLowerCase();
                locales.put(lang, locale);
                {
                    INode cMapping = locale.getObjectOrNull("mapping");
                    if (cMapping != null) {
                        for (INode cMap : cMapping.getObjects()) {
                            try {
                                SALUTATION sal = SALUTATION.valueOf(cMap.getName().toUpperCase());
                                String val = cMap.getString(INode.NAMELESS_VALUE, null);
                                if (MString.isSet(val)) mapping.put(val.toLowerCase().trim(), sal);
                            } catch (Throwable t) {
                            }
                        }
                    }
                }
                {
                    INode cMapping = locale.getObjectOrNull("plural");
                    if (cMapping != null) {
                        for (INode cMap : cMapping.getObjects()) {
                            try {
                                SALUTATION sal = SALUTATION.valueOf(cMap.getName().toUpperCase());
                                String val = cMap.getString(INode.NAMELESS_VALUE, null);
                                if (MString.isSet(val)) mapping.put(val.toLowerCase().trim(), sal);
                            } catch (Throwable t) {
                            }
                        }
                    }
                }
                {
                    INode cMapping = locale.getObjectOrNull("singular");
                    if (cMapping != null) {
                        for (INode cMap : cMapping.getObjects()) {
                            try {
                                SALUTATION sal = SALUTATION.valueOf(cMap.getName().toUpperCase());
                                String val = cMap.getString(INode.NAMELESS_VALUE, null);
                                if (MString.isSet(val)) mapping.put(val.toLowerCase().trim(), sal);
                            } catch (Throwable t) {
                            }
                        }
                    }
                }
            }
        }

        return definition;
    }

    public static void reloadDefinition() {
        definition = null;
        getDefinition();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(attributes);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        attributes = (IProperties) in.readObject();
    }

    public static String[] separateStreetFromNumber(String street) {
        if (street == null) return new String[] {null, null};
        int p = street.lastIndexOf(' ');
        if (p < 0) return new String[] {street, null};
        String xstreet = street.substring(0, p).trim();
        String xnr = street.substring(p).trim();
        return new String[] {xstreet, xnr};
    }
}
