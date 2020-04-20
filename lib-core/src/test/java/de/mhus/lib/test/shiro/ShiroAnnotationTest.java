package de.mhus.lib.test.shiro;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;

public class ShiroAnnotationTest {

    @RequiresPermissions("printer:print")
    public void permissionPrinterPrint() {}

    @RequiresAuthentication
    public void authentication() {}

    @RequiresGuest
    public void guest() {}
    
    @RequiresRoles("admin")
    public void roleAdmin() {}
    
    @RequiresRoles("user")
    public void roleUser() {}
    
}
