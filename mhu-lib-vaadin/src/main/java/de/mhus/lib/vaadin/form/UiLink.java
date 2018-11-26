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

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import de.mhus.lib.core.MXml;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.UiComponent;

public class UiLink extends UiVaadin {

	private Object value;



	@Override
	protected void setValue(Object value) throws MException {
		DataSource ds = getForm().getDataSource();
//		((Link)getComponentEditor()).setTargetName("_blank");
//		((Link)getComponentEditor()).setResource(new ExternalResource( MCast.toString(value) ));
//		((Link)getComponentEditor()).setCaption( ds.getString(this, "label", getConfig().getString("label", "label=Link") ) );
		
		String label = MXml.encode( ds.getString(this, "label", getConfig().getString("label", "label=Link") ) );
		((Label)getComponentEditor()).setCaptionAsHtml(true);
		if (value == null)
			((Label)getComponentEditor()).setCaption(label );
		else {
			String link = MXml.encode( String.valueOf( value )  );
			((Label)getComponentEditor()).setCaption("<a href=\"" + link + "\" target=_blank>" + label + "</a>" );
		}
		this.value = value;
	}

	@Override
	public Component createEditor() {
		return new Label();
//		return new Link();
	}

	@Override
	protected Object getValue() throws MException {
		return value;
//		return ((Link)getComponentEditor()).getResource().toString();
	}

	
	
	@Override
	public void setEnabled(boolean enabled) throws MException {
	}



	public static class Adapter implements ComponentAdapter {

		@Override
		public UiComponent createAdapter(IConfig config) {
			return new UiLink();
		}

		@Override
		public ComponentDefinition getDefinition() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
