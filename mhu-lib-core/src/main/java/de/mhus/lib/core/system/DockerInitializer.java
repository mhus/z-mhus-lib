package de.mhus.lib.core.system;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.service.ServerIdent;

public class DockerInitializer implements CfgInitiator {

    private static String containerId;

    @Override
    public void doInitialize(IApiInternal internal, CfgManager manager, IConfig config) {
        // get docker id
        File cgroup = new File("/proc/self/cgroup");
        if (cgroup.exists()) {
            try {
                List<String> content = MFile.readLines(cgroup, true);
                for (String line : content) {
                    if (line.contains(":memory:")) {
                        String[] parts = line.split("/");
                        if (parts.length > 2) {
                            containerId = parts[2];
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        if (containerId != null) {
            ServerIdent.ATTRIBUTES.setString("containerId", containerId);
        }
    }

    public static String getContainerId() {
        return containerId;
    }
}
