package dev.change.services.internal.events;

import com.configcat.ConfigCatClient;

import java.lang.reflect.Method;
import java.util.ServiceLoader;

public class FlagSubscriber {
    private static final ConfigCatClient client = ConfigCatClient.get("E2LbCH4TcEqLbgMN0dzSqA/ZvTH_wBRB0yaUszCMQQuWg");

    public static void subscribeAll() {
        ServiceLoader.load(Object.class).forEach(FlagSubscriber::subscribe);
    }

    public static void subscribe(Object subscriber) {
        for (Method method : subscriber.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Subscribe annotation = method.getAnnotation(Subscribe.class);
                String flag = annotation.flag();
                FireWhen fireWhen = annotation.fireWhen();
                if ((fireWhen == FireWhen.ENABLED && isFeatureFlagEnabled(flag)) ||
                        (fireWhen == FireWhen.DISABLED && !isFeatureFlagEnabled(flag)) ||
                        (fireWhen == FireWhen.BOTH)) {
                    try {
                        method.invoke(subscriber);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private static boolean isFeatureFlagEnabled(String featureFlag) {
        return client.getValue(Boolean.class, featureFlag, false);
    }

    
}
