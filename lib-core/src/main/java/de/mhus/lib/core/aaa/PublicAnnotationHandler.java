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

import java.lang.annotation.Annotation;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;

import de.mhus.lib.annotations.generic.Public;

public class PublicAnnotationHandler extends AuthorizingAnnotationHandler {

    public PublicAnnotationHandler() {
        super(Public.class);
    }

    @Override
    public void assertAuthorized(Annotation a) throws AuthorizationException {
        if (!(a instanceof Public)) return;

        if (!((Public) a).readable()) throw new UnauthenticatedException();
    }
}
