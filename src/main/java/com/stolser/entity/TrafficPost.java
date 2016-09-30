package com.stolser.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "posts")
public class TrafficPost {
    @Id private String id;
    private String systemId; // todo: create a unique index
    private String name;
    private Set<Road> roads;
    @Version private Long version;

    public TrafficPost(String systemId, String name) {
        this.systemId = systemId; // todo: check for uniqueness;
        this.name = name;
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

    public Set<Road> getRoads() {
        return roads;
    }

    public void addRoad(Road newRoad) {
        if (roads == null) roads = new HashSet<>();
        roads.add(newRoad);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrafficPost)) return false;

        TrafficPost that = (TrafficPost) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("TrafficPost{name: '%s', systemId: '%s'}", name, systemId);
    }
}
