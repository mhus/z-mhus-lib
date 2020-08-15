package de.mhus.lib.core.security;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.util.SecureString;

@DefaultImplementation(TrustFromConfiguration.class)
public interface TrustApi {

    SecureString getPassword(String name);

    boolean validatePassword(String name, String password);
}
