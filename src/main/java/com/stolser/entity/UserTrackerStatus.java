package com.stolser.entity;

/**
 *Designate the current status of a user inside UserTracker object.
 * 'LEFT_AUTOBAHN' is the final status for each UserTracker.
 */
public enum UserTrackerStatus {
    AT_TRAFFIC_POST("In the process of registration"),
    ON_THE_ROAD("Has hit the road"),
    LEFT_AUTOBAHN("Has left the autobahn");

    private String description;

    UserTrackerStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
