package de.mhus.test.forms03;

import java.util.Date;

import de.mhus.lib.annotations.form.ALayoutModel;
import de.mhus.lib.annotations.vaadin.Column;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.form.ui.FmText;

public class DemoEntity {

	private String id;
	private String text;
	private Date date;
	private String firstName;
	private String lastName;
	
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
	@Column(order=1,title="First Name")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	@Column(order=2,title="Last Name")
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@ALayoutModel
	public DefRoot model() {
		return new DefRoot(
				new FmText("firstName","Vorname","Bla bla..."),
				new FmText("lastName","Nachname","Bla bla...")
				);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
