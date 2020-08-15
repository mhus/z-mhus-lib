package de.mhus.lib.core.mapi;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MHousekeeper;
import de.mhus.lib.core.MHousekeeperTask;
import de.mhus.lib.core.MPeriod;

public class ConfigFileWatcher implements ApiInitialize {

    private HashMap<File, Long> configFiles = new HashMap<>();
    private MHousekeeperTask fileWatch;

    @Override
    public void doInitialize(ClassLoader coreLoader) {
        MCfgManager api = MApi.get().getCfgManager();
        for (File file : api.getMhusConfigFiles()) {
            configFiles.put(file, file.lastModified());
        }

        fileWatch =
                new MHousekeeperTask("mhus-config-watch") {

                    @Override
                    protected void doit() throws Exception {
                        boolean ok = true;
                        synchronized (configFiles) {
                            for (Map.Entry<File, Long> entry : configFiles.entrySet()) {
                                if (entry.getKey().lastModified() != entry.getValue()) {
                                    ok = false;
                                    break;
                                }
                            }
                        }
                        if (!ok) {
                            MCfgManager api = MApi.get().getCfgManager();
                            api.doRestart();
                            configFiles.clear();
                            for (File file : api.getMhusConfigFiles()) {
                                configFiles.put(file, file.lastModified());
                            }
                        }
                    }
                };

        MHousekeeper housekeeper = M.l(MHousekeeper.class);
        housekeeper.register(fileWatch, MPeriod.MINUTE_IN_MILLISECOUNDS);
    }
}
