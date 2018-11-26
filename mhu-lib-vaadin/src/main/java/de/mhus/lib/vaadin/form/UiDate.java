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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.AbstractDateField;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DateTimeField;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.UiComponent;
import de.mhus.lib.form.definition.FmDate;
import de.mhus.lib.form.definition.FmDate.FORMATS;

public class UiDate extends UiVaadin {

	ZoneId zoneId = ZoneId.systemDefault();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void setValue(Object value) throws MException {
		Date date = MCast.toDate(value, null);
        Component field = getComponentEditor();
        if (date == null) {
        		((AbstractDateField)field).setValue(null);
        } else
        if (field instanceof DateField) {
	        	LocalDate localDate = Instant.ofEpochMilli(date.getTime()).atZone(zoneId)
	        			.toLocalDate();
        		((DateField)field).setValue(localDate);
        } else
        if (field instanceof DateTimeField) {
	        	LocalDateTime localDate = Instant.ofEpochMilli(date.getTime()).atZone(zoneId)
	        			.toLocalDateTime();
        	((DateTimeField)field).setValue(localDate);
        }
	}

	@Override
	public Component createEditor() {
		// DateField ret = new DateField();
		// DateTimeField
		AbstractDateField<?,?> ret = null;
		FORMATS format = FmDate.FORMATS.valueOf(getConfig().getString("format",FmDate.FORMATS.DATE.name()).toUpperCase());
		switch (format) {
		case DATE:
			ret = new DateField();
			((DateField)ret).setResolution(DateResolution.DAY);
			break;
		case DATETIME:
			ret = new DateTimeField();
			((DateTimeField)ret).setResolution(DateTimeResolution.MINUTE);
			break;
		case DATETIMESECONDS:
			ret = new DateTimeField();
			((DateTimeField)ret).setResolution(DateTimeResolution.SECOND);
			break;
		case TIME:
			ret = new DateTimeField();
			((DateTimeField)ret).setDateFormat("HH:mm");
			((DateTimeField)ret).setResolution(DateTimeResolution.MINUTE);
			break;
		case TIMESECONDS:
			ret = new DateTimeField();
			ret.setDateFormat("HH:mm:ss");
			((DateTimeField)ret).setResolution(DateTimeResolution.SECOND);
			break;
		case CUSTOM:
			ret = new DateTimeField();
			String custom = getConfig().getString(FmDate.CUSTOM_FORMAT, null);
			if (custom != null)
				ret.setDateFormat(custom);
			((DateTimeField)ret).setResolution(DateTimeResolution.SECOND);
			break;
		default:
			ret = new DateField();
			break;
		}
		ret.setLocale(getForm().getLocale());
		return ret;
	}

	@Override
	protected Object getValue() throws MException {
		Component field = getComponentEditor();
		if (field instanceof DateField) {
			LocalDate localDate = ((DateField)field).getValue();
			return Date.from(localDate.atStartOfDay(zoneId).toInstant());
		} else
		if (field instanceof DateTimeField) {
			LocalDateTime localDate = ((DateTimeField)field).getValue();
			return Date.from(localDate.atZone(zoneId).toInstant());
		}
		return null;
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
