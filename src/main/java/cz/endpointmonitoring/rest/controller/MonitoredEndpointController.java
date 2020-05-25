package cz.endpointmonitoring.rest.controller;

import cz.endpointmonitoring.model.MonitoredEndpoint;
import cz.endpointmonitoring.model.MonitoringResult;
import cz.endpointmonitoring.model.MonitoringUser;
import cz.endpointmonitoring.security.service.UserDetailsImpl;
import cz.endpointmonitoring.service.MonitoredEndpointService;
import cz.endpointmonitoring.service.MonitoringUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/monitoredEndpoints")
public class MonitoredEndpointController {
    @Autowired
    private MonitoredEndpointService monitoredEndpointService;

    private MonitoringUser getMonitoringUser() {
        return ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMonitoringUser();
    }

    @GetMapping
    public @ResponseBody Collection<MonitoredEndpoint> getAll() {
        return monitoredEndpointService.findAllByUser(getMonitoringUser());
    }

    @PostMapping
    public @ResponseBody MonitoredEndpoint post(@RequestBody MonitoredEndpoint monitoredEndpoint) {
        return monitoredEndpointService.insert(monitoredEndpoint, getMonitoringUser());
    }

    @GetMapping("/{id}")
    public @ResponseBody Optional<MonitoredEndpoint> get(@PathVariable Long id) {
        return monitoredEndpointService.findByIdAndUser(id, getMonitoringUser());
    }

    @PutMapping("/{id}")
    public @ResponseBody MonitoredEndpoint update(@PathVariable Long id, @RequestBody MonitoredEndpoint monitoredEndpoint) {
        return monitoredEndpointService.update(id, getMonitoringUser(), monitoredEndpoint);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody long delete(@PathVariable Long id) {
        return monitoredEndpointService.delete(id, getMonitoringUser());
    }

    @GetMapping("/{id}/results")
    public @ResponseBody Collection<MonitoringResult> getTop10Results(@PathVariable Long id) {
        return monitoredEndpointService.getTop10Results(id, getMonitoringUser());
    }
}
