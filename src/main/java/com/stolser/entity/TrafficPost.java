package com.stolser.entity;

import static org.apache.commons.collections.CollectionUtils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.*;

@Document(collection = "posts")
public class TrafficPost {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrafficPost.class);
    private static final Marker carStatusUpdate = MarkerFactory.getMarker("carStatusUpdate");
    @Id private String id;
    @Indexed(unique = true, name = "systemId", direction = IndexDirection.ASCENDING)
    private String systemId;
    private String name;
    @DBRef
    private List<Road> roads;
    @Version private Long version;

    public TrafficPost(String systemId, String name) {
        LOGGER.debug("TrafficPost constructor...: sysId = {}", systemId);
        checkNotNull(systemId, "systemId cannot be null.");
        this.systemId = systemId;
        this.name = name;
        this.roads = new ArrayList<>();
    }

    public synchronized void register(Car car) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOGGER.debug(carStatusUpdate, String.format("Client. TP: %s:\n\tuser: %s;\n\tstatus: %s;\n" +
                "\troad: %s;\n\tdate: %s\n--------------------------------\n",
                car.getCurrentTrafficPost(), car.getDriver(), car.getStatus(), car.getCurrentRoad(),
                new SimpleDateFormat("dd.MM HH:mm:ss").format(new Date())));

        try (Socket socket = new Socket("localhost", 7777);
             ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(
                        socket.getOutputStream()))) {
            Message message = new Message(car.getCurrentTrafficPost().getSystemId(),
                    car.getDriver().getSystemId(),
                    car.getStatus(),
                    car.getCurrentRoad() != null ? car.getCurrentRoad().getSystemId() : null,
                    Instant.now().toEpochMilli());

            output.writeObject(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        LOGGER.debug("setting roads ...: {}", roads);
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
