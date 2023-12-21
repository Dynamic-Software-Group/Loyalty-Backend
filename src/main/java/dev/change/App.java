package dev.change;

import dev.change.services.internal.events.FlagSubscriber;
import io.sentry.Sentry;

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
                new Thread(App::syncDatabases)
        );

        // Sentry testing
        try {
            throw new Exception("Test");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }

    private static void syncDatabases() {

    }
}
