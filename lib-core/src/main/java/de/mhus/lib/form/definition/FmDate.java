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
package de.mhus.lib.form.definition;

import de.mhus.lib.basics.consts.Identifier;
import de.mhus.lib.core.M;
import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.IDefAttribute;

public class FmDate extends IFmElement {

    private static final long serialVersionUID = 1L;

    public enum FORMATS {
        DATE,
        DATETIME,
        DATETIMESECONDS,
        TIME,
        TIMESECONDS,
        CUSTOM
    };

    public static final String FORMAT = "format";
    public static final String CUSTOM_FORMAT = "customformat";

    public FmDate(
            Identifier ident,
            FORMATS format,
            String title,
            String description,
            IDefAttribute... definitions) {
        this(M.n(ident), format, title, description, definitions);
    }

    public FmDate(
            String name,
            FORMATS format,
            String title,
            String description,
            IDefAttribute... definitions) {
        this(name, new DefAttribute(FORMAT, format.name()), new FaNls(title, description));
        addDefinition(definitions);
    }
    //	public FmDate(String name, String title, String description) {
    //		this(name, new FmNls(title, description), new FmDefaultSources());
    //	}

    public FmDate(String name, IDefAttribute... definitions) {
        super(name, definitions);
        // setString(FmElement.TYPE, FORMATS.DATE.name());
        setString("type", "date");
    }
}
