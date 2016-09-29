package de.mhus.lib.form;

import java.util.Locale;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsBundle;
import de.mhus.lib.core.util.MNlsFactory;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.errors.MException;

/**
 * <p>Form class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class Form extends MObject implements MNlsProvider {

	private Locale locale = Locale.getDefault();
	private ComponentAdapterProvider adapterProvider;
	protected IConfig model;
	private DataSource dataSource;
	private MNlsBundle nlsBundle;
	private FormControl control;
	private UiInformation informationPane;
	
	/**
	 * <p>Constructor for Form.</p>
	 */
	public Form() {
		
	}
	
	/**
	 * <p>Constructor for Form.</p>
	 *
	 * @param locale a {@link java.util.Locale} object.
	 * @param adapterProvider a {@link de.mhus.lib.form.ComponentAdapterProvider} object.
	 * @param model a {@link de.mhus.lib.core.config.IConfig} object.
	 */
	public Form(Locale locale, ComponentAdapterProvider adapterProvider, IConfig model) {
		this.locale = locale;
		this.adapterProvider = adapterProvider;
		this.model = model;
	}

	/**
	 * <p>Constructor for Form.</p>
	 *
	 * @param model a {@link de.mhus.lib.core.definition.DefRoot} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public Form(DefRoot model) throws MException {
		this.model	= model;
		model.build();
	}
	
	/**
	 * <p>Getter for the field <code>locale</code>.</p>
	 *
	 * @return a {@link java.util.Locale} object.
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * <p>Getter for the field <code>adapterProvider</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.ComponentAdapterProvider} object.
	 */
	public ComponentAdapterProvider getAdapterProvider() {
		return adapterProvider;
	}

	/**
	 * <p>Getter for the field <code>model</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.config.IConfig} object.
	 */
	public IConfig getModel() {
		return model;
	}

	/**
	 * <p>Getter for the field <code>dataSource</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.DataSource} object.
	 */
	public DataSource getDataSource() {
		if (dataSource == null) dataSource = MSingleton.baseLookup(this,DataSource.class);
		return dataSource;
	}

	/**
	 * <p>Setter for the field <code>dataSource</code>.</p>
	 *
	 * @param dataSource a {@link de.mhus.lib.form.DataSource} object.
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * <p>getNls.</p>
	 *
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public synchronized MNls getNls() {
		if (nlsBundle == null) {
//			nlsBundle = base(MNlsBundle.class);
			return null;
		}
		return nlsBundle.getNls(locale);
	}

	/**
	 * <p>Setter for the field <code>nlsBundle</code>.</p>
	 *
	 * @param bundle a {@link de.mhus.lib.core.util.MNlsBundle} object.
	 */
	public void setNlsBundle(MNlsBundle bundle) {
		this.nlsBundle = bundle;
	}

	/**
	 * <p>Setter for the field <code>adapterProvider</code>.</p>
	 *
	 * @param adapterProvider a {@link de.mhus.lib.form.ComponentAdapterProvider} object.
	 */
	public void setAdapterProvider(ComponentAdapterProvider adapterProvider) {
		this.adapterProvider = adapterProvider;
	}
	/**
	 * <p>Setter for the field <code>locale</code>.</p>
	 *
	 * @param locale a {@link java.util.Locale} object.
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	/**
	 * <p>Getter for the field <code>control</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.FormControl} object.
	 */
	public FormControl getControl() {
		if (control == null) setControl(MSingleton.baseLookup(this,FormControl.class));
		return control;
	}
	
	/**
	 * <p>Setter for the field <code>control</code>.</p>
	 *
	 * @param control a {@link de.mhus.lib.form.FormControl} object.
	 */
	public void setControl(FormControl control) {
		this.control = control;
		if (control != null) control.attachedForm(this);
	}
	/**
	 * <p>Getter for the field <code>informationPane</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.UiInformation} object.
	 */
	public UiInformation getInformationPane() {
		return informationPane;
	}
	/**
	 * <p>Setter for the field <code>informationPane</code>.</p>
	 *
	 * @param informationPane a {@link de.mhus.lib.form.UiInformation} object.
	 */
	public void setInformationPane(UiInformation informationPane) {
		this.informationPane = informationPane;
	}
	
	/**
	 * <p>Getter for the field <code>nlsBundle</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.util.MNlsBundle} object.
	 */
	public MNlsBundle getNlsBundle() {
		return nlsBundle;
	}
	
}
