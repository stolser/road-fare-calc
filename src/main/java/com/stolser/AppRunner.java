package com.stolser;

import static org.springframework.data.mongodb.core.query.Criteria.*;

import com.stolser.entity.*;
import com.stolser.repository.RoadRepository;
import com.stolser.repository.TrafficPostRepository;
import com.stolser.repository.UserRepository;
import com.stolser.repository.UserTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

public class AppRunner {
    private static final String EMAIL = "stolser@ukr.net";

    private MongoTemplate template;
    private UserRepository userRepo;
    private RoadRepository roadRepo;
    private TrafficPostRepository trafficPostRepo;
    private UserTrackerRepository userTrackerRepo;

    private List<User> usersPopulateDb;
    private List<TrafficPost> postsPopulateDb;
    private List<Road> roadsPopulateDb;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config/mainAppConfig.xml");
        AppRunner app = context.getBean("runner", AppRunner.class);
        System.out.printf("There are %d beans in the context.\n", context.getBeanDefinitionCount());
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);

        app.setupDatabase();
//        app.displayDataFromDb();


//        app.updateTrafficPostCollection();
//        app.createVehicles();
    }

    @Autowired
    public AppRunner(MongoTemplate template, UserRepository userRepo, RoadRepository roadRepo,
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

    private void updateTrafficPostCollection() {

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

    private void createVehicles() {
        List<TrafficPost> journeyMap = new ArrayList<>();
        User user1 = template.findOne(new Query(where("firstName").is("Oleg")), User.class);
        User user2 = template.findOne(new Query(where("firstName").is("Anton")), User.class);
        User user3 = template.findOne(new Query(where("firstName").is("Bill")), User.class);
        User user4 = template.findOne(new Query(where("firstName").is("Joshua")), User.class);

        Car car1 = new Car("AO777PP", user1, journeyMap);
        Car car2 = new Car("XZ-111", user2, journeyMap);
        Car car3 = new Car("18Wheeler", user3, journeyMap);
        Car car4 = new Car("School-bus", user4, journeyMap);
        Set<Car> cars = new HashSet<>();
        cars.add(car1);
        cars.add(car2);
        cars.add(car3);
        cars.add(car4);

        System.out.println("cars: ");
        cars.stream().forEach(System.out::println);

    }
}
