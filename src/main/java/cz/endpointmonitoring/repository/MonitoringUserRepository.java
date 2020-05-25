package cz.endpointmonitoring.repository;

import cz.endpointmonitoring.model.MonitoringUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonitoringUserRepository extends JpaRepository<MonitoringUser, Long> {
    Optional<MonitoringUser> findByAccessToken(String accessToken);
}
