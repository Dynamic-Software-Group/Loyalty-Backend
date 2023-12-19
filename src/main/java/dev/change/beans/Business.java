package dev.change.beans;

import dev.change.services.data.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@AllArgsConstructor
@Getter
@Setter
public class Business implements Identifiable<String> {
    private String id;
    private String ownerId;
    private String name;
    private String description;
    private double latitudeCorner1;
    private double longitudeCorner1;
    private double latitudeCorner2;
    private double longitudeCorner2;
    private String address;
    private String niche;
    private URL logo;
    private URL website;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String s) {
        this.id = s;
    }
}
