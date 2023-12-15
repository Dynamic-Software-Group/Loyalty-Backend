package dev.change.services.internal.events;

import com.configcat.ConfigCatClient;
import lombok.Getter;

public class ConfigCat {

    @Getter
    private static final ConfigCatClient client = ConfigCatClient.get("E2LbCH4TcEqLbgMN0dzSqA/ZvTH_wBRB0yaUszCMQQuWg");

}
