package com.stolser.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "roads")
public class Road {
    @Id private String id;
    private String systemId; // todo: create a unique index
    private TrafficPost firstTPost;
    private TrafficPost secondTPost;
    private double length;
    private BigDecimal fare;

    public Road(String systemId, TrafficPost firstTPost, TrafficPost secondTPost, double length, BigDecimal fare) {
        this.systemId = systemId; // todo: check for uniqueness;
        this.firstTPost = firstTPost;
        this.secondTPost = secondTPost;
        this.length = length;
        this.fare = fare;
    }

    public Set<TrafficPost> getTrafficPosts() {
        return new HashSet<>(Arrays.asList(firstTPost, secondTPost));
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

        return id != null ? id.equals(road.id) : road.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("Road{'%s' (%s - %s)}", systemId, firstTPost, secondTPost);
    }
}
