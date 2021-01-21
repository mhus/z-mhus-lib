/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
