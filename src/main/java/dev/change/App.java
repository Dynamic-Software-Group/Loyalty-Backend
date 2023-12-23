package dev.change;

import dev.change.services.authentication.RedisUserService;
import dev.change.services.internal.events.FlagSubscriber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"dev.change"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        System.out.println("Application Booting...");
        FlagSubscriber.subscribeAll();
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(
                new Thread(() -> {
                    RedisUserService.logoutAll();
                    App.syncDatabases();
                })
        );
    }

    private static void syncDatabases() {

    }
}
