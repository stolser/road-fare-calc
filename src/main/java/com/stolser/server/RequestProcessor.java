package com.stolser.server;

import com.stolser.entity.*;
import com.stolser.repository.RoadRepository;
import com.stolser.repository.TrafficPostRepository;
import com.stolser.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Configurable(autowire = Autowire.BY_TYPE, preConstruction = true, dependencyCheck = true)
public class RequestProcessor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestProcessor.class);
    private static final Marker carStatusUpdate = MarkerFactory.getMarker("carStatusUpdate");
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoadRepository roadRepo;
    @Autowired
    private TrafficPostRepository trafficPostRepo;
    private Socket socket;

    public RequestProcessor(Socket socket) {
        this.socket = socket;
//        System.out.println("userRepo: " + userRepo);
//        System.out.println("roadRepo: " + roadRepo);
//        System.out.println("trafficPostRepo: " + trafficPostRepo);
    }

    @Override
    public void run() {
        Message message;
        try (ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(
                socket.getInputStream()))) {

            message = (Message) input.readObject();
            processMessage(message);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processMessage(Message message) {
        TrafficPost trafficPost = trafficPostRepo.findBySystemId(message.getTpSystemId());
        User driver = userRepo.findBySystemId(message.getUserId());
        UserTrackerStatus status = message.getStatus();
        Road road = roadRepo.findBySystemId(message.getRoadSystemId());
        Instant timestamp = Instant.ofEpochMilli(message.getTimestamp());

        LOGGER.debug(carStatusUpdate, String.format("Server. TP: %s:\n\tuser: %s;\n\tstatus: %s;\n\troad: %s;\n" +
                "\tdate: %s\n--------------------------------\n",
                trafficPost, driver, status, road,
                new SimpleDateFormat("dd.MM HH:mm:ss").format(new Date(message.getTimestamp()))));

    }
}
