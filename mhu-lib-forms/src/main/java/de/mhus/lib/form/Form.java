package de.mhus.lib.form;

import java.util.Locale;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.errors.MException;

public class Form {

	private Locale locale = Locale.getDefault();
	private ComponentAdapterProvider adapterProvider;
	private IConfig model;
	private DataSource dataSource;
	private MNls nls;
	
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

}
