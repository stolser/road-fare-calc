package com.stolser.entity;

import com.stolser.repository.RoadRepository;
import com.stolser.repository.TrafficPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Car {
    private static final Logger LOGGER = LoggerFactory.getLogger(Car.class);
    private String plate;
    private User driver;
    private List<TrafficPost> journeyRoute;

    private int nextPostIndex;
    private UserTrackerStatus status;
    private boolean isOnTheWay;
    private TrafficPost currentTrafficPost;
    private TrafficPost nextTrafficPost;
    private Road currentRoad;

    TrafficPostRepository postRepo;
    RoadRepository roadRepo;

    public Car(String plate, User driver, List<TrafficPost> journeyRoute) {
        this.plate = plate; // todo: check for uniqueness
        this.driver = driver;
        this.journeyRoute = journeyRoute;
    }

    public void setPostRepo(TrafficPostRepository postRepo) {
        this.postRepo = postRepo;
    }

    public void setRoadRepo(RoadRepository roadRepo) {
        this.roadRepo = roadRepo;
    }

    public void startJourney() {
        if (isOnTheWay) throw new IllegalStateException("This car is already on the road.");

        chooseAndArriveAtStartingTrafficPost();

        while (isOnTheWay) {
            chooseNextTrafficPostAndRoad();

            if (nextTrafficPost == null) {
                leaveAutobahn();
            } else {
                driveAlongNextRoad();
                arriveAndRegisterAtNextTrafficPost();
            }
        }

        LOGGER.debug("\tCar {} left the autobahn!", this);

    }

    private void chooseAndArriveAtStartingTrafficPost() {
        isOnTheWay = true;
        status = UserTrackerStatus.AT_TRAFFIC_POST;
        currentTrafficPost = journeyRoute.get(nextPostIndex);
        nextPostIndex++;

        currentTrafficPost.register(this);
    }

    private void chooseNextTrafficPostAndRoad() {
        if (journeyIsOver()) {
            nextTrafficPost = null;
        } else {
            nextTrafficPost = journeyRoute.get(nextPostIndex);
            nextPostIndex++;
            List<Road> possibleRoads = roadRepo.findByTwoPostSystemIds(nextTrafficPost.getSystemId(),
                    currentTrafficPost.getSystemId());
            LOGGER.trace("possibleRoads: " + possibleRoads);
            currentRoad = possibleRoads.get(0);

            LOGGER.debug("car: {}; nextTrafficPost: {}; currentRoad: {}",
                    this.plate, nextTrafficPost, currentRoad);
        }
    }

    private boolean journeyIsOver() {
        return nextPostIndex >= journeyRoute.size();
    }

    private void leaveAutobahn() {
        nextPostIndex = 0;
        status = UserTrackerStatus.LEFT_AUTOBAHN;
        isOnTheWay = false;
        nextTrafficPost = null;
        currentRoad = null;

        currentTrafficPost.register(this);
        currentTrafficPost = null;
    }

    private void driveAlongNextRoad() {
        status = UserTrackerStatus.ON_THE_ROAD;
        currentTrafficPost.register(this);

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis((long) currentRoad.getLength()/100));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void arriveAndRegisterAtNextTrafficPost() {
        status = UserTrackerStatus.AT_TRAFFIC_POST;
        currentRoad = null;
        currentTrafficPost = nextTrafficPost;
        currentTrafficPost.register(this);
    }

    public String getPlate() {
        return plate;
    }

    public User getDriver() {
        return driver;
    }

    public UserTrackerStatus getStatus() {
        return status;
    }

    public Road getCurrentRoad() {
        return currentRoad;
    }

    public List<TrafficPost> getJourneyRoute() {
        return journeyRoute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;

        Car car = (Car) o;

        return plate != null ? plate.equals(car.plate) : car.plate == null;
    }

    @Override
    public int hashCode() {
        return plate != null ? plate.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("Car{plate: '%s', driver: %s}", plate, driver);
    }
}
