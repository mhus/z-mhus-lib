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
package de.mhus.lib.liferay;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.mhus.lib.core.MLog;
import de.mhus.lib.liferay.osgi.CmdLiferayMhus;

public class LiferayActivator extends MLog implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		log().i("Start");
		context.registerService(CommandProvider.class, new CmdLiferayMhus(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		log().i("Stop");
	}

}
