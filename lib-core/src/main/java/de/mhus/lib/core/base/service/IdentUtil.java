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
package de.mhus.lib.core.base.service;

import de.mhus.lib.core.M;

public class IdentUtil {

    private static String ident;

    public static String getServerIdent() {
        if (ident == null) ident = M.l(ServerIdent.class).getIdent();
        return ident;
    }

    public static String getServiceIdent() {
        if (ident == null) ident = M.l(ServerIdent.class).getService();
        return ident;
    }
}
