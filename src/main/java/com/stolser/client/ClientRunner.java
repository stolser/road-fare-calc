package com.stolser.client;

import com.stolser.client.config.ClientMainConfig;
import com.stolser.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ClientRunner {
    private List<Car> cars;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ClientMainConfig.class);
        ClientRunner runner = context.getBean("clientRunner", ClientRunner.class);
        System.out.printf("There are %d beans in the Client context:\n", context.getBeanDefinitionCount());
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);

        runner.makeCarsHitTheRoad();
    }

    private void makeCarsHitTheRoad() {
        System.out.println("Cars on the start:");
        cars.stream().forEach(car -> {
            System.out.println("car: " + car);
            System.out.println("journeyMap: ");
            car.getJourneyRoute().stream().forEach(journey -> System.out.printf("\t%s\n", journey));
            System.out.println("----------------");
        });

        cars.forEach(car -> {
            new Thread() {
                public void run() {
                    car.startJourney();
                }
            }.start();
        });
    }

    @Autowired
    @Lazy
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}
