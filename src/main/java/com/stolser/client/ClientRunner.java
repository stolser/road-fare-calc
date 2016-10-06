package com.stolser.client;

import com.stolser.client.config.ClientMainConfig;
import com.stolser.entity.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
public class ClientRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRunner.class);
    private List<Car> cars;
    @Autowired
    private ExecutorService threadPool;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ClientMainConfig.class);
        ClientRunner runner = context.getBean("clientRunner", ClientRunner.class);
        displayBeans(context);

        runner.makeCarsHitTheRoad();
    }

    private void makeCarsHitTheRoad() {
        displayCars();
        cars.forEach(car -> threadPool.submit(car::startJourney));
    }

    @Autowired
    @Lazy
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    private void displayCars() {
        System.out.println("Cars on the start:");
        cars.stream().forEach(car -> {
            System.out.printf("car: %s\njourneyMap: ", car);
            car.getJourneyRoute().stream().forEach(journey -> System.out.printf("\t%s\n", journey));
            System.out.println("--------------------------------");
        });
    }

    private static void displayBeans(ApplicationContext context) {
        System.out.printf("There are %d beans in the Client context:\n", context.getBeanDefinitionCount());
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
    }
}
