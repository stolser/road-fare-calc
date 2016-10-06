package com.stolser.entity;

import static com.google.common.base.Preconditions.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Document(collection = "trackers")
public class UserTracker {
    @Id private String id;
    @DBRef
    private User user;
    private UserTrackerStatus status;
    @DBRef
    private Map<Long, TrafficPost> trafficPosts;
    @DBRef
    private List<Road> roads;

    public UserTracker(User user) {
        this.user = user;
        this.trafficPosts = new TreeMap<>();
        this.roads = new ArrayList<>();
    }

    public void addNewTrafficPost(TrafficPost trafficPostId, Long timestamp) {
        checkNotNull(trafficPostId, "trafficPostId cannot be null.");
        trafficPosts.put(timestamp, trafficPostId);
    }

    public void addNewRoad(Road road) {
        checkNotNull(road, "road cannot be null.");
        roads.add(road);
    }

    public void setStatus(UserTrackerStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public UserTrackerStatus getStatus() {
        return status;
    }

    public Map<Long, TrafficPost> getTrafficPosts() {
        return trafficPosts;
    }

    public List<Road> getRoads() {
        return roads;
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
        return String.format("UserTracker{\n\t_id: '%s', \n\tuser: %s, \n\tstatus: %s, " +
                "\n\ttrafficPosts.size: %d, \n\troads.size: %d}\n---------------------------",
                id, user, status, trafficPosts.size(), roads.size());
    }
}
