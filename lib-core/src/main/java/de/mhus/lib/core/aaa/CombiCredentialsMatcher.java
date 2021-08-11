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
package de.mhus.lib.core.aaa;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import de.mhus.lib.core.M;

public class CombiCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        if (token instanceof BearerToken) {
            String tokenSubject =
                    M.l(JwtProvider.class).getSubject(((BearerToken) token).getToken());
            String infoSubject = info.getPrincipals().toString();
            return tokenSubject != null && tokenSubject.equals(infoSubject);
        }
        if (token instanceof TrustedToken) {
            return true;
        }

        Object credentials = token.getCredentials();
        Object principal = token.getPrincipal();
        if (credentials == null || principal == null) {
            return false;
        }

        if (!Aaa.ADMIN_LOGIN_ALLOWED.value() && Aaa.USER_ADMIN.value().equals(principal))
            return false; // admin can't login directly
        if (Aaa.USER_GUEST.value().equals(principal)) return false; // guest can't login directly

        return super.doCredentialsMatch(token, info);
    }
}
