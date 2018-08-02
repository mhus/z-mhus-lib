package de.mhus.lib.form;

import java.util.Locale;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.util.MNlsBundle;
import de.mhus.lib.errors.MException;

public class MutableMForm extends MForm {

	public MutableMForm() {
		super();
	}

	public MutableMForm(DefRoot model) throws MException {
		super(model);
	}

	public MutableMForm(Locale locale, ComponentAdapterProvider adapterProvider, IConfig model) {
		super(locale, adapterProvider, model);
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
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

	public void setControl(FormControl control) {
		this.control = control;
		if (control != null) control.attachedForm(this);
	}

	public void setInformationPane(UiInformation informationPane) {
		this.informationPane = informationPane;
	}

	public void setActionHandler(ActionHandler actionHandler) {
		this.actionHandler = actionHandler;
	}

	public void setBuilder(IUiBuilder builder) {
		this.builder = builder;
	}

}
