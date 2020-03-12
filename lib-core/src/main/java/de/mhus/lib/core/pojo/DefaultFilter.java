/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.pojo;

import de.mhus.lib.annotations.pojo.Action;
import de.mhus.lib.annotations.pojo.Embedded;
import de.mhus.lib.annotations.pojo.Hidden;

public class DefaultFilter implements PojoFilter {

    private boolean removeHidden;
    private boolean removeWriteOnly;
    private boolean removeReadOnly;
    private boolean removeEmbedded;
    private boolean removeNoActions;

    public DefaultFilter() {
        this(true, false, true, false, true);
    }

    public DefaultFilter(
            boolean removeHidden,
            boolean removeEmbedded,
            boolean removeWriteOnly,
            boolean removeReadOnly,
            boolean removeNoActions) {
        this.removeHidden = removeHidden;
        this.removeEmbedded = removeEmbedded;
        this.removeWriteOnly = removeWriteOnly;
        this.removeReadOnly = removeReadOnly;
        this.removeNoActions = removeNoActions;
    }

    @Override
    public void filter(PojoModelImpl model) {
        for (String name : model.getAttributeNames()) {
            PojoAttribute<?> attr = model.getAttribute(name);
            if (removeHidden && attr.getAnnotation(Hidden.class) != null) {
                model.removeAttribute(name);
            } else if (removeEmbedded && attr.getAnnotation(Embedded.class) != null) {
                model.removeAttribute(name);
            } else if (removeWriteOnly && !attr.canRead()) {
                model.removeAttribute(name);
            } else if (removeReadOnly && !attr.canWrite()) {
                model.removeAttribute(name);
            }
        }

        for (String name : model.getActionNames()) {
            PojoAction action = model.getAction(name);
            if (removeNoActions && action.getAnnotation(Action.class) == null)
                model.removeAction(name);
        }
    }
}
