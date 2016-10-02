package com.stolser;

import com.stolser.configuration.ServerMainConfig;
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
import org.springframework.context.annotation.Lazy;
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

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ServerMainConfig.class);
        ServerRunner runner = context.getBean("runner", ServerRunner.class);
        System.out.printf("There are %d beans in the Server context.\n", context.getBeanDefinitionCount());
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);

        runner.setupDatabase();

//        app.displayDataFromDb();


    }

    @Autowired
    public ServerRunner(MongoTemplate template, UserRepository userRepo, RoadRepository roadRepo,
                        TrafficPostRepository trafficPostRepo, UserTrackerRepository userTrackerRepo) {
        this.template = template;
        this.userRepo = userRepo;
        this.roadRepo = roadRepo;
        this.trafficPostRepo = trafficPostRepo;
        this.userTrackerRepo = userTrackerRepo;
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

        System.out.println("Users into the DB:");
        usersPopulateDb.stream().forEach(System.out::println);
        userRepo.insert(usersPopulateDb);

        System.out.println("Roads into the DB:");
        roadsPopulateDb.stream().forEach(System.out::println);
        roadRepo.insert(roadsPopulateDb);

        System.out.println("TrafficPosts into the DB:");
        postsPopulateDb.stream().forEach(System.out::println);
        trafficPostRepo.insert(postsPopulateDb);

    }

    private void displayDataFromDb() {
        System.out.println("Users from the DB:");
        userRepo.findAll().stream().forEach(System.out::println);
    }

}
