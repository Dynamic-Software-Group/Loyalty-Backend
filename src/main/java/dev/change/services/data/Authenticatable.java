package dev.change.services.data;

import java.util.List;

public interface Authenticatable<ID> {
    public String getJwt();
    public String genJwt(String email, List<String> authorities);
    public void setJwt(String jwt);
}