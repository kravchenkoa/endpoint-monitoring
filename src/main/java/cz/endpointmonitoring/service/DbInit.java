package cz.endpointmonitoring.service;

import cz.endpointmonitoring.model.MonitoredEndpoint;
import cz.endpointmonitoring.model.MonitoringResult;
import cz.endpointmonitoring.model.MonitoringUser;
import cz.endpointmonitoring.repository.MonitoredEndpointRepository;
import cz.endpointmonitoring.repository.MonitoringResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DbInit {
    @Autowired
    private MonitoringUserService monitoringUserService;
    @Autowired
    private MonitoringResultRepository monitoringResultRepository;
    @Autowired
    private MonitoredEndpointRepository monitoredEndpointRepository;
    @Autowired
    private MonitoringService monitoringService;


    @EventListener
    public void init(ApplicationReadyEvent event) {
        monitoringService.deleteAll();
        monitoringUserService.deleteAll();
        monitoredEndpointRepository.deleteAll();
        monitoringResultRepository.deleteAll();

        MonitoringUser applifting = new MonitoringUser("Applifting", "info@applifting.cz", "93f39e2f-80de-4033-99ee-249d92736a25");
        MonitoringUser batman = new MonitoringUser("batman", "batman@example.com", "dcb20f8a-5657-4f1b-9f7f-ce65739b359e");

        monitoringUserService.insert(applifting);
        monitoringUserService.insert(batman);

        try {
            MonitoringUser foundUser = monitoringUserService.findByAccessToken("dcb20f8a-5657-4f1b-9f7f-ce65739b359e").orElseThrow(() -> new BadCredentialsException(""));
            System.out.println("found user? " + foundUser);
        } catch (BadCredentialsException e) {
            System.out.println("user ?inserted? but not found");
        }
    }
}
