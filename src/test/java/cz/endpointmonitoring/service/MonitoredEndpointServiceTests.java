package cz.endpointmonitoring.service;

import cz.endpointmonitoring.model.MonitoredEndpoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MonitoredEndpointServiceTests {
    @Autowired
    MonitoredEndpointService monitoredEndpointService;
    private static final String DEFAULT_URL = "http://www.google.com";
    private static final int DEFAULT_INTERVAL = 10;

    @Test
    public void testInsertInvalidMonitoredEndpoint() {
        MonitoredEndpoint noUrl = new MonitoredEndpoint();
        MonitoredEndpoint invalidUrl = new MonitoredEndpoint();
        invalidUrl.setUrl("this is not a valid url");
        MonitoredEndpoint invalidInterval = new MonitoredEndpoint();
        invalidInterval.setUrl(DEFAULT_URL);
        invalidInterval.setMonitoringInterval(-5);

        Assertions.assertNull(monitoredEndpointService.insert(noUrl));
        Assertions.assertNull(monitoredEndpointService.insert(invalidUrl));
        Assertions.assertNull(monitoredEndpointService.insert(invalidInterval));
    }

    private MonitoredEndpoint insertValidEndpoint() {
        MonitoredEndpoint monitoredEndpoint = new MonitoredEndpoint();
        monitoredEndpoint.setUrl(DEFAULT_URL);
        return monitoredEndpointService.insert(monitoredEndpoint);
    }

    @Test
    public void testInsertValidEndpoint() {
        Assertions.assertNotNull(insertValidEndpoint().getId());
    }

    @Test
    public void testUpdateMonitoredEndpoint() {
        MonitoredEndpoint monitoredEndpoint = insertValidEndpoint();
        monitoredEndpoint.setUrl("http://www.reddit.com");
        String updatedName = "updated name";
        monitoredEndpoint.setName(updatedName);
        int updatedInterval = DEFAULT_INTERVAL + 10;
        monitoredEndpoint.setMonitoringInterval(updatedInterval);

        monitoredEndpoint = monitoredEndpointService.update(monitoredEndpoint);

        Assertions.assertEquals(DEFAULT_URL, monitoredEndpoint.getUrl());
        Assertions.assertEquals(updatedName, monitoredEndpoint.getName());
        Assertions.assertEquals(updatedInterval, monitoredEndpoint.getMonitoringInterval());
    }
}
