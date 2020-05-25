package cz.endpointmonitoring.service;

import cz.endpointmonitoring.model.MonitoredEndpoint;
import cz.endpointmonitoring.model.MonitoringResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class MonitoringService {
    @Autowired
    private MonitoringResultService monitoringResultService;
    @Autowired
    private MonitoredEndpointService monitoredEndpointService;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
    private final Map<Long, ScheduledFuture<?>> scheduledTasksByMonitoredEndpointId = new HashMap<>();

    private class MonitoringTask implements Runnable {
        private static final String DEFAULT_HTTP_PROTOCOL = "GET";
        // todo mysql inserts fail when trying to insert a longer payload
        private static final int PAYLOAD_MAX_LENGTH = 255;
        private MonitoredEndpoint monitoredEndpoint;


        public MonitoringTask(final MonitoredEndpoint monitoredEndpoint) {
            this.monitoredEndpoint = new MonitoredEndpoint();
            this.monitoredEndpoint.setId(monitoredEndpoint.getId());
            this.monitoredEndpoint.setUrl(monitoredEndpoint.getUrl());
            this.monitoredEndpoint.setMonitoringInterval(null);
        }

        public void run() {
            try {
                URL url = new URL(monitoredEndpoint.getUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(DEFAULT_HTTP_PROTOCOL);
                String returnedPayload = new BufferedReader(new InputStreamReader(connection.getInputStream()))
                        .lines().collect(Collectors.joining("\n")).substring(0,PAYLOAD_MAX_LENGTH);

                System.out.println(monitoredEndpoint.getId() + " | " +  monitoredEndpoint.getUrl() + " " + Instant.now() + " | returned code " + connection.getResponseCode());

                MonitoringResult monitoringResult = new MonitoringResult();
                monitoringResult.setDateOfCheck(Instant.now());
                monitoringResult.setReturnedHttpStatusCode(connection.getResponseCode());
                monitoringResult.setReturnedPayload(returnedPayload);
                monitoringResult.setMonitoredEndpoint(monitoredEndpoint);
                monitoringResultService.insert(monitoringResult);

                monitoredEndpoint.setDateOfLastCheck(monitoringResult.getDateOfCheck());
                monitoredEndpointService.update(monitoredEndpoint);
            } catch (IOException ignored) {}
        }
    }

    public void start(final MonitoredEndpoint monitoredEndpoint) {
        MonitoringTask monitoringTask = new MonitoringTask(monitoredEndpoint);
        ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(monitoringTask, 0,
                monitoredEndpoint.getMonitoringInterval(), TimeUnit.SECONDS);
        scheduledTasksByMonitoredEndpointId.put(monitoredEndpoint.getId(), future);
    }

    public void update(final MonitoredEndpoint monitoredEndpoint) {
        delete(monitoredEndpoint);
        start(monitoredEndpoint);
    }

    public void delete(final MonitoredEndpoint monitoredEndpoint) {
        ScheduledFuture<?> future = scheduledTasksByMonitoredEndpointId.get(monitoredEndpoint.getId());
        future.cancel(false);
        scheduledTasksByMonitoredEndpointId.remove(monitoredEndpoint.getId());
    }

    public void deleteAll() {
        for (ScheduledFuture<?> future : scheduledTasksByMonitoredEndpointId.values()) {
            future.cancel(false);
        }
        scheduledTasksByMonitoredEndpointId.clear();
    }
}
