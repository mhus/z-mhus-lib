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
package de.mhus.lib.core.security;

import java.util.Locale;

import de.mhus.lib.errors.MException;

/**
 * Basic concept of authorization:
 *
 * <p>- The user/subject has a set of groups and a name. - The object can be authorized by an ACL
 * set - There are a set of ace's for every action - check if the user has access by processing the
 * ace entries - A thread is marked with a access context, in this context the program is acting
 * while executed - By default the access context is Guest - Using process() will change the current
 * access context and release() fall back to the previous context
 *
 * @author mikehummel
 */
public interface AccessApi {

    public static final String DISPLAY_NAME = "displayName";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";

    // access

    public static final String ROOT_NAME = "root";
    public static final String GUEST_NAME = "guest";
    public static final String ACCESS_NOT = "not:";
    public static final String ACCESS_NOT_USER = "notuser:";
    public static final String ACCESS_USER = "user:";
    public static final String ACCESS_SEPARATOR = ",";
    public static final String ACCESS_ALL = "*";
    public static final String ACCESS_COMMENT = "#";
    public static final char ACCESS_IS = '=';

    AaaContext process(String ticket, Locale locale);

    AaaContext release(String ticket);

    AaaContext process(Account ac, Trust trust, boolean admin, Locale locale);

    AaaContext release(Account ac);

    AaaContext release(AaaContext context);

    void resetContext();

    AaaContext getCurrentOrGuest();

    Account getCurrentAccount() throws MException;

    Account getAccount(String account) throws MException;

    AaaContext processAdminSession();

    boolean validatePassword(Account account, String password);

    String createTrustTicket(String trustName, AaaContext user);

    /**
     * Check if a resource access is granted to the account
     *
     * <p>Check if the account has access. The list is the rule set. Rules: - '*'access to all -
     * 'user:' prefix to allow user - 'notuser:' prefix to deny user - 'notgrout:' prefix to deny
     * group - group name to allow group
     *
     * @param account
     * @param acl
     * @param resourceName Name of the resource
     * @param id The id of the object
     * @param action The action to do or null for general access
     * @param def
     * @return Thrue if access is granted
     */
    boolean hasGroupAccess(Account account, String acl, String action, String def);

    /**
     * Check if a action access is granted to the account.
     *
     * @param account
     * @param who
     * @param acl
     * @param action
     * @param def
     * @return True if access is granted
     */
    boolean hasGroupAccess(Account account, Class<?> who, String acl, String action, String def);

    /**
     * Check if the access to the resource and action is granted.
     *
     * @param account
     * @param resourceName
     * @param id
     * @param action
     * @param def
     * @return True if access is granted
     */
    boolean hasResourceAccess(
            Account account, String resourceName, String id, String action, String def);

    /**
     * Create a access ticket for the user with password.
     *
     * @param username
     * @param password
     * @return The ticket.
     */
    String createUserTicket(String username, String password);

    /**
     * Return a synthetic guest access context.
     *
     * @return The guest context
     */
    AaaContext getGuestContext();

    /**
     * Process the access context.
     *
     * @param context
     */
    void process(AaaContext context);

    /**
     * Return the current context or null if not set.
     *
     * @return Context or null
     */
    AaaContext getCurrent();

    /**
     * Return a synthetic Guest Account.
     *
     * @return The system guest account
     */
    AccountGuest getGuestAccount();

    /**
     * Search and return the ACL as string for the given resource and action.
     *
     * @param account
     * @param resourceName
     * @param id
     * @param action
     * @param def
     * @return The ACL
     */
    String getResourceAccessAcl(
            Account account, String resourceName, String id, String action, String def);

    /**
     * Return the ACL as string for the given action.
     *
     * @param account
     * @param aclName
     * @param action
     * @param def
     * @return The ACL
     */
    String getGroupAccessAcl(Account account, String aclName, String action, String def);

    /**
     * Login as the user.
     *
     * @param user
     * @param locale
     * @return Current access context for this thread.
     */
    AaaContext processUserSession(String user, Locale locale);

    /**
     * Return a API to modify the current user.
     *
     * @return The API or null if not supported
     * @throws MException if current user can't be located (see getCurrentAccount)
     */
    ModifyCurrentAccountApi getModifyCurrentAccountApi() throws MException;

    /**
     * Return a API to modify Accounts. It's usually the AccountSource.
     *
     * @return The API or null if not supported
     */
    ModifyAccountApi getModifyAccountApi();

    /**
     * Return a API to modify Authorization.
     *
     * @return The API or null if not supported
     */
    ModifyAuthorizationApi getModifyAuthorizationApi();

    /**
     * Return a API to modify Trusts.
     *
     * @return The API or null if not supported
     */
    ModifyTrustApi getModifyTrustApi();
}
