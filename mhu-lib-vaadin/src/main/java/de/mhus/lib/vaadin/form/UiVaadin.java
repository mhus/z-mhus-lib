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

import com.vaadin.data.HasValue;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusNotifier;
import com.vaadin.ui.Component;

import de.mhus.lib.core.util.MNls;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.UiComponent;

public abstract class UiVaadin extends UiComponent {

	private Component componentWizard;
	private Component componentError;
	private Component componentLabel;
	private Component componentEditor;
	private boolean editorEditable = true;
	
	@Override
	public void doRevert() throws MException {
		DataSource ds = getForm().getDataSource();
		setEnabled( ds.getBoolean(this, DataSource.ENABLED, true) );
		setEditable( ds.getBoolean(this, DataSource.EDITABLE, true) );
		setVisible( ds.getBoolean(this, DataSource.VISIBLE, true) );
		doUpdateMetadata();
		Object val = getConfig().get("value");
		if (val == null)
			val = getConfig().getString("defaultvalue", null);
		setValue(ds.getObject(this, DataSource.VALUE, val ));
		setCaption(getCaption(ds));
		if (componentError != null) componentError.setVisible(false);
		editorEditable = getConfig().getBoolean("editable", true) && ds.getBoolean(this, DataSource.EDITOR_EDITABLE, true);
		if (componentEditor != null && !editorEditable) componentEditor.setEnabled(false);
		getForm().getControl().reverted(this);
	}

	public String getCaption(DataSource ds) {
		return MNls.find(getForm(), ds.getString(this, DataSource.CAPTION, getConfigString(DataSource.CAPTION, getName())) );
	}

	@Override
	public void doUpdateValue() throws MException {
		DataSource ds = getForm().getDataSource();
		String def = getConfig().getString("defaultvalue", null);
		setValue(ds.getObject(this, DataSource.VALUE, def));
		getForm().getControl().valueSet(this);
	}
	
	
	@Override
	public void setVisible(boolean visible) throws MException {
		if (componentLabel != null) componentLabel.setVisible(visible);
		if (componentError != null && componentError.isVisible()) componentError.setVisible(visible);
		if (componentEditor != null) componentEditor.setVisible(visible);
		if (componentWizard != null) componentWizard.setVisible(visible);
	}

	@Override
	public boolean isVisible() throws MException {
		if (componentLabel != null && componentLabel.isVisible()) return true;
		if (componentError != null && componentError.isVisible()) return true;
		if (componentEditor != null && componentEditor.isVisible()) return true;
		if (componentWizard != null && componentWizard.isVisible()) return true;
		return false;
	}

	@Override
	public void setEnabled(boolean enabled) throws MException {
		if (componentEditor != null && editorEditable) componentEditor.setEnabled(enabled);
		if (componentWizard != null) componentWizard.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() throws MException {
		if (componentEditor != null && componentEditor.isVisible()) return true;
		if (componentWizard != null && componentWizard.isVisible()) return true;
		return false;
	}

	@Override
	public void setEditable(boolean editable) throws MException {
		if (!(componentEditor instanceof HasValue)) return;
		if (componentEditor != null && editorEditable) ((HasValue<?>)componentEditor).setReadOnly(!editable);
//		if (componentWizard != null) componentWizard.setReadOnly(!editable);
	}

	protected abstract void setValue(Object value) throws MException;
	protected abstract Object getValue() throws MException;

	protected void setCaption(String value) throws MException {
		if (componentLabel != null) componentLabel.setCaption(value);
	}

	protected Component create(UiLayout grid) throws MException {
		grid.createRow(this);
		
		return getComponentEditor();
	}

	public abstract Component createEditor();

	public Component getComponentWizard() {
		return componentWizard;
	}

	public void setComponentWizard(Component componentWizard) {
		this.componentWizard = componentWizard;
	}

	public Component getComponentError() {
		return componentError;
	}

	public void setComponentError(Component componentError) {
		this.componentError = componentError;
	}

	public Component getComponentLabel() {
		return componentLabel;
	}

	public void setComponentLabel(Component componentLabel) {
		this.componentLabel = componentLabel;
	}

	public Component getComponentEditor() {
		return componentEditor;
	}

	public void setComponentEditor(Component componentEditor) {
		this.componentEditor = componentEditor;
	}

	public UiLayout getLayout() {
		return null;
	}

	@Override
	public void setError(String error) {
		Component el = getComponentError();
		if (el == null) return;
		el.setCaption(error);
		el.setVisible(true);
	}

	@Override
	public void clearError() {
		Component el = getComponentError();
		if (el == null) return;
		el.setCaption("");
		el.setVisible(false);
	}

	public void fieldValueChangedEvent() {
		Component e = getComponentEditor();
		DataSource ds = getForm().getDataSource();
		if (e == null || ds == null) return;
		Object newValue = null;
		try {
			newValue = getValue();
			if (getForm().getControl().newValue(this, newValue)) {
				ds.setObject(this, DataSource.VALUE, newValue );
			}
		} catch (Throwable t) {
			getForm().getControl().newValueError(this, newValue, t);
		}

	}

	public void focusEvent() {
		getForm().getControl().focus(this);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public void setListeners() {
		Component e = getComponentEditor();
		if (e == null) return;
		if (e instanceof com.vaadin.v7.ui.AbstractField) {
          ((com.vaadin.v7.ui.AbstractField<Object>)e).setImmediate(true);
          ((com.vaadin.v7.ui.AbstractField<Object>)e).addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
              
              private static final long serialVersionUID = 1L;

              @Override
              public void valueChange(com.vaadin.v7.data.Property.ValueChangeEvent event) {
                  fieldValueChangedEvent();
              }
          });
		} else
		if (e instanceof HasValue) {
//			((AbstractField)e).setImmediate(true);
			((HasValue<Object>)e).addValueChangeListener(new ValueChangeListener<Object>() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent<Object> event) {
					fieldValueChangedEvent();
				}
			});
		}
		if (e instanceof FocusNotifier) {
			((FocusNotifier)e).addFocusListener(new FieldEvents.FocusListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void focus(FocusEvent event) {
					focusEvent();
				}
			});
		}
	}
	
	@Override
	public void doUpdateMetadata() throws MException {
	}

}
