package com.stolser.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Document(collection = "trackers")
public class UserTracker {
    @Id private String id;
    private long userId;
    private Map<Instant, String> trafficPostIds; // TrafficPost systemIds
    private List<String> roadIds;// Road systemIds

    public UserTracker(long userId) {
        this.userId = userId;
        this.trafficPostIds = new TreeMap<>();
        this.roadIds = new ArrayList<>();
    }

    public void addNewTrafficPost(String trafficPostId, Instant date, String roadId) {
        trafficPostIds.put(date, trafficPostId);
        roadIds.add(roadId);
    }

    public long getUserId() {
        return userId;
    }

    public Map<Instant, String> getTrafficPostIds() {
        return trafficPostIds;
    }

    public List<String> getRoadIds() {
        return roadIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTracker)) return false;

        UserTracker that = (UserTracker) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("UserTracker{userId: %d}", userId);
    }
}
