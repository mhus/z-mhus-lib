package de.mhus.lib.form;

import java.util.Locale;

import de.mhus.lib.core.config.IConfig;

public class Form {

	private Locale locale = Locale.getDefault();
	private ComponentAdapterProvider adapterProvider;
	private IConfig model;
	
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

}
