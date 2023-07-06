package com.jnetdata.msp.core.model.util;


import java.util.Collection;
import java.util.List;

/**
 * 当前登录主体
 */
public interface ISubject {

    /**
     * Returns {@code true} if this Subject is permitted to perform an action or access a resource summarized by the
     * specified permission string.
     * @param permission the String representation of a Permission that is being checked.
     * @return true if this Subject is permitted, false otherwise.
     */
    boolean isPermitted(String permission);

    /**
     * Checks if this Subject implies the given permission strings and returns a boolean array indicating which
     * permissions are implied.
     * @param permissions the String representations of the Permissions that are being checked.
     * @return a boolean array where indices correspond to the index of the
     *         permissions in the given list.  A true value at an index indicates this Subject is permitted for
     *         for the associated {@code Permission} string in the list.  A false value at an index
     *         indicates otherwise.
     */
    boolean[] isPermitted(String... permissions);

    /**
     * Returns {@code true} if this Subject implies all of the specified permission strings, {@code false} otherwise.
     * <p/>
     * @param permissions the String representations of the Permissions that are being checked.
     * @return true if this Subject has all of the specified permissions, false otherwise.
     */
    boolean isPermittedAll(String... permissions);

    /**
     * Ensures this Subject implies the specified permission String.
     * <p/>
     * If this subject's existing associated permissions do not {@link Permission#implies(Permission)} imply}
     * the given permission, an {@link org.apache.shiro.authz.AuthorizationException} will be thrown.
     * <p/>
     * @param permission the String representation of the Permission to check.
     * @throws org.apache.shiro.authz.AuthorizationException
     *          if the user does not have the permission.
     */
    void checkPermission(String permission);

    /**
     * Returns {@code true} if this Subject has the specified role, {@code false} otherwise.
     *
     * @param roleIdentifier the application-specific role identifier (usually a role id or role name).
     * @return {@code true} if this Subject has the specified role, {@code false} otherwise.
     */
    boolean hasRole(String roleIdentifier);

    /**
     * Checks if this Subject has the specified roles, returning a boolean array indicating
     * which roles are associated.
     * <p/>
     * This is primarily a performance-enhancing method to help reduce the number of
     * {@link #hasRole} invocations over the wire in client/server systems.
     *
     * @param roleIdentifiers the application-specific role identifiers to check (usually role ids or role names).
     * @return a boolean array where indices correspond to the index of the
     *         roles in the given identifiers.  A true value indicates this Subject has the
     *         role at that index.  False indicates this Subject does not have the role at that index.
     */
    boolean[] hasRoles(List<String> roleIdentifiers);

    /**
     * Returns {@code true} if this Subject has all of the specified roles, {@code false} otherwise.
     *
     * @param roleIdentifiers the application-specific role identifiers to check (usually role ids or role names).
     * @return true if this Subject has all the roles, false otherwise.
     */
    boolean hasAllRoles(Collection<String> roleIdentifiers);


    /**
     * Asserts this Subject has the specified role by returning quietly if they do or throwing an
     * {@link org.apache.shiro.authz.AuthorizationException} if they do not.
     *
     * @param roleIdentifier the application-specific role identifier (usually a role id or role name ).
     * @throws org.apache.shiro.authz.AuthorizationException
     *          if this Subject does not have the role.
     */
    void checkRole(String roleIdentifier);

    /**
     * Asserts this Subject has all of the specified roles by returning quietly if they do or throwing an
     * {@link org.apache.shiro.authz.AuthorizationException} if they do not.
     *
     * @param roleIdentifiers the application-specific role identifiers to check (usually role ids or role names).
     * @throws org.apache.shiro.authz.AuthorizationException
     *          if this Subject does not have all of the specified roles.
     */
    void checkRoles(Collection<String> roleIdentifiers);

