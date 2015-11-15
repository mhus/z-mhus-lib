package de.mhus.lib.form;

import java.util.Locale;

import de.mhus.lib.core.config.IConfig;

public class Form {

	private Locale locale = Locale.getDefault();
	private ComponentAdapterProvider adapterProvider;
	private IConfig model;
	private DataSource dataSource;
	private NlsProvider nlsProvider;
	
	public Form(Locale locale, ComponentAdapterProvider adapterProvider, IConfig model) {
		this.locale = locale;
		this.adapterProvider = adapterProvider;
		this.model = model;
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

	public NlsProvider getNlsProvider() {
		return nlsProvider;
	}

	public void setNlsProvider(NlsProvider nlsProvider) {
		this.nlsProvider = nlsProvider;
	}

}
