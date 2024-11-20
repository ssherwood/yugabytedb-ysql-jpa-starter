package io.undertree.starter.ysqljpa.samples.json;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "json_as_map")
public class JsonAsMapDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NaturalId
    @Column(length = 10)
    private String alternateId; // in the form of nnnnnnnnnn

    private Integer points;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> metadata = new HashMap<>();

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    //

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAlternateId() {
        return alternateId;
    }

    public void setAlternateId(String alternateId) {
        this.alternateId = alternateId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
