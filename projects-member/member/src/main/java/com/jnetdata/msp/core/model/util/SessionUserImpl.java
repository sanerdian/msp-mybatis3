package com.jnetdata.msp.core.model.util;

import com.jnetdata.msp.member.user.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class SessionUserImpl implements ISessionUser {
    @Override
    public IUser<Long> getCurrentUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null){
            throw new NoCurrentUserException();
        }
        return user;
    }

    @Override
    public IUser<Long> getCurrentUserWithoutException() {
        try {
            return (User) SecurityUtils.getSubject().getPrincipal();
        }catch(Exception e) {
            return null;
        }
    }

    @Override
    public ISubject getSubject() {
        Subject subject = SecurityUtils.getSubject();
        return from(subject);
    }

    private static ISubject from(Subject subject) {
        return new ISubject() {
            @Override
            public boolean isPermitted(String permission) {
                return subject.isPermitted(permission);
            }

            @Override
            public boolean[] isPermitted(String... permissions) {
                return subject.isPermitted(permissions);
            }

            @Override
            public boolean isPermittedAll(String... permissions) {
                return subject.isPermittedAll(permissions);
            }

            @Override
            public void checkPermission(String permission) {
                subject.checkPermissions(permission);
            }

            @Override
            public boolean hasRole(String roleIdentifier) {
                return subject.hasRole(roleIdentifier);
            }

            @Override
            public boolean[] hasRoles(List<String> roleIdentifiers) {
                return subject.hasRoles(roleIdentifiers);
            }

            @Override
            public boolean hasAllRoles(Collection<String> roleIdentifiers) {
                return subject.hasAllRoles(roleIdentifiers);
            }

            @Override
            public void checkRole(String roleIdentifier) {
                subject.checkRole(roleIdentifier);
            }

            @Override
            public void checkRoles(Collection<String> roleIdentifiers) {
                subject.checkRoles(roleIdentifiers);
            }

            @Override
            public void checkRoles(String... roleIdentifiers) {
                subject.checkRoles(roleIdentifiers);
            }

            @Override
            public boolean isAuthenticated() {
                return subject.isAuthenticated();
            }

            @Override
            public boolean isRemembered() {
                return subject.isRemembered();
            }
        };
    }

}
