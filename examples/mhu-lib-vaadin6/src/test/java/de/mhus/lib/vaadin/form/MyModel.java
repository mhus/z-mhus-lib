package de.mhus.lib.vaadin.form;

import de.mhus.lib.annotations.form.ALayoutModel;
import de.mhus.lib.annotations.pojo.Action;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.definition.FmDefaultSources;
import de.mhus.lib.form.definition.FmNls;
import de.mhus.lib.vaadin.form2.FmCombobox;
import de.mhus.lib.vaadin.form2.FmInformation;
import de.mhus.lib.vaadin.form2.FmPassword;
import de.mhus.lib.vaadin.form2.FmText;

public class MyModel {

	private String user = "U12345";
	private String password = "nanan";
	private String sex = "m";
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		System.out.println("MODEL User:" + user);
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		System.out.println("MODEL Password:" + password);
		this.password = password;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		System.out.println("MODEL Sex:" + sex);
		this.sex = sex;
	}
	public String[] getSexOptions() { 
		return new String[] {"m=Male", "f=Female"};
	}
	
	@ALayoutModel
	@Action
	public DefRoot createModel() throws MException {
		return new DefRoot(
				new FmInformation()
				,
				new FmText("user",new FmNls("User","Beschreibung 1"),new FmDefaultSources())
				,
				new FmPassword("password",new FmNls("Password","Beschreibung 2"),new FmDefaultSources())
				,
//				new FmNumber("number",FmNumber.TYPE.INTEGER,new FmNls("text3","Nr3","Beschreibung 3")).format(FmNumber.FORMAT.CURRENCY)
//				,
				new FmCombobox("sex", new FmNls("Sex","Geschlecht"),new FmDefaultSources())
				).build();
	}
	
	
}
