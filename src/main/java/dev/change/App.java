package dev.change;

import dev.change.services.internal.events.FlagSubscriber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        System.out.println("Application Booting...");
        FlagSubscriber.subscribeAll();
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(
                new Thread(App::syncDatabases)
        );
    }

    private static void syncDatabases() {

    }
}
