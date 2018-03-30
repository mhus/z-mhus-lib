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

import de.mhus.lib.core.util.MNls;

public class FormControlAdapter implements FormControl {

	@Override
	public void focus(UiComponent component) {
		component.clearError();
		UiInformation info = component.getForm().getInformationPane();
		DataSource ds = component.getForm().getDataSource();
		if (info == null || ds == null) return;
		info.setInformation(
				MNls.find(component.getForm(), ds.getString(component, DataSource.CAPTION, component.getConfigString(DataSource.CAPTION,  component.getName()))), 
				MNls.find(component.getForm(), ds.getString(component, DataSource.DESCRIPTION, component.getConfigString(DataSource.DESCRIPTION,  "")) ));
	}

	@Override
	public boolean newValue(UiComponent component, Object newValue) {
		return true;
	}

	@Override
	public void reverted(UiComponent component) {
	}

	@Override
	public void attachedForm(MForm form) {
		
	}

	@Override
	public void newValueError(UiComponent component, Object newValue, Throwable t) {
		if (t == null) {
			component.clearError();
			return;
		}
		//TODO Special NLS enabled exception
		component.setError(t.getMessage());
	}

	@Override
	public void valueSet(UiComponent component) {
		
	}

}
