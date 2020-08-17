/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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
package de.mhus.lib.core.service;

import de.mhus.lib.core.M;

public class IdentUtil {

    private static String ident;
    private static String service;
    private static String str;

    public static String getServerIdent() {
        if (ident == null) ident = M.l(ServerIdent.class).getIdent();
        return ident;
    }

    public static String getServiceIdent() {
        if (service == null) service = M.l(ServerIdent.class).getService();
        return service;
    }

    public static String getFullIdent() {
        if (str == null) str = M.l(ServerIdent.class).toString();
        return str;
    }
}
