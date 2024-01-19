package dev.change.services.data;

public interface Identifiable<ID> {
    ID getId();
    void setId(ID id);
}
