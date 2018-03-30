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
package de.mhus.lib.vaadin;

public class ManagedListAdapter implements ManagedListEntity {

	@Override
	public void doPostCreate(AbstractListEditor<?> source) {
	}

	@Override
	public void doCancel(AbstractListEditor<?> source) {
	}

	@Override
	public boolean doPreNew(AbstractListEditor<?> source, Object id) {
		return false;
	}

	@Override
	public void doPreSave(AbstractListEditor<?> source) {
	}

	@Override
	public void doPostSave(AbstractListEditor<?> source) {
	}

	@Override
	public Object doGetId(AbstractListEditor<?> source) {
		return null;
	}

	@Override
	public void doPreDelete(AbstractListEditor<?> source) {
	}

	@Override
	public void doPostDelete(AbstractListEditor<?> source) {
	}

}
