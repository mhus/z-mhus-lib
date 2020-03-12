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
package de.mhus.lib.core.cast;

import java.text.DecimalFormat;

public class DoubleToString implements Caster<Double, String> {

    private static DecimalFormat doubleFormat = new DecimalFormat("0.##########");

    @Override
    public Class<? extends String> getToClass() {
        return String.class;
    }

    @Override
    public Class<? extends Double> getFromClass() {
        return Double.class;
    }

    @Override
    public String cast(Double in, String def) {
        return null;
    }

    public String toString(double _in) {
        String out = doubleFormat.format(_in);
        if (out.indexOf(',') >= 0) out = out.replace(",", "."); // for secure
        return out;
    }
}
