package de.mhus.lib.core.aaa;

import java.util.Map;

public class MutablePrincipalData extends PrincipalData {

    private static final long serialVersionUID = 1L;

    public MutablePrincipalData() {
        super();
    }

    public MutablePrincipalData(Map<String, String> data) {
        super(data);
    }

    public Map<String, String> getData() {
        return data;
    }

}