    /**
     * Same as {@link #checkRoles(Collection<String> roleIdentifiers) checkRoles(Collection<String> roleIdentifiers)} but
     * doesn't require a collection as a an argument.
     * Asserts this Subject has all of the specified roles by returning quietly if they do or throwing an
     * {@link org.apache.shiro.authz.AuthorizationException} if they do not.
     *
     * @param roleIdentifiers roleIdentifiers the application-specific role identifiers to check (usually role ids or role names).
     * @throws AuthorizationException org.apache.shiro.authz.AuthorizationException
     *          if this Subject does not have all of the specified roles.
     */
    void checkRoles(String... roleIdentifiers);

    /**
     * Returns {@code true} if this Subject/user proved their identity <em>during their current session</em>
     * by providing valid credentials matching those known to the system, {@code false} otherwise.
     * <p/>
     * Note that even if this Subject's identity has been remembered via 'remember me' services, this method will
     * still return {@code false} unless the user has actually logged in with proper credentials <em>during their
     * current session</em>.  See the {@link #isRemembered() isRemembered()} method JavaDoc for more.
     *
     * @return {@code true} if this Subject proved their identity during their current session
     *         by providing valid credentials matching those known to the system, {@code false} otherwise.
     */
    boolean isAuthenticated();


    /**
     * Returns {@code true} if this {@code Subject} has an identity (it is not anonymous) and the identity
     * (aka {@link #getPrincipals() principals}) is remembered from a successful authentication during a previous
     * session.
     * <p/>
     * Although the underlying implementation determines exactly how this method functions, most implementations have
     * this method act as the logical equivalent to this code:
     * <pre>
     * {@link #getPrincipal() getPrincipal()} != null && !{@link #isAuthenticated() isAuthenticated()}</pre>
     * <p/>
     * Note as indicated by the above code example, if a {@code Subject} is remembered, they are
     * <em>NOT</em> considered authenticated.  A check against {@link #isAuthenticated() isAuthenticated()} is a more
     * strict check than that reflected by this method.  For example, a check to see if a subject can access financial
     * information should almost always depend on {@link #isAuthenticated() isAuthenticated()} to <em>guarantee</em> a
     * verified identity, and not this method.
     * <p/>
     * Once the subject is authenticated, they are no longer considered only remembered because their identity would
     * have been verified during the current session.
     * <h4>Remembered vs Authenticated</h4>
     * Authentication is the process of <em>proving</em> you are who you say you are.  When a user is only remembered,
     * the remembered identity gives the system an idea who that user probably is, but in reality, has no way of
     * absolutely <em>guaranteeing</em> if the remembered {@code Subject} represents the user currently
     * using the application.
     * <p/>
     * So although many parts of the application can still perform user-specific logic based on the remembered
     * {@link #getPrincipals() principals}, such as customized views, it should never perform highly-sensitive
     * operations until the user has legitimately verified their identity by executing a successful authentication
     * attempt.
     * <p/>
     * We see this paradigm all over the web, and we will use <a href="http://www.amazon.com">Amazon.com</a> as an
     * example:
     * <p/>
     * When you visit Amazon.com and perform a login and ask it to 'remember me', it will set a cookie with your
     * identity.  If you don't log out and your session expires, and you come back, say the next day, Amazon still knows
     * who you <em>probably</em> are: you still see all of your book and movie recommendations and similar user-specific
     * features since these are based on your (remembered) user id.
     * <p/>
     * BUT, if you try to do something sensitive, such as access your account's billing data, Amazon forces you
     * to do an actual log-in, requiring your username and password.
     * <p/>
     * This is because although amazon.com assumed your identity from 'remember me', it recognized that you were not
     * actually authenticated.  The only way to really guarantee you are who you say you are, and therefore allow you
     * access to sensitive account data, is to force you to perform an actual successful authentication.  You can
     * check this guarantee via the {@link #isAuthenticated() isAuthenticated()} method and not via this method.
     *
     * @return {@code true} if this {@code Subject}'s identity (aka {@link #getPrincipals() principals}) is
     *         remembered from a successful authentication during a previous session, {@code false} otherwise.
     */
    boolean isRemembered();


}
