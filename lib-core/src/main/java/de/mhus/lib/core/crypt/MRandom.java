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
package de.mhus.lib.core.crypt;

import java.security.SecureRandom;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.errors.NotSupportedException;

@DefaultImplementation(DefaultRandom.class)
public interface MRandom {

    /**
     * Return a random byte from -127 to 128
     *
     * @return a random yte
     */
    byte getByte();

    /**
     * Return a random integer from 0 to INTEGER MAX.
     *
     * @return a random integer
     */
    int getInt();

    /**
     * Return a random double from 0 to 1
     *
     * @return random double
     */
    double getDouble();

    /**
     * Return a random long from 0 to LONG MAX.
     *
     * @return random long
     */
    long getLong();

    /**
     * Return an adaption of an random if available. e.g. java.util.Random and java.security.Random
     * should be supported.
     *
     * @param ifc Requested Interface or Class
     * @return The instance of Ifc
     * @throws NotSupportedException If adaption was not possible.
     */
    <T> T adaptTo(Class<? extends T> ifc)
            throws NotSupportedException; // adaptTo java.util.Random or java.secure.Random if
    // available

    /**
     * Return a random readable character
     *
     * @return random character
     */
    char getChar();

    SecureRandom getSecureRandom();
}
