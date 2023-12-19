package dev.change;

import dev.change.services.data.impl.DataFacadeImpl;
import dev.change.services.internal.events.FlagSubscriber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class App {
    private static final DataFacadeImpl<String> dataFacade = new DataFacadeImpl<>(String.class);
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        System.out.println("Application Booting...");
        FlagSubscriber.subscribeAll();
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(
                new Thread(App::syncDatabases)
        );

        new Timer().scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        dataFacade.checkDatabases();
                    }
                },
                0,
                1000 * 60 // 1 minute
        );
    }

    private static void syncDatabases() {

    }
}
