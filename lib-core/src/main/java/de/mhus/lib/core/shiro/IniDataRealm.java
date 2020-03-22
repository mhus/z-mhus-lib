package de.mhus.lib.core.shiro;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.config.Ini;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IniDataRealm extends IniRealm implements PrincipalDataRealm {

    public static final String DATA_SECTION_NAME = "data";
    private static transient final Logger log = LoggerFactory.getLogger(IniDataRealm.class);

    private HashMap<String,Map<String,String>> userData = new HashMap<>();
    
    
    
    public IniDataRealm() {
        super();
    }

    public IniDataRealm(Ini ini) {
        super(ini);
    }

    public IniDataRealm(String resourcePath) {
        super(resourcePath);
    }

    @Override
    public PrincipalData getUserData(Subject subject) {
        String userName = ShiroUtil.getPrincipal(subject);
        Map<String,String> data = userData.get(userName);
        if (data == null) return null;
        PrincipalData ret = new PrincipalData();
        ret.putAll(data);
        return ret;
    }

    @Override
    protected void onInit() {
        super.onInit();
        Ini ini = getIni();
        
        processDefinitions(ini);
        
    }

    private void processDefinitions(Ini ini) {
        if (CollectionUtils.isEmpty(ini)) {
            log.warn("{} defined, but the ini instance is null or empty.", getClass().getSimpleName());
            return;
        }

        Ini.Section dataSection = ini.getSection(DATA_SECTION_NAME);
        if (!CollectionUtils.isEmpty(dataSection)) {
            log.debug("Discovered the [{}] section.  Processing...", DATA_SECTION_NAME);
            processDataDefinitions(dataSection);
        }
    }

    protected void processDataDefinitions(Map<String, String> dataDefs) {
        if (dataDefs == null || dataDefs.isEmpty()) {
            return;
        }
        for (String key : dataDefs.keySet()) {
            String value = dataDefs.get(key);

            int pos = key.indexOf('#');
            if (pos > 0) {
                String userName = key.substring(0,pos);
                String subKey = key.substring(pos+1);
                
                Map<String,String> data = userData.get(userName);
                if (data == null) {
                    data = new HashMap<>();
                    userData.put(userName, data);
                }
                data.put(subKey, value);
            }
        }
    }
    
}
