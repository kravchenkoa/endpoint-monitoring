package cz.endpointmonitoring.service;

import cz.endpointmonitoring.model.MonitoringUser;
import cz.endpointmonitoring.repository.MonitoringUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MonitoringUserService {
    @Autowired
    private MonitoringUserRepository monitoringUserRepository;

    public Optional<MonitoringUser> findByAccessToken(final String accessToken) {
        return monitoringUserRepository.findByAccessToken(accessToken);
    }

    public MonitoringUser insert(final MonitoringUser user) {
        return monitoringUserRepository.save(user);
    }

    public void deleteAll() {
        monitoringUserRepository.deleteAll();
    }
}
