package de.mhus.test.forms03;

import java.util.Date;

import de.mhus.lib.annotations.form.ALayoutModel;
import de.mhus.lib.annotations.vaadin.Column;
import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.form.Item;
import de.mhus.lib.form.ui.FmCombobox;
import de.mhus.lib.form.ui.FmText;

public class DemoEntity {

	private String id;
	private String text;
	private Date date;
	private String firstName;
	private String lastName;
	private String gender;
	
	public DemoEntity() {
		
	}
	
	public DemoEntity(DemoEntity demo) {
		save(demo);
	}
	
	public void save(DemoEntity demo) {
		this.id = demo.id;
		this.firstName = demo.firstName;
		this.lastName = demo.lastName;
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
				new DefAttribute("layout","50x50"),
				new FmText("firstName","firstname=First name","Bla bla...", new DefAttribute("columns", "2") ),
				new FmText("lastName","lastname=Last name","Bla bla..."),
				new FmCombobox("gender", "gender=Geschlecht","Bla bla")
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
	
}
