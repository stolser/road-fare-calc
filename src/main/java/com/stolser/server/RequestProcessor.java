package com.stolser.server;

import static com.google.common.base.Preconditions.*;

import com.stolser.entity.*;
import com.stolser.repository.RoadRepository;
import com.stolser.repository.TrafficPostRepository;
import com.stolser.repository.UserRepository;
import com.stolser.repository.UserTrackerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    @Autowired
    private UserTrackerRepository userTrackerRepo;
    @Autowired
    private MailSender mailSender;
    private Socket socket;

    public RequestProcessor(Socket socket) {
        this.socket = socket;
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
        UserTrackerStatus status = message.getStatus();
        TrafficPost trafficPost = trafficPostRepo.findBySystemId(message.getTpSystemId());
        User driver = userRepo.findBySystemId(message.getUserId());
        Road road = roadRepo.findBySystemId(message.getRoadSystemId());
        Long timestamp = message.getTimestamp();

        logInfo(status, trafficPost, driver, road, timestamp);

        UserTracker driverActiveTracker = userTrackerRepo.findByUserAndStatusIsNot(driver,
                UserTrackerStatus.LEFT_AUTOBAHN);

        if (driverActiveTracker != null) {
            switch (status) {
                case AT_TRAFFIC_POST:
                    driverActiveTracker.addNewTrafficPost(checkNotNull(trafficPost), timestamp);
                    break;
                case ON_THE_ROAD:
                    driverActiveTracker.addNewRoad(checkNotNull(road));
                    break;
                case LEFT_AUTOBAHN:
                    break;
                default:
                    throw new IllegalStateException("Wrong userTrackerStatus.");
            }

            driverActiveTracker.setStatus(status);

            if (status == UserTrackerStatus.LEFT_AUTOBAHN) {
//                sendUserInfoAboutJourney(driverActiveTracker);
// todo: uncomment this + set username/password in the system properties to send emails;
                driverActiveTracker.setSummaryReport(generateJourneySummaryText(driverActiveTracker));
            }

            userTrackerRepo.save(driverActiveTracker);

        } else {
            UserTracker newUserTracker = new UserTracker(driver);
            newUserTracker.setStatus(status);
            newUserTracker.addNewTrafficPost(trafficPost, timestamp);

            userTrackerRepo.save(newUserTracker);
        }
    }

    private void sendUserInfoAboutJourney(UserTracker tracker) {
        String messageText = generateJourneySummaryText(tracker);
        String userEmail = tracker.getUser().getEmail();

        mailSender.sendEmailTo(userEmail, messageText);
    }

    private String generateJourneySummaryText(UserTracker tracker) {
        checkArgument(tracker.getStatus() == UserTrackerStatus.LEFT_AUTOBAHN, "Status MUST be LEFT_AUTOBAHN.");
        StringBuilder builder = new StringBuilder();
        User user = tracker.getUser();
        double userFare = calculateUserFare(tracker.getRoads());
        Map<Long, TrafficPost> postDateMapper = tracker.getTrafficPosts();
        Set<Long> dates = new TreeSet<>(postDateMapper.keySet());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm:ss");
        AtomicInteger index = new AtomicInteger(1);

        builder.append("<h2>Summary about journey along Autobahn</h2>");
        builder.append(String.format("<p>User: %s %s</p>", user.getFirstName(), user.getLastName()));
        builder.append(String.format("<p>Total fare: $%.2f</p>", userFare));
        builder.append("<p>The route of the journey:</p>");
        builder.append("<div><table><tr><th>#</th><th>TrafficPost</th><th>Date</th></tr>");
        dates.stream().forEach(date -> {
            builder.append(String.format("<tr><td>%d</td><td>%s</td><td>%s</td></tr>",
                    index.getAndIncrement(),
                    postDateMapper.get(date).getName(),
                    dateFormat.format(new Date(date))));
        });
        builder.append("</table></div>");

        return builder.toString();
    }

    private double calculateUserFare(List<Road> roads) {

        return roads.stream().mapToDouble(Road::getFare).sum();
    }

    private void logInfo(UserTrackerStatus status, TrafficPost trafficPost, User driver, Road road, Long timestamp) {
        LOGGER.debug(carStatusUpdate, String.format("Server. TP: %s:\n\tuser: %s;\n\tstatus: %s;\n\troad: %s;\n" +
                "\tdate: %s",
                trafficPost, driver, status, road,
                new SimpleDateFormat("dd.MM HH:mm:ss").format(new Date(timestamp))));
        LOGGER.trace("userTrackers.size(): {}", userTrackerRepo.findAll().size());
        LOGGER.trace("Active users (status != LEFT_AUTOBAHN): {}",
                userTrackerRepo.findByStatusIsNot(UserTrackerStatus.LEFT_AUTOBAHN).size());
        LOGGER.trace("--------------------------------");
    }
}
