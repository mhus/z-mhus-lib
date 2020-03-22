package de.mhus.lib.core.shiro;

import java.util.HashMap;

public class PrincipalData extends HashMap<String, String> {

    private static final long serialVersionUID = 1L;
    public static final String DISPLAY_NAME = "_shiro.displayName";
    public static final String NAME = "_shiro.name";
    public static final String SESSION_KEY = "_shiro.principalData";
//    private static final String READ_ONLY = "_ro";

    public String getDisplayName() {
        return get(DISPLAY_NAME);
    }

    public String getName() {
        return get(NAME);
    }

    public void ro() {
        // TODO
    }


}
