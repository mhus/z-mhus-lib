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
package de.mhus.lib.core.shiro;

import org.apache.shiro.ShiroException;
import org.apache.shiro.subject.Subject;

public interface BearerRealm {

    public final BearerConfiguration DEFAULT_CONFIGURATION = new BearerConfiguration();

    /**
     * Create a new token.
     *
     * @param subject A subject of the current realm
     * @param issuer The request issuer or null if unknown
     * @param configuration Configuration. You could recycle an existing configuration.
     * @return A new bearer token to use with org.apache.shiro.authc.BearerToken
     * @throws ShiroException It account is not part of the realm or the token can not be created
     */
    String createBearerToken(Subject subject, String issuer, BearerConfiguration configuration)
            throws ShiroException;

    default String createBearerToken(Subject subject, String issuer) {
        return createBearerToken(subject, issuer, DEFAULT_CONFIGURATION);
    }
}
