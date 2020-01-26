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
package de.mhus.lib.core.matcher;

public class Context {

    public Context parentContext;
    public ModelPart first;
    ModelComposit current = null;
    ModelComposit parent = null;
    public boolean not;
    ModelComposit root = null;

    public ModelComposit findRoot() {
        if (root == null && first != null) {
            root = new ModelAnd();
            root.add(first);
        }
        return root;
    }

    public void append(ModelComposit next) {
        if (root == null) {
            root = next;
            current = next;
            parent = next;
        } else {
            if (current == null) current = parent;
            current.add(next);
        }
        if (first != null) next.add(first);
        current = next;
        parent = next;
        first = null;
        not = false;
    }
}
