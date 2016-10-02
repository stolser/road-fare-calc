package com.stolser.server;

import com.stolser.server.config.ServerMainConfig;
import com.stolser.entity.*;
import com.stolser.repository.RoadRepository;
import com.stolser.repository.TrafficPostRepository;
import com.stolser.repository.UserRepository;
import com.stolser.repository.UserTrackerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

public class ServerRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRunner.class);
    private MongoTemplate template;
    private UserRepository userRepo;
    private RoadRepository roadRepo;
    private TrafficPostRepository trafficPostRepo;
    private UserTrackerRepository userTrackerRepo;

    private List<User> usersPopulateDb;
    private List<TrafficPost> postsPopulateDb;
    private List<Road> roadsPopulateDb;

    private List<UserTracker> trackers;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ServerMainConfig.class);
        ServerRunner runner = context.getBean("serverRunner", ServerRunner.class);
        System.out.printf("There are %d beans in the Server context.\n", context.getBeanDefinitionCount());
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);

        runner.setupDatabase();
        runner.displayDataFromDb();


    }

    @Autowired
    public ServerRunner(MongoTemplate template, UserRepository userRepo, RoadRepository roadRepo,
                        TrafficPostRepository trafficPostRepo, UserTrackerRepository userTrackerRepo) {
        this.template = template;
        this.userRepo = userRepo;
        this.roadRepo = roadRepo;
        this.trafficPostRepo = trafficPostRepo;
        this.userTrackerRepo = userTrackerRepo;
        this.trackers = new ArrayList<>();
    }

    @Autowired
    public void setUsersPopulateDb(List<User> usersPopulateDb) {
        this.usersPopulateDb = usersPopulateDb;
    }

    @Autowired
    public void setRoadsPopulateDb(List<Road> roadsPopulateDb) {
        this.roadsPopulateDb = roadsPopulateDb;
    }

    @Autowired
    public void setPostsPopulateDb(List<TrafficPost> postsPopulateDb) {
        this.postsPopulateDb = postsPopulateDb;
    }

    private void setupDatabase() {
        template.dropCollection(User.class);
        template.dropCollection(Road.class);
        template.dropCollection(TrafficPost.class);
        template.dropCollection(UserTracker.class);

        userRepo.insert(usersPopulateDb);
        roadRepo.insert(roadsPopulateDb);
        trafficPostRepo.insert(postsPopulateDb);

    }

    private void displayDataFromDb() {
        System.out.println("Users from the DB:");
        userRepo.findAll().stream().forEach(System.out::println);

        System.out.println("TrafficPosts from the DB:");
        trafficPostRepo.findAll().stream().forEach(System.out::println);

        System.out.println("Roads from the DB:");
        roadRepo.findAll().stream().forEach(System.out::println);
    }

}
