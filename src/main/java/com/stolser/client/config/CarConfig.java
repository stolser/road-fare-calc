package com.stolser.client.config;

import com.stolser.entity.Car;
import com.stolser.entity.TrafficPost;
import com.stolser.entity.User;
import com.stolser.repository.TrafficPostRepository;
import com.stolser.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.*;

@Configuration
public class CarConfig {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TrafficPostRepository postRepo;

    @Bean(name = {"cars"})
    @Lazy
    public List<Car> getCarsForAutobahn() {
        List<Car> cars = new ArrayList<>();
        List<User> users = userRepo.findAll();

        for(User user: users) {
            String plate = newPlate();
            List<TrafficPost> journeyMap = newJourneyMap();
            Car newCar = new Car(plate, user, journeyMap);
            cars.add(newCar);
        }

        return cars;
    }

    @Bean
    @Scope("prototype")
    public String newPlate() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    @Bean
    @Scope("prototype")
    @Lazy
    public List<TrafficPost> newJourneyMap() {
        List<List<TrafficPost>> journeyRoutes = new ArrayList<>();
        List<TrafficPost> posts = postRepo.findAll();
        Map<String, TrafficPost> idPostMapper = new HashMap<>();
        posts.forEach(post -> idPostMapper.put(post.getSystemId(), post));

        List<TrafficPost> journeyRoute1 = new ArrayList<>();
        journeyRoute1.add(idPostMapper.get("tr-post-1"));
        journeyRoute1.add(idPostMapper.get("tr-post-3"));
        journeyRoute1.add(idPostMapper.get("tr-post-6"));
        journeyRoute1.add(idPostMapper.get("tr-post-9"));

        List<TrafficPost> journeyRoute2 = new ArrayList<>();
        journeyRoute2.add(idPostMapper.get("tr-post-1"));
        journeyRoute2.add(idPostMapper.get("tr-post-2"));
        journeyRoute2.add(idPostMapper.get("tr-post-5"));
        journeyRoute2.add(idPostMapper.get("tr-post-7"));
        journeyRoute2.add(idPostMapper.get("tr-post-8"));
        journeyRoute2.add(idPostMapper.get("tr-post-10"));

        List<TrafficPost> journeyRoute3 = new ArrayList<>();
        journeyRoute3.add(idPostMapper.get("tr-post-10"));
        journeyRoute3.add(idPostMapper.get("tr-post-8"));
        journeyRoute3.add(idPostMapper.get("tr-post-4"));
        journeyRoute3.add(idPostMapper.get("tr-post-2"));
        journeyRoute3.add(idPostMapper.get("tr-post-1"));
        journeyRoute3.add(idPostMapper.get("tr-post-3"));
        journeyRoute3.add(idPostMapper.get("tr-post-6"));
        journeyRoute3.add(idPostMapper.get("tr-post-9"));

        List<TrafficPost> journeyRoute4 = new ArrayList<>();
        journeyRoute4.add(idPostMapper.get("tr-post-7"));
        journeyRoute4.add(idPostMapper.get("tr-post-5"));
        journeyRoute4.add(idPostMapper.get("tr-post-2"));
        journeyRoute4.add(idPostMapper.get("tr-post-1"));

        List<TrafficPost> journeyRoute5 = new ArrayList<>();
        journeyRoute5.add(idPostMapper.get("tr-post-2"));
        journeyRoute5.add(idPostMapper.get("tr-post-5"));
        journeyRoute5.add(idPostMapper.get("tr-post-2"));
        journeyRoute5.add(idPostMapper.get("tr-post-4"));
        journeyRoute5.add(idPostMapper.get("tr-post-8"));
        journeyRoute5.add(idPostMapper.get("tr-post-7"));
        journeyRoute5.add(idPostMapper.get("tr-post-5"));
        journeyRoute5.add(idPostMapper.get("tr-post-2"));

        journeyRoutes.add(journeyRoute1);
        journeyRoutes.add(journeyRoute2);
        journeyRoutes.add(journeyRoute3);
        journeyRoutes.add(journeyRoute4);
        journeyRoutes.add(journeyRoute5);

        return getRandomJourneyMap(journeyRoutes);
    }

    private List<TrafficPost> getRandomJourneyMap(List<List<TrafficPost>> journeyMaps) {
        return journeyMaps.get(new Random().nextInt(journeyMaps.size()));
    }
}
