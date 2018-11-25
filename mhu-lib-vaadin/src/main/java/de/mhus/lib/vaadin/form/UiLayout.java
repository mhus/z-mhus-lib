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

import java.io.Serializable;

import com.vaadin.data.HasValue;
import com.vaadin.ui.Component;

import de.mhus.lib.errors.MException;

public abstract class UiLayout extends UiVaadin implements Serializable {

	private static final long serialVersionUID = 1L;

	public abstract void createRow(final UiVaadin c);
	public abstract Component getComponent();
	
	@Override
	public void doUpdateValue() throws MException {
	}
	@Override
	public void doUpdateMetadata() throws MException {
		
	}
	@Override
	public void setVisible(boolean visible) throws MException {
		getComponent().setVisible(visible);
		super.setVisible(visible);
	}
	@Override
	public boolean isVisible() throws MException {
		return getComponent().isVisible();
	}
	@Override
	public void setEnabled(boolean enabled) throws MException {
		getComponent().setEnabled(enabled);
	}
	@Override
	public void setEditable(boolean editable) throws MException {
		if (!(getComponent() instanceof HasValue)) return;
		((HasValue<?>)getComponent()).setReadOnly(!editable);
	}
	@Override
	public boolean isEnabled() throws MException {
		if (!(getComponent() instanceof HasValue)) return true;
		return !((HasValue<?>)getComponent()).isReadOnly();
	}
	@Override
	public void setError(String error) {
	}
	@Override
	public void clearError() {
	}

	@Override
	protected void setValue(Object value) throws MException {
	}

	@Override
	protected Object getValue() throws MException {
		return null;
	}

	@Override
	public Component createEditor() {
		Component ret = getComponent();
		return ret;
	}
	
	@Override
	public UiLayout getLayout() {
		return this;
	}

	
}
