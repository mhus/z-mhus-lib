/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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
package de.mhus.lib.core.mapi;

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.service.ServerIdent;

public class LocalIpInitializer implements CfgInitiator {

    @Override
    public void doInitialize(IApiInternal internal, MCfgManager manager, IConfig config) {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println(
                    "IP Address: "
                            + inetAddress.getHostAddress()
                            + " / Host Name: "
                            + inetAddress.getHostName());
            ServerIdent.getAttributes().setString("ip", inetAddress.getHostAddress());
            ServerIdent.getAttributes().setString("hostname", inetAddress.getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
