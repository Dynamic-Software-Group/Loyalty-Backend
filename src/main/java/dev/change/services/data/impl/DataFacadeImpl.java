package dev.change.services.data.impl;

import java.lang.reflect.Field;
import java.io.IOException;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.change.services.data.storage.DataFacade;
import dev.change.services.internal.events.ConfigCat;

public class DataFacadeImpl<T> implements DataFacade<T> {
    private boolean redis = ConfigCat.getClient().getValue(Boolean.class, "redisEnabled", true);
    private final ObjectMapper mapper = new ObjectMapper();
    private final Class<T> clazz;

    public DataFacadeImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    // jsonobject
    @Override
    public T getData(String key) {
        try {
            return mapper.readValue(jsonData(key), clazz);
        } catch (Exception e) {
            return null;
        }
    }

    private String jsonData(String key) {
        //TODO: read from datatbase depending on current primary
        return null;
    }
}   
