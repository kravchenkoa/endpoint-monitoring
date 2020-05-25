package cz.endpointmonitoring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;
import java.time.Instant;

@Table
@Entity
@Data
@NoArgsConstructor
public class MonitoredEndpoint {
    private static final Integer DEFAULT_MONITORING_INTERVAL = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String url;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Instant dateOfCreation;
    @Getter
    @Setter
    private Instant dateOfLastCheck;
    @Getter
    @Setter
    private Integer monitoringInterval = DEFAULT_MONITORING_INTERVAL;
    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MonitoringUser monitoringUser;
}
