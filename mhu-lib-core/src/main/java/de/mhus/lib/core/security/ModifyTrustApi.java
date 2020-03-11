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
package de.mhus.lib.core.security;

import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.errors.MException;

public interface ModifyTrustApi {

    /**
     * Create a Trust configuration.
     *
     * @param name
     * @param password
     * @param properties
     * @throws MException
     */
    void createTrust(String name, String password, IReadProperties properties) throws MException;

    /**
     * Delete Trust configuration.
     *
     * @param name
     * @throws MException
     */
    void deleteTrust(String name) throws MException;

    /**
     * Change Trust secret.
     *
     * @param name
     * @param newPassword
     * @throws MException
     */
    void changePassword(String name, String newPassword) throws MException;

    /**
     * Change Trust properties.
     *
     * @param name
     * @param properties
     * @throws MException
     */
    void changeTrust(String name, IReadProperties properties) throws MException;

    Trust getTrust(String string);
}
