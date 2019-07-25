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
package de.mhus.lib.core.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Locale;

import de.mhus.lib.annotations.generic.Public;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.MConfig;
import de.mhus.lib.core.config.XmlConfig;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.errors.MException;

public class Address implements Externalizable {

	public enum SALUTATION {
		OTHER, MR, MRS, COMPANY, AGENCY, COUPLE
	}

	private static IConfig definition;

	private static HashMap<String, SALUTATION> mapping;

	private static HashMap<String, IConfig> locales;

	@Public
	protected IProperties attributes;

	public Address() {
	}
	
	public Address(IProperties attributes) {
		this.attributes = attributes;
	}

	public Address.SALUTATION getSalutation() {
		return toSalutation(attributes.getString(MConstants.ADDR_SALUTATION, null));
	}

	public String getEmail() {
		return attributes.getString(MConstants.ADDR_EMAIL, null);
	}

	public String getNameTitle() {
		return attributes.getString(MConstants.ADDR_NAME_TITLE, null);
	}

	public String getFirstName() {
		return attributes.getString(MConstants.ADDR_FIRST_NAME, null);
	}

	public String getLastName() {
		return attributes.getString(MConstants.ADDR_LAST_NAME, null);
	}

	public String getNameMid() {
		return attributes.getString(MConstants.ADDR_NAME_MID, null);
	}

	public String getNameAffix() {
		return attributes.getString(MConstants.ADDR_NAME_AFFIX, null);
	}

	public String getStreet() {
		return attributes.getString(MConstants.ADDR_STREET, null);
	}

	public String getHouseNumber() {
		return attributes.getString(MConstants.ADDR_HOUSE_NUMBER, null);
	}

	public String getTown() {
		return attributes.getString(MConstants.ADDR_TOWN, null);
	}

	public String getCountry() {
		return attributes.getString(MConstants.ADDR_COUNTRY, null);
	}

	public String getZip() {
		return attributes.getString(MConstants.ADDR_ZIP, null);
	}

	public String getPhone() {
		return attributes.getString(MConstants.ADDR_PHONE, null);
	}

	public String getMobile() {
		return attributes.getString(MConstants.ADDR_MOBILE, null);
	}

	public String getFullName(Locale l) {
		IConfig locale = getLocaleConfig(l);
		if (locale != null) {
			IConfig cAssemble = locale.getNode("assemble");
			if (cAssemble != null) {
				IConfig cSal = cAssemble.getNode(getSalutation().name().toLowerCase());
				if (cSal != null) {
					String template = cSal.getString(XmlConfig.VALUE, "");
					String out = MString.compileAndExecute(template,getAttributes(),"");
					while (out.indexOf("  ") > -1)
						out = out.replace("  ", " ");
					return out;
				}
			}
		}
		
		// fallback
		return getFirstName() + " " + getLastName();
	}
	
	
	private static IConfig getLocaleConfig(Locale l) {
		getDefinition();
		if (l == null) l = Locale.getDefault();
		IConfig locale = locales.get(l.getLanguage());
		if (locale == null)
			locale = locales.get("en");
		return locale;
	}

	public static SALUTATION toSalutation(int salutation) {
		if (salutation >= SALUTATION.values().length || salutation < 0)
			salutation = 0;
		return SALUTATION.values()[salutation];
	}
	
	public static int toSalutationInt(String salStr) {
		getDefinition();
		int salutation = MCast.toint(salStr, -1);
		if (salStr != null && salutation == -1) {
			salStr = salStr.trim().toLowerCase();
			getDefinition();
			SALUTATION sal = mapping.get(salStr);
			if (sal == null)
				return 0;
			return sal.ordinal();
		}
		if (salutation >= SALUTATION.values().length || salutation < 0)
			salutation = 0;
		return salutation;
	}

	public static String toSalutationString(SALUTATION salutation, Locale l) {
		IConfig locale = getLocaleConfig(l);
		if (locale != null) {
			IConfig cSingular = locale.getNode("singular");
			if (cSingular != null) {
				IConfig cSal = cSingular.getNode(salutation.name().toLowerCase());
				if (cSal != null) {
					return cSal.getString(XmlConfig.VALUE, "");
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
		IConfig locale = getLocaleConfig(l);
		if (locale != null) {
			IConfig cLetter = locale.getNode("letter");
			if (cLetter != null) {
				IConfig cSal = cLetter.getNode(getSalutation().name().toLowerCase());
				if (cSal != null) {
					String template = cSal.getString(XmlConfig.VALUE, "");
					return MString.compileAndExecute(template,getAttributes(),"");
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
		if (salutation < 0 || salutation >= SALUTATION.values().length)
			salutation = 0;
		return SALUTATION.values()[salutation];
	}

	public static synchronized IConfig getDefinition() {
		if (definition == null) {
			try {
				definition = MConfig.createFromResource(Address.class, "address.xml");
			} catch (MException e) {
				MLogUtil.log().w(Address.class, e);
				definition = new XmlConfig(); // empty config
			}

			// load data
			mapping = new HashMap<>();
			locales = new HashMap<>();
			for (IConfig locale : definition.getNodes("locale")) {
				String lang = locale.getString("language", "").toLowerCase();
				locales.put(lang, locale);
				{
					IConfig cMapping = locale.getNode("mapping");
					if (cMapping != null) {
						for (IConfig cMap : cMapping.getNodes()) {
							try {
								SALUTATION sal = SALUTATION.valueOf(cMap.getName().toUpperCase());
								String val = cMap.getString(XmlConfig.VALUE, null);
								if (MString.isSet(val))
									mapping.put(val.toLowerCase().trim(), sal);
							} catch (Throwable t) {
							}
						}
					}
				}
				{
					IConfig cMapping = locale.getNode("plural");
					if (cMapping != null) {
						for (IConfig cMap : cMapping.getNodes()) {
							try {
								SALUTATION sal = SALUTATION.valueOf(cMap.getName().toUpperCase());
								String val = cMap.getString(XmlConfig.VALUE, null);
								if (MString.isSet(val))
									mapping.put(val.toLowerCase().trim(), sal);
							} catch (Throwable t) {
							}
						}
					}
				}
				{
					IConfig cMapping = locale.getNode("singular");
					if (cMapping != null) {
						for (IConfig cMap : cMapping.getNodes()) {
							try {
								SALUTATION sal = SALUTATION.valueOf(cMap.getName().toUpperCase());
								String val = cMap.getString(XmlConfig.VALUE, null);
								if (MString.isSet(val))
									mapping.put(val.toLowerCase().trim(), sal);
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
        if (street == null) return new String[] {null,null};
        int p = street.lastIndexOf(' ');
        if (p < 0) return new String[] {street,null};
        String xstreet = street.substring(0, p).trim();
        String xnr = street.substring(p).trim();
        return new String[] {xstreet,xnr};
    }

}
