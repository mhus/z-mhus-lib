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
package de.mhus.lib.core.lang;

import de.mhus.lib.core.base.BaseFindStrategy;
import de.mhus.lib.core.base.InjectStrategy;
import de.mhus.lib.core.base.NoInjectionStrategy;
import de.mhus.lib.core.base.SingleBaseStrategy;
import de.mhus.lib.core.system.DefaultBase;

public class BaseControl {

	// need to create instances on request to avoid recursive loops using createBase() inside findStrategy
	private BaseFindStrategy findStrategy = null;
	private InjectStrategy injectStrategy = null;
	
	public Base base() {
		return getFindStrategy().find();
	}

	public void setFindStrategy(BaseFindStrategy strategy) {
		findStrategy = strategy;
	}
	
	public void setInjectStrategy(InjectStrategy strategy) {
		injectStrategy = strategy;
	}
	
	public synchronized InjectStrategy getInjectStrategy() {
		if (injectStrategy == null) {
			injectStrategy = new NoInjectionStrategy();
		}
		return injectStrategy;
	}
	
	public synchronized BaseFindStrategy getFindStrategy() {
		if (findStrategy == null) {
			findStrategy = new SingleBaseStrategy();
		}
		return findStrategy;
	}
	
	
	public Base createBase(MObject mObject, Base parent) {
		Base newBase = new DefaultBase(parent);
		return newBase;
	}

	public Base installBase(Base base) {
		return getFindStrategy().install(base);
	}
	
//	public Base getCurrentBase() {
//		return getFindStrategy().find();
//	}

	public void inject(Object object, Base base) {
		getInjectStrategy().inject(object, base);
	}
	
}
