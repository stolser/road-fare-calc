package com.stolser.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Document(collection = "roads")
public class Road {
    private static final Logger LOGGER = LoggerFactory.getLogger(Road.class);
    @Id private String id;
    @Indexed(unique = true, name = "systemId", direction = IndexDirection.ASCENDING)
    private String systemId;
    private List<String> postSystemIds;
    private double length;
    private BigDecimal fare;

    public Road(String systemId, List<String> postSystemIds, double length, BigDecimal fare) {
        // todo: check postSystemIds: 2 elements, not null
        LOGGER.debug("Road constructor...: sysId = {}; postSystemIds = {}", systemId, postSystemIds);
        checkNotNull(systemId, "systemId cannot be null.");
        this.systemId = systemId;
        this.postSystemIds = postSystemIds;
        this.length = length;
        this.fare = fare;
    }

    public List<String> getPostSystemIds() {
        return postSystemIds;
    }

    public String getSystemId() {
        return systemId;
    }

    public double getLength() {
        return length;
    }

    public BigDecimal getFare() {
        return fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Road)) return false;

        Road road = (Road) o;

        return systemId.equals(road.systemId);
    }

    @Override
    public int hashCode() {
        return systemId.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Road{'%s' ('%s' - '%s')}", systemId, postSystemIds.get(0), postSystemIds.get(1));
    }
}
