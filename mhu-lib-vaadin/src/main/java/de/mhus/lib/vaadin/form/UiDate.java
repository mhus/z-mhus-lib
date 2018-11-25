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

import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.UiComponent;
import de.mhus.lib.form.definition.FmDate;
import de.mhus.lib.form.definition.FmDate.FORMATS;

public class UiDate extends UiVaadin {

	@Override
	protected void setValue(Object value) throws MException {
		((DateField)getComponentEditor()).setValue(MCast.toDate(value, null));
	}

	@Override
	public Component createEditor() {
		DateField ret = new DateField();
		ret.setLocale(getForm().getLocale());
		FORMATS format = FmDate.FORMATS.valueOf(getConfig().getString("format",FmDate.FORMATS.DATE.name()).toUpperCase());
		switch (format) {
		case DATE:
			ret.setResolution(Resolution.DAY);
			break;
		case DATETIME:
			ret.setResolution(Resolution.MINUTE);
			break;
		case DATETIMESECONDS:
			ret.setResolution(Resolution.SECOND);
			break;
		case TIME:
			ret.setDateFormat("HH:mm");
			ret.setResolution(Resolution.MINUTE);
			break;
		case TIMESECONDS:
			ret.setDateFormat("HH:mm:ss");
			ret.setResolution(Resolution.SECOND);
			break;
		case CUSTOM:
			String custom = getConfig().getString(FmDate.CUSTOM_FORMAT, null);
			if (custom != null)
				ret.setDateFormat(custom);
			ret.setResolution(Resolution.SECOND);
			break;
		default:
			break;
		}
		return ret;
	}

	@Override
	protected Object getValue() throws MException {
		return ((DateField)getComponentEditor()).getValue();
	}

	public static class Adapter implements ComponentAdapter {

		@Override
		public UiComponent createAdapter(IConfig config) {
			return new UiDate();
		}

		@Override
		public ComponentDefinition getDefinition() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
