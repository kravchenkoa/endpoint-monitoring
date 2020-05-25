package cz.endpointmonitoring.security.service;

import cz.endpointmonitoring.model.MonitoringUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    @Getter
    private final MonitoringUser monitoringUser;

    public UserDetailsImpl(final MonitoringUser monitoringUser) {
        this.monitoringUser = monitoringUser;
    }

    public static UserDetailsImpl build(final MonitoringUser monitoringUser) {
        return new UserDetailsImpl(monitoringUser);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return monitoringUser.getAccessToken();
    }

    @Override
    public String getUsername() {
        return monitoringUser.getAccessToken();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
