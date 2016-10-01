package com.stolser.entity;

import static org.apache.commons.collections.CollectionUtils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.*;

@Document(collection = "posts")
public class TrafficPost {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrafficPost.class);
    @Id private String id;
    @Indexed(unique = true, name = "systemId", direction = IndexDirection.ASCENDING)
    private String systemId;
    private String name;
    @DBRef
    private List<Road> roads;
    @Version private Long version;

    public TrafficPost(String systemId, String name) {
        LOGGER.debug("constructor...");
        checkNotNull(systemId, "systemId cannot be null.");
        this.systemId = systemId;
        this.name = name;
        this.roads = new ArrayList<>();
    }

    public void register(Car car) {
        // send a notification to the server;
    }

    public String getSystemId() {
        return systemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Road> getRoads() {
        return roads;
    }

    public void setRoads(List<Road> roads) {
        LOGGER.debug("setting roads ...");
        this.roads = roads;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrafficPost)) return false;

        TrafficPost that = (TrafficPost) o;

        return systemId.equals(that.systemId);

    }

    @Override
    public int hashCode() {
        return systemId.hashCode();
    }

    @Override
    public String toString() {
        return String.format("TrafficPost{name: '%s', systemId: '%s', " +
                "roadN: %d}", name, systemId, size(roads));
    }
}
