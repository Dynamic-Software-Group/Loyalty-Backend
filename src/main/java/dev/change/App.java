package dev.change;

import dev.change.services.internal.events.FlagSubscriber;

public class App {
    public static void main(String[] args) {
        FlagSubscriber.subscribeAll();
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(
                new Thread(App::syncDatabases)
        );
    }

    private static void syncDatabases() {

    }
}
