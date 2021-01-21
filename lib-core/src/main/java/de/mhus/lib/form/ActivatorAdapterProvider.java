/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.form;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.config.IConfig;

public class ActivatorAdapterProvider implements ComponentAdapterProvider {

    protected MActivator activator;

    public ActivatorAdapterProvider(MActivator activator) {
        this.activator = activator;
    }

    @Override
    public UiComponent createComponent(String id, IConfig config) throws Exception {
        return getAdapter(id).createAdapter(config);
    }

    @Override
    public ComponentAdapter getAdapter(String id) throws Exception {
        return (ComponentAdapter) activator.getObject(id);
    }

    @Override
    public UiWizard createWizard(String obj) throws Exception {
        return (UiWizard) activator.createObject(obj);
    }

    public MActivator getActivator() {
        return activator;
    }
}
