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

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.UiComponent;

public class UiLayout3x33 extends AbstractColLayout {

	private static final long serialVersionUID = 1L;
	
	public UiLayout3x33() {
		super(3);
	}
	
	public static class Adapter implements ComponentAdapter {

		@Override
		public UiComponent createAdapter(IConfig config) {
			return new UiLayout3x33();
		}

		@Override
		public ComponentDefinition getDefinition() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
}
