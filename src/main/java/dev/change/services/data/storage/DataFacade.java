package dev.change.services.data.storage;

import org.json.JSONObject;

public interface DataFacade<T> {
    T getData(PreparedQuery query);
}
