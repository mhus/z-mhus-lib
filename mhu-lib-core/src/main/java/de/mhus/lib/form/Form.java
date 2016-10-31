package de.mhus.lib.form;

import java.util.Locale;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsBundle;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.errors.MException;

public class Form extends MObject implements MNlsProvider {

	private Locale locale = Locale.getDefault();
	private ComponentAdapterProvider adapterProvider;
	protected IConfig model;
	private DataSource dataSource;
	private MNlsBundle nlsBundle;
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
		if (dataSource == null) dataSource = MSingleton.baseLookup(this,DataSource.class);
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public synchronized MNls getNls() {
		if (nlsBundle == null) {
//			nlsBundle = base(MNlsBundle.class);
			return null;
		}
		return nlsBundle.getNls(locale);
	}

	public void setNlsBundle(MNlsBundle bundle) {
		this.nlsBundle = bundle;
	}

	public void setAdapterProvider(ComponentAdapterProvider adapterProvider) {
		this.adapterProvider = adapterProvider;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public FormControl getControl() {
		if (control == null) setControl(MSingleton.baseLookup(this,FormControl.class));
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
	
	public MNlsBundle getNlsBundle() {
		return nlsBundle;
	}
	
}
