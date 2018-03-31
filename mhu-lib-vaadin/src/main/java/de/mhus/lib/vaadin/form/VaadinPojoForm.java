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

import de.mhus.lib.form.PojoForm;
import de.mhus.lib.form.PojoProvider;

public class VaadinPojoForm<T> extends VaadinForm implements PojoProvider {

	private static final long serialVersionUID = 1L;
	private T pojo;

	public VaadinPojoForm(T pojo) throws Exception {
		setPojo(pojo);
		setForm(new PojoForm(this) );
	}
	
	@Override
	public T getPojo() {
		return pojo;
	}

	public void setPojo(T pojo) {
		this.pojo = pojo;
		if (getBuilder() != null)
			getBuilder().doUpdateValues();
	}
	
}
