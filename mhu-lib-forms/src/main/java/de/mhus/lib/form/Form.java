package de.mhus.lib.form;

import java.util.Locale;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.errors.MException;

public class Form {

	private Locale locale = Locale.getDefault();
	private ComponentAdapterProvider adapterProvider;
	protected IConfig model;
	private DataSource dataSource;
	private MNls nls;
	private FormControl control;
	private UiInformation informationPane;
	
	public Form() {
		
	}
	public Form(Locale locale, ComponentAdapterProvider adapterProvider, IConfig model) {
		this.locale = locale;
		this.adapterProvider = adapterProvider;
		this.model = model;
	}

	public Form(DefRoot model) throws MException {
		this.model	= model;
		model.build();
	}
	
	public Locale getLocale() {
		return locale;
	}

	public ComponentAdapterProvider getAdapterProvider() {
		return adapterProvider;
	}

	public IConfig getModel() {
		return model;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public MNls getNls() {
		return nls;
	}

	public void setNls(MNls nls) {
		this.nls = nls;
	}

	public void setAdapterProvider(ComponentAdapterProvider adapterProvider) {
		this.adapterProvider = adapterProvider;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public FormControl getControl() {
		if (control == null) setControl(new FormControlAdapter());
		return control;
	}
	
	public void setControl(FormControl control) {
		this.control = control;
		if (control != null) control.attachedForm(this);
	}
	public UiInformation getInformationPane() {
		return informationPane;
	}
	public void setInformationPane(UiInformation informationPane) {
		this.informationPane = informationPane;
	}
	
}
