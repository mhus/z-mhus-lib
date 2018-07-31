/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.form;

import java.util.Locale;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsBundle;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.errors.MException;

public class MForm extends MObject implements MNlsProvider {

	private Locale locale = Locale.getDefault();
	private ComponentAdapterProvider adapterProvider;
	protected IConfig model;
	private DataSource dataSource;
	private ActionHandler actionHandler;
	private MNlsBundle nlsBundle;
	private FormControl control;
	private UiInformation informationPane;
	
	public MForm() {
		
	}
	
	public MForm(Locale locale, ComponentAdapterProvider adapterProvider, IConfig model) {
		if (locale != null)
			this.locale = locale;
		this.adapterProvider = adapterProvider;
		if (model == null) new NullPointerException("model could not be null");
		this.model = model;
	}

	public MForm(DefRoot model) throws MException {
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
		if (dataSource == null) dataSource = MApi.lookup(DataSource.class);
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
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
		if (control == null) setControl(MApi.lookup(FormControl.class));
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

	public ActionHandler getActionHandler() {
		return actionHandler;
	}

	public void setActionHandler(ActionHandler actionHandler) {
		this.actionHandler = actionHandler;
	}
	
}
