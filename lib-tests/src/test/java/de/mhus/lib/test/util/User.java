package de.mhus.lib.test.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import de.mhus.lib.annotations.generic.Public;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;

public class User {

    @Public
	private String id;
    @Public
	private String salutation;
    @Public
	private String firstname;
    @Public
	private String lastname;
    @Public
	private String email;
    @Public
	private String password;
    @Public
	private String phone;
    @Public
	private String mobile;
    @Public
	private String street;
    @Public
	private String streetNumber;
    @Public
	private String zip;
    @Public
	private String town;
    @Public
	private String company;
    @Public
	private String language;
    @Public
	private String note;
    @Public
	private boolean enabled = true;
    @Public
	private Date birthday;
    @Public
	private List<String> tags;
    @Public(readable = false)
    private JsonNode node;
	
	public User() {
		tags = new LinkedList<>();
	}
	
	public User(JsonNode node) {
		tags = new LinkedList<>();
		if (node.get("_id") != null)
			this.id = node.get("_id").asText();
		if (node.get("firstname") != null)
			this.firstname = node.get("firstname").asText(); 
		if (node.get("lastname") != null)
			this.lastname = node.get("lastname").asText();
		if (node.get("email") != null)
			this.email = node.get("email").asText();
		if (node.get("password") != null)
			this.password = node.get("password").asText();
		if (node.get("salutation") != null)
			this.salutation = node.get("salutation").asText();
		if (node.get("phone") != null)
			this.phone = node.get("phone").asText();
		if (node.get("mobile") != null)
			this.mobile = node.get("mobile").asText();
		if (node.get("street") != null)
			this.street = node.get("street").asText();
		if (node.get("streetnumber") != null)
			this.streetNumber = node.get("streetnumber").asText();
		if (node.get("zip") != null)
			this.zip = node.get("zip").asText();
		if (node.get("town") != null)
			this.town = node.get("town").asText();
		if (node.get("company") != null)
			this.company = node.get("company").asText();
		if (node.get("language") != null)
			this.language = node.get("language").asText();
		if (node.get("note") != null)
			this.note = node.get("note").asText();
		if (node.get("enabled") != null)
			this.enabled = MCast.toboolean(node.get("enabled").asText(), true);
		if (node.get("birthday") != null)
			this.birthday = MCast.toDate(node.get("birthday").asText(), null);
		if (node.get("tags") != null) {
			Iterator<JsonNode> iter = node.get("tags").elements();
			while (iter.hasNext()) {
				tags.add(iter.next().asText());
			}
		}
		this.node = node;
	}
	
	public User(IProperties p) {
		tags = new LinkedList<>();
		this.id = p.getString("id", null);
		this.firstname = p.getString("firstname", null);
		this.lastname = p.getString("lastname", null);
		this.salutation = p.getString("salutation", null);
		this.email = p.getString("email", null);
		this.password = p.getString("password", null);
		this.phone = p.getString("phone", null);
		this.mobile = p.getString("mobile", null);
		this.street = p.getString("street", null);
		if (MString.isSet(p.getString("streetNumber", null)) || MString.isSet(p.getString("streetnumber", null)))
			this.streetNumber = p.getString("streetNumber", p.getString("streetnumber", null));
		this.zip = p.getString("zip", null);
		this.town = p.getString("town", null);
		this.company = p.getString("company", null);
		this.language = p.getString("language", null);
		this.note = p.getString("note", null);
		if (MString.isSet(p.getString("birthday", null)))
			this.birthday = p.getDate("birthday");
		this.enabled = p.getBoolean("enabled", true);
		if (MString.isSet(p.getString("tags", null))) {
			String[] parts = MString.split(p.getString("tags", null), ",");
			for (String part : parts) {
				this.tags.add(part.trim());
			}
		}
	}

	public JsonNode getNode() {
	    return node;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		if (MString.isSet(id))
			map.put("_id", id);
		if (MString.isSet(salutation))
			map.put("salutation", salutation);
		if (MString.isSet(firstname))
			map.put("firstname", firstname);
		if (MString.isSet(lastname))
			map.put("lastname", lastname);
		if (MString.isSet(email))
			map.put("email", email);
		if (MString.isSet(password))
			map.put("password", password);
		if (MString.isSet(phone))
			map.put("phone", phone);
		if (MString.isSet(mobile))
			map.put("mobile", mobile);
		if (MString.isSet(street))
			map.put("street", street);
		if (MString.isSet(streetNumber))
			map.put("streetnumber", streetNumber);
		if (MString.isSet(zip))
			map.put("zip", zip);
		if (MString.isSet(town))
			map.put("town", town);
		if (MString.isSet(company))
			map.put("company", company);
		if (MString.isSet(language))
			map.put("language", language);
		if (MString.isSet(note))
			map.put("note", note);
		if (birthday != null)
			map.put("birthday", birthday);
		map.put("enabled", enabled);
		if (tags != null && !tags.isEmpty())
			map.put("tags", tags);
		
		return map;
	}
	
	@Override
	public String toString() {
		return toMap().toString();
	}
}