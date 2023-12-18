package dev.change.services.data.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PreparedQuery {
    private final Type type;
    private final String id;

    private enum Type {
        USER,
        BUSINESS
    }

    public String toRedis() {
        return type.name() + ":" + id;
    }

    public String toSql() {
        return "SELECT * FROM " + type.name() + " WHERE id = " + id;
    }
}
