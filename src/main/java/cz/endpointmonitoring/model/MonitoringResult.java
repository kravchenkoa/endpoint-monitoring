package cz.endpointmonitoring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;
import java.time.Instant;

@Table
@Entity
@Data
@NoArgsConstructor
public class MonitoringResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private Instant dateOfCheck;
    @Getter
    @Setter
    private Integer returnedHttpStatusCode;
    @Getter
    @Setter
    private String returnedPayload;
    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MonitoredEndpoint monitoredEndpoint;
}
