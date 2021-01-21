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
package de.mhus.lib.portlet.actions;

import javax.portlet.ActionRequest;

import de.mhus.lib.core.IProperties;

/**
 * Abstract AbstractAction class.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class AbstractAction implements Action {

    /** {@inheritDoc} */
    @Override
    public synchronized IProperties createProperties(ActionRequest request) {
        ActionProperties properties = new ActionProperties(request);
        return properties;
    }
}
