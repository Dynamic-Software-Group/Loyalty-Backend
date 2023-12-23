package dev.change.beans;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.change.services.data.Identifiable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User implements Identifiable<String> {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    @ElementCollection
    private List<String> authorities;
    private String jwt;
    @ElementCollection
    @MapKeyColumn(name = "businessId")
    private Map<String, Integer> points; // Map<BusinessId, Points>
    private double latitude; // Get from frontend (may not be accurate)
    private double longitude; // ""
    private String firstName;
    private String lastName;
    private UpgradeLevel upgradeLevel;
    private URL profilePicture;

    public enum UpgradeLevel {
        BRONZE, SILVER, GOLD, PLATINUM
    }

    public boolean hasAuthority(String authority) {
        return authorities.contains(authority);
    }

    public void addAuthority(String authority) {
        authorities.add(authority);
    }

    public void removeAuthority(String authority) {
        authorities.remove(authority);
    }

    public void addPoints(String businessId, int points) {
        if (this.points == null) {
            this.points = new HashMap<>();
        }
        this.points.put(businessId, getPoints(businessId) + points);
    }

    public void removePoints(String businessId, int points) {
        if (this.points == null) {
            this.points = new HashMap<>();
        }
        this.points.put(businessId, this.points.get(businessId) - points);
    }

    public void setPoints(String businessId, int points) {
        if (this.points == null) {
            this.points = new HashMap<>();
        }
        this.points.put(businessId, points);
        System.out.println(this.points);
    }

    public int getPoints(String businessId) {
        if (this.points == null) {
            this.points = new HashMap<>();
        }
        if (!this.points.containsKey(businessId)) {
            return 0;
        }
        return this.points.get(businessId);
    }

    public boolean hasUpgradeLevel(UpgradeLevel upgradeLevel) {
        return this.upgradeLevel == upgradeLevel;
    }


    @Override
    public String getId() {
        return id;
    }
}
