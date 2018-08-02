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

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(FormControlAdapter.class)
public interface FormControl {

	/**
	 * Call if the control is added to the form. Remember the form object.
	 * 
	 * @param form
	 */
	void attachedForm(MForm form);
	
	/**
	 * The component gets focus.
	 * 
	 * @param component
	 */
	void focus(UiComponent component);
	
	/**
	 * Set a new value to the component. Return true if the value is valid.
	 * @param component
	 * @param newValue
	 * @return If the value was accepted.
	 */
	boolean newValue(UiComponent component, Object newValue);

	/**
	 * The value was reverted.
	 * @param component
	 */
	void reverted(UiComponent component);

	void newValueError(UiComponent component, Object newValue, Throwable t);

	/**
	 * The method is called after every value update.
	 * @param component
	 */
	void valueSet(UiComponent component);

	/**
	 * The method is called after the form is initialized to set the default
	 * state.
	 */
	void setup();

	/**
	 * This function can be called by custom code, e.g. if a action is triggered.
	 * 
	 * @param action
	 * @param params
	 */
	void doAction(String action, Object ... params);
	
}
