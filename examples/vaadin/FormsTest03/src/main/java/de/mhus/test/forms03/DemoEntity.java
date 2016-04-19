package de.mhus.test.forms03;

import java.util.Date;
import java.util.Set;

import de.mhus.lib.annotations.form.ALayoutModel;
import de.mhus.lib.annotations.vaadin.Column;
import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.form.Item;
import de.mhus.lib.form.definition.FmColumns;
import de.mhus.lib.form.definition.FmCombobox;
import de.mhus.lib.form.definition.FmLayout50x50;
import de.mhus.lib.form.definition.FmOptions;
import de.mhus.lib.form.definition.FmRootLayout50x50;
import de.mhus.lib.form.definition.FmText;

public class DemoEntity {

	private String id;
	private String text;
	private Date date;
	private String firstName;
	private String lastName;
	private String gender;
	private Set<String> hobbies;
	
	public DemoEntity() {
		
	}
	
	public DemoEntity(DemoEntity demo) {
		save(demo);
	}
	
	public void save(DemoEntity demo) {
		this.id = demo.id;
		this.firstName = demo.firstName;
		this.lastName = demo.lastName;
		this.hobbies = demo.hobbies;
	}
	
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Column(order=1,title="First Name",nls="firstname")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	@Column(order=2,title="Last Name",nls="lastname")
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@ALayoutModel
	public DefRoot model() {
		return new DefRoot( 
				new FmRootLayout50x50(),
				new FmText("firstName","firstname=First name","Bla bla...", new FmColumns(2) ),
				new FmText("lastName","lastname=Last name","Bla bla..."),
				new FmCombobox("gender", "gender=Geschlecht","Bla bla"),
				new FmOptions("hobbies", "Hobbies", "Deine Hobbies")
				);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getGender() {
		return gender;
	}

	public Item[] getGenderItems() {
		return new Item[] {
				new Item("m","male=Male"),
				new Item("f","female=Female")
		};
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public Set<String> getHobbies() {
		return hobbies;
	}
	
	public void setHobbies(Set<String> in) {
		this.hobbies = in;
	}
	
	public Item[] getHobbiesItems() {
		return new Item[] {
				new Item("l","Lesen"),
				new Item("m","Malen"),
				new Item("r", "Radfahren")
		};
	}
	
}
