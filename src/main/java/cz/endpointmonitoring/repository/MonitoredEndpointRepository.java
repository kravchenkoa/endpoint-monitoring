package cz.endpointmonitoring.repository;

import cz.endpointmonitoring.model.MonitoredEndpoint;
import cz.endpointmonitoring.model.MonitoringUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface MonitoredEndpointRepository extends JpaRepository<MonitoredEndpoint, Long> {
    Collection<MonitoredEndpoint> findAllByMonitoringUser(final MonitoringUser user);
    Optional<MonitoredEndpoint> findByIdAndMonitoringUser(final Long id, final MonitoringUser monitoringUser);
}
