package dev.change.beans;

import dev.change.services.data.Identifiable;

public class Business implements Identifiable<String> {
    private String id;
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String s) {
        this.id = s;
    }
}
