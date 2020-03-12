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
package de.mhus.lib.basics;

public class Ace {

    public static final String RIGHTS_NONE = "";
    public static final String RIGHTS_ALL = "crud";
    public static final String RIGHTS_RO = "r";
    public static final String RIGHTS_RU = "ru";
    public static final String GENERAL_OPERATOR = "operator";
    public static final Ace ACE_NONE = new Ace(RIGHTS_NONE);
    public static final Ace ACE_ALL = new Ace(RIGHTS_ALL);
    public static final Ace ACE_RO = new Ace(RIGHTS_RO);
    public static final Ace ACE_RU = new Ace(RIGHTS_RU);

    protected String rights;

    public Ace() {}

    public Ace(String rights) {
        this.rights = rights;
    }

    public boolean canCreate() {
        return hasFlag('c');
    }

    public boolean canRead() {
        return hasFlag('r');
    }

    public boolean canUpdate() {
        return hasFlag('u');
    }

    public boolean canDelete() {
        return hasFlag('d');
    }

    public boolean hasFlag(char f) {
        return rights != null && rights.indexOf(f) > -1;
    }

    public String getRights() {
        return rights;
    }

    @Override
    public String toString() {
        return rights;
    }
}
