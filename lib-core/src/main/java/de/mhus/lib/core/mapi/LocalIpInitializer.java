package de.mhus.lib.core.mapi;

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.mhus.lib.core.base.service.ServerIdent;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.config.IConfig;

public class LocalIpInitializer implements CfgInitiator {

    @Override
    public void doInitialize(IApiInternal internal, MCfgManager manager, IConfig config) {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("IP Address: " + inetAddress.getHostAddress() + " / Host Name: " + inetAddress.getHostName());
            ServerIdent.getAttributes().setString("ip", inetAddress.getHostAddress());
            ServerIdent.getAttributes().setString("hostname", inetAddress.getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
