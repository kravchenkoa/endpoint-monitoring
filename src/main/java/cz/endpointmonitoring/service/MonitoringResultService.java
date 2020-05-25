package cz.endpointmonitoring.service;

import cz.endpointmonitoring.model.MonitoredEndpoint;
import cz.endpointmonitoring.model.MonitoringResult;
import cz.endpointmonitoring.repository.MonitoringResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MonitoringResultService {
    @Autowired
    private MonitoringResultRepository monitoringResultRepository;

    public MonitoringResult insert(MonitoringResult monitoringResult) {
        monitoringResult.setId(null);
        return monitoringResultRepository.save(monitoringResult);
    }

    public Collection<MonitoringResult> findTop10ByMonitoredEndpoint(final MonitoredEndpoint monitoredEndpoint) {
        return monitoringResultRepository.findTop10ByMonitoredEndpointOrderByDateOfCheckDesc(monitoredEndpoint);
    }

    public long deleteByMonitoredEndpoint(final MonitoredEndpoint monitoredEndpoint) {
        return monitoringResultRepository.deleteByMonitoredEndpoint(monitoredEndpoint);
    }
}
