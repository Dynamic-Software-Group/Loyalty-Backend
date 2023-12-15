package dev.change.services.data;

import java.util.List;

public interface Authenticate<ID> {
    String getJwt();
    String genJwt(String email, List<String> authorities);
    void setJwt(String jwt);
}