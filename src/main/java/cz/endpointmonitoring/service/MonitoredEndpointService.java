package cz.endpointmonitoring.service;

import cz.endpointmonitoring.model.MonitoredEndpoint;
import cz.endpointmonitoring.model.MonitoringResult;
import cz.endpointmonitoring.model.MonitoringUser;
import cz.endpointmonitoring.repository.MonitoredEndpointRepository;
import org.apache.commons.validator.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@Service
public class MonitoredEndpointService {
    @Autowired
    private MonitoredEndpointRepository monitoredEndpointRepository;
    @Autowired
    private MonitoringService monitoringService;
    @Autowired
    private MonitoringResultService monitoringResultService;

    public Collection<MonitoredEndpoint> findAllByUser(final MonitoringUser monitoringUser) {
        return monitoredEndpointRepository.findAllByMonitoringUser(monitoringUser);
    }

    private Boolean isEndpointValid(MonitoredEndpoint monitoredEndpoint) {
        return new UrlValidator().isValid(monitoredEndpoint.getUrl())
                && monitoredEndpoint.getMonitoringInterval() > 0;
    }

    public MonitoredEndpoint insert(MonitoredEndpoint monitoredEndpoint) {
        return insert(monitoredEndpoint, null);
    }
    public MonitoredEndpoint insert(MonitoredEndpoint monitoredEndpoint, MonitoringUser monitoringUser) {
        if (!isEndpointValid(monitoredEndpoint)) {
            return null;
        }

        // ensuring that the endpoint is "clean" before inserting to database
        monitoredEndpoint.setId(null);
        monitoredEndpoint.setMonitoringUser(monitoringUser);
        monitoredEndpoint.setDateOfCreation(Instant.now());
        monitoredEndpoint.setDateOfLastCheck(null);

        monitoredEndpoint = monitoredEndpointRepository.save(monitoredEndpoint);

        monitoringService.start(monitoredEndpoint);
        return monitoredEndpoint;
    }

    public Optional<MonitoredEndpoint> findById(Long id) {
        return monitoredEndpointRepository.findById(id);
    }

    public Optional<MonitoredEndpoint> findByIdAndUser(Long id, MonitoringUser monitoringUser) {
        return monitoredEndpointRepository.findByIdAndMonitoringUser(id, monitoringUser);
    }

    public MonitoredEndpoint update(MonitoredEndpoint monitoredEndpoint) {
        return update(findById(monitoredEndpoint.getId()), monitoredEndpoint, true);
    }

    public MonitoredEndpoint update(Long id, MonitoringUser monitoringUser, MonitoredEndpoint monitoredEndpoint) {
        return update(findByIdAndUser(id, monitoringUser), monitoredEndpoint, false);
    }
    private MonitoredEndpoint update(Optional<MonitoredEndpoint> optionalMonitoredEndpoint, MonitoredEndpoint monitoredEndpoint, Boolean isInternalCall) {
        if (optionalMonitoredEndpoint.isPresent()) {
            MonitoredEndpoint oldEndpoint = optionalMonitoredEndpoint.get();
            if (monitoredEndpoint.getName() != null) {
                oldEndpoint.setName(monitoredEndpoint.getName());
            }
            // only the application itself can modify date of last check
            if (isInternalCall && monitoredEndpoint.getDateOfLastCheck() != null) {
                oldEndpoint.setDateOfLastCheck(monitoredEndpoint.getDateOfLastCheck());
            }
            // on monitoring interval change it is necessary to update the async task
            if (monitoredEndpoint.getMonitoringInterval() != null && monitoredEndpoint.getMonitoringInterval() > 0) {
                oldEndpoint.setMonitoringInterval(monitoredEndpoint.getMonitoringInterval());
                monitoringService.update(oldEndpoint);
            }
            return monitoredEndpointRepository.save(oldEndpoint);
        } else {
            return null;
        }
    }

    public long delete(Long id, MonitoringUser monitoringUser) {
        Optional<MonitoredEndpoint> optionalMonitoredEndpoint = findByIdAndUser(id, monitoringUser);
        if (optionalMonitoredEndpoint.isPresent()) {
            MonitoredEndpoint oldEndpoint = optionalMonitoredEndpoint.get();
            monitoringService.delete(oldEndpoint);
            long deletedResults = monitoringResultService.deleteByMonitoredEndpoint(oldEndpoint);
            monitoredEndpointRepository.delete(oldEndpoint);
            return deletedResults;
        } else {
            return -1;
        }
    }

    public Collection<MonitoringResult> getTop10Results(Long id, MonitoringUser monitoringUser) {
        return findByIdAndUser(id, monitoringUser)
                .map(monitoredEndpoint -> monitoringResultService.findTop10ByMonitoredEndpoint(monitoredEndpoint))
                .orElse(null);
    }
}
