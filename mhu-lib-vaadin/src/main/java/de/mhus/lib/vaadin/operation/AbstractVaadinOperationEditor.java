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
package de.mhus.lib.vaadin.operation;

import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.vaadin.DialogControl;

public abstract class AbstractVaadinOperationEditor extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	protected DialogControl control;
	protected IProperties editorProperties;
	public static final Log log = Log.getLog(AbstractVaadinOperationEditor.class);

	protected final void initialize(IProperties editorProperties, DialogControl control) {
		this.editorProperties = editorProperties;
		this.control = control;
		initUI();
	}

	protected abstract void initUI();

	public abstract void fillOperationParameters(IProperties param);
	
}
