package de.mhus.lib.core.shiro;

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
