/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.system;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.base.service.ServerIdent;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.config.IConfig;

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
                            if ("kubepods".equals(containerId) && parts.length > 4)
                                containerId = parts[4];
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (containerId != null) {
            System.out.println("Container: " + containerId);
            ServerIdent.getAttributes().setString("containerId", containerId);
            ServerIdent.getAttributes().setString("containerShortId", containerId.substring(0, 12));
        } else {
            System.out.println("WARNING: docker environment not found");
        }
    }

    public static String getContainerId() {
        return containerId;
    }
}
