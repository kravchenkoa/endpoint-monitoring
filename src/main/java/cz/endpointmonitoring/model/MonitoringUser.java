package cz.endpointmonitoring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;

@Table
@Entity
@Data
@NoArgsConstructor
public class MonitoringUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Getter
    @Setter
    private String userName;
    @Getter
    @Setter
    private String email;
    @Column(unique = true)
    @Getter
    @Setter
    private String accessToken;

    public MonitoringUser(String userName, String email, String accessToken) {
        this.userName = userName;
        this.email = email;
        this.accessToken = accessToken;
    }
}
