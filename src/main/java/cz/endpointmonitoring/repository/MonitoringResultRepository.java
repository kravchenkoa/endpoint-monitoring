package cz.endpointmonitoring.repository;

import cz.endpointmonitoring.model.MonitoredEndpoint;
import cz.endpointmonitoring.model.MonitoringResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;

@Repository
public interface MonitoringResultRepository extends JpaRepository<MonitoringResult, Long> {
    Collection<MonitoringResult> findTop10ByMonitoredEndpointOrderByDateOfCheckDesc(final MonitoredEndpoint monitoredEndpoint);

    @Transactional
    long deleteByMonitoredEndpoint(final MonitoredEndpoint monitoredEndpoint);
}
