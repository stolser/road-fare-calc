package com.stolser.entity;

import java.util.List;

public class Car {
    private String plate;
    private User driver;
    private List<TrafficPost> journeyMap;

    public Car(String plate, User driver, List<TrafficPost> journeyMap) {
        this.plate = plate; // todo: check for uniqueness
        this.driver = driver;
        this.journeyMap = journeyMap;
    }

    public String getPlate() {
        return plate;
    }

    public User getDriver() {
        return driver;
    }

    public List<TrafficPost> getJourneyMap() {
        return journeyMap;
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
