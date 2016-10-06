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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;

public class ServerRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRunner.class);
    private MongoTemplate template;
    private UserRepository userRepo;
    private RoadRepository roadRepo;
    private TrafficPostRepository trafficPostRepo;
    private UserTrackerRepository userTrackerRepo;
    private ExecutorService threadPool;

    private List<User> usersPopulateDb;
    private List<TrafficPost> postsPopulateDb;
    private List<Road> roadsPopulateDb;

    private List<UserTracker> trackers;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ServerMainConfig.class);
        ServerRunner runner = context.getBean("serverRunner", ServerRunner.class);
        displayBeans(context);

//!!!!!!!!!!        runner.setupDatabase(); use only once, then MUST be comment!!!!!!!!!!!!
        runner.displayDataFromDb();
        runner.startServer();

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

    @Autowired
    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
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

        System.out.println("UserTrackers from the DB:");
        userTrackerRepo.findAll().stream().forEach(System.out::println);
    }

    private void startServer() {
        try(ServerSocket server = new ServerSocket(7777)) {
            while (true) {
                LOGGER.debug("Server listening...");
                Socket client = server.accept();
                LOGGER.debug("... a new request's been accepted.");
                RequestProcessor processor = new RequestProcessor(client);
                threadPool.submit(processor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayBeans(ApplicationContext context) {
        System.out.printf("There are %d beans in the Server context.\n", context.getBeanDefinitionCount());
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
    }

}
