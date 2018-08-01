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
package de.mhus.lib.vaadin.form;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.activator.DefaultActivator;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.form.ActivatorAdapterProvider;

/*
Default Config:
<de.mhus.lib.vaadin.form.DefaultAdapterProvider>
	<adapter name="text"        class="de.mhus.lib.vaadin.form.UiText.Adapter"/>
	<adapter name="checkbox"    class="de.mhus.lib.vaadin.form.UiCheckbox.Adapter"/>
	<adapter name="date"        class="de.mhus.lib.vaadin.form.UiDate.Adapter"/>
	<adapter name="password"    class="de.mhus.lib.vaadin.form.UiPassword.Adapter"/>
	<adapter name="number"      class="de.mhus.lib.vaadin.form.UiNumber.Adapter"/>
	<adapter name="textarea"    class="de.mhus.lib.vaadin.form.UiTextArea.Adapter"/>
	<adapter name="richtext"    class="de.mhus.lib.vaadin.form.UiRichTextArea.Adapter"/>
	<adapter name="combobox"    class="de.mhus.lib.vaadin.form.UiCombobox.Adapter"/>
	<adapter name="layout100"   class="de.mhus.lib.vaadin.form.UiLayout100.Adapter"/>
	<adapter name="layout50x50" class="de.mhus.lib.vaadin.form.UiLayout50x50.Adapter"/>
	<adapter name="layouttabs"  class="de.mhus.lib.vaadin.form.UiLayoutTabs.Adapter"/>
	<adapter name="100"         class="de.mhus.lib.vaadin.form.UiLayout100.Adapter"/>
	<adapter name="50x50"       class="de.mhus.lib.vaadin.form.UiLayout50x50.Adapter"/>
	<adapter name="tabs"        class="de.mhus.lib.vaadin.form.UiLayoutTabs.Adapter"/>
	<adapter name="options"     class="de.mhus.lib.vaadin.form.UiOptions.Adapter"/>
	<adapter name="layoutpanel" class="de.mhus.lib.vaadin.form.UiPanel.Adapter"/>
	<adapter name="panel"       class="de.mhus.lib.vaadin.form.UiPanel.Adapter"/>
	<adapter name="link"        class="de.mhus.lib.vaadin.form.UiLink.Adapter"/>
	<adapter name="label"       class="de.mhus.lib.vaadin.form.UiLabel.Adapter"/>
</de.mhus.lib.vaadin.form.DefaultAdapterProvider>
 */
public class DefaultAdapterProvider extends ActivatorAdapterProvider {

	public DefaultAdapterProvider() {
		super(null);
		DefaultActivator a = new DefaultActivator();
		activator = a;
		
		IConfig cfg = MApi.getCfg(DefaultAdapterProvider.class, null);
		if (cfg != null) {
			MActivator from = MApi.get().createActivator();
			for (IConfig mapping : cfg.getNodes("adapter")) {
				try {
					String name = mapping.getString("name");
					String className = mapping.getString("class");
					Class<?> clazz = from.getClazz(className);
					a.addMap(name, clazz);
				} catch (Exception e) {
					MLogUtil.log().e(mapping,e);
				}
			}
		} else {
			// default config
			a.addMap("text", UiText.Adapter.class);
			a.addMap("checkbox", UiCheckbox.Adapter.class);
			a.addMap("date", UiDate.Adapter.class);
			a.addMap("password", UiPassword.Adapter.class);
			a.addMap("number", UiNumber.Adapter.class);
			a.addMap("textarea", UiTextArea.Adapter.class);
			a.addMap("richtext", UiRichTextArea.Adapter.class);
			a.addMap("combobox", UiCombobox.Adapter.class);
			a.addMap("layout100", UiLayout100.Adapter.class);
			a.addMap("layout50x50", UiLayout2x50.Adapter.class);
			a.addMap("layout33x33x33", UiLayout3x33.Adapter.class);
			a.addMap("layout25x25x25x25", UiLayout4x25.Adapter.class);
			a.addMap("layouttabs", UiLayoutTabs.Adapter.class);
			a.addMap("layoutwizard", UiLayoutWizard.Adapter.class);
			a.addMap("100", UiLayout100.Adapter.class);
			a.addMap("50x50", UiLayout2x50.Adapter.class);
			a.addMap("tabs", UiLayoutTabs.Adapter.class);
			a.addMap("options", UiOptions.Adapter.class);
			a.addMap("layoutpanel", UiPanel.Adapter.class);
			a.addMap("panel", UiPanel.Adapter.class);
			a.addMap("link", UiLink.Adapter.class);
			a.addMap("label", UiLabel.Adapter.class);
			a.addMap("action", UiAction.Adapter.class);
			a.addMap("void", UiVoid.Adapter.class);
		}
	}

}
