package de.mhus.lib.logging;

import org.apache.shiro.subject.Subject;

import de.mhus.lib.core.logging.ParameterEntryMapper;

public class ShiroMapper implements ParameterEntryMapper {

    @Override
    public Object map(Object in) {
        if (in == null) return null;
        if (in instanceof Subject)
            return ((Subject)in).getPrincipal();
        return in;
    }

}
