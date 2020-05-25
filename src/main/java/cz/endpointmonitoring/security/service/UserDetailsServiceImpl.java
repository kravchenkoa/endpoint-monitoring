package cz.endpointmonitoring.security.service;

import cz.endpointmonitoring.model.MonitoringUser;
import cz.endpointmonitoring.service.MonitoringUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    MonitoringUserService monitoringUserService;

    @Override
    public UserDetails loadUserByUsername(String accessToken) throws UsernameNotFoundException {
        MonitoringUser monitoringUser = monitoringUserService.findByAccessToken(accessToken).orElseThrow(() -> new UsernameNotFoundException("No user for access token: " + accessToken));
        return UserDetailsImpl.build(monitoringUser);
    }
}
