package dev.change.controllers;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class SecretHandler {
    private static @NotNull String getApi() {
        String secret = null;
        try {
//            Process process = Runtime.getRuntime().exec("doppler secrets get API_KEY --plain");
//            BufferedReader reader = new BufferedReader(
//                    new java.io.InputStreamReader(process.getInputStream())
//            );
//            secret = reader.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        return secret;
        return "test";
    }

    public static boolean notValid(@NotNull String api) {
        return !api.equals(getApi());
    }

    public static String genId() {
        return java.util.UUID.randomUUID().toString();
    }
}
