package de.mhus.lib.core.security;


import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;

import de.mhus.lib.core.shiro.ShiroUtil;

public class AaaUtil {

    public static String createTrustTicket(String trust, Subject subject) {
        //TODO
        return "tru," + trust + "," + ShiroUtil.getPrincipal(subject);
    }

    public static void login(Subject subject, String ticket) {
        if (ticket == null) throw new AuthorizationException("ticket not set");
        String[] parts = ticket.split(",",3);
        if (parts.length != 3) throw new AuthorizationException("ticket not valide");
        UsernamePasswordToken token = new UsernamePasswordToken(parts[0], parts[1]);
        subject.login(token);
    }

}
