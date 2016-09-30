package com.stolser;

import static org.springframework.data.mongodb.core.query.Criteria.*;

import com.stolser.entity.Car;
import com.stolser.entity.TrafficPost;
import com.stolser.entity.User;
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

    @Autowired
    private MongoTemplate template;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("mainAppConfig.xml");
        AppRunner app = context.getBean("runner", AppRunner.class);
        System.out.printf("There are %d beans in the context.\n", context.getBeanDefinitionCount());
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);

        app.setupDatabase();
        app.updateTrafficPostCollection();
        app.updateUsersCollection();
        app.createVehicles();
    }

    private void updateTrafficPostCollection() {

    }

    private void setupDatabase() {
        template.dropCollection(User.class);

        Index userSystemIdIndex = new Index("systemId", Sort.Direction.ASC).unique();
        template.indexOps(User.class).ensureIndex(userSystemIdIndex);

    }

    private void updateUsersCollection() {
        User user1 = new User("Oleg", "Stoliarov", EMAIL);
        User user2 = new User("Anton", "Koshel", EMAIL);
        User user3 = new User("Bill", "Gates", EMAIL);
        User user4 = new User("Joshua", "Bloch", EMAIL);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        template.insertAll(users);
        System.out.println("users: ");
        template.find(new Query(), User.class).stream().forEach(System.out::println);

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
