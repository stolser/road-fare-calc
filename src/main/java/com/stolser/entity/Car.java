package com.stolser.entity;

import com.stolser.repository.RoadRepository;
import com.stolser.repository.TrafficPostRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Car {
    private String plate;
    private User driver;
    private List<TrafficPost> journeyRoute;
    private int nextTrafficPostIndex;

    private UserTrackerStatus status;
    private boolean isOnTheWay;
    private TrafficPost currentTrafficPost;
    private Road currentRoad;

    TrafficPostRepository postRepo;
    RoadRepository roadRepo;

    public Car(String plate, User driver, List<TrafficPost> journeyRoute) {
        this.plate = plate; // todo: check for uniqueness
        this.driver = driver;
        this.journeyRoute = journeyRoute;
    }

    @Autowired
    public void setPostRepo(TrafficPostRepository postRepo) {
        this.postRepo = postRepo;
    }

    @Autowired
    public void setRoadRepo(RoadRepository roadRepo) {
        this.roadRepo = roadRepo;
    }

    public void startJourney() {
        if (isOnTheWay) throw new IllegalStateException("This car is already on the road.");

        TrafficPost nextTP;
        Road nextRoad;


//        chooseStartingTrafficPost().register(this);
//        while (isOnTheWay) {
//            nextTP = chooseNextTrafficPost();
//
//            if (nextTP == null) {
//                leaveAutobahn();
//            } else {
//                nextRoad = chooseNextRoad(nextTP);
//                driveAlongNextRoad(nextRoad);
//                nextTP.register(this);
//            }
//        }

    }

    private TrafficPost chooseStartingTrafficPost() {

        return null;
    }

    public String getPlate() {
        return plate;
    }

    public User getDriver() {
        return driver;
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
