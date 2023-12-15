package dev.change.services.data.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.change.services.data.DBSerializable;
import dev.change.services.data.storage.DataFacade;
import dev.change.services.internal.events.ConfigCat;
import org.json.JSONObject;

public class DataFacadeImpl<T extends DBSerializable> implements DataFacade<T> {
    private boolean redis = ConfigCat.getClient().getValue(Boolean.class, "redisEnabled", true);
    private final ObjectMapper mapper = new ObjectMapper();

    // jsonobject
    @Override
    public T getData(String key) {
        return mapper.readValue()
    }

    private
}
