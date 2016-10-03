package com.stolser.entity;

import java.io.Serializable;

public class Message implements Serializable {
    private String tpSystemId;
    private long userId;
    private UserTrackerStatus status;
    private String roadSystemId;
    private long timestamp;

    public Message(String tpSystemId, long userId, UserTrackerStatus status, String roadSystemId, long timestamp) {
        this.tpSystemId = tpSystemId;
        this.userId = userId;
        this.status = status;
        this.roadSystemId = roadSystemId;
        this.timestamp = timestamp;
    }

    public String getTpSystemId() {
        return tpSystemId;
    }

    public long getUserId() {
        return userId;
    }

    public UserTrackerStatus getStatus() {
        return status;
    }

    public String getRoadSystemId() {
        return roadSystemId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "tpSystemId='" + tpSystemId + '\'' +
                ", userId=" + userId +
                ", status=" + status +
                ", roadSystemId='" + roadSystemId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
