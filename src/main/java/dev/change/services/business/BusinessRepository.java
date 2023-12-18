package dev.change.services.business;

import dev.change.beans.Business;
import dev.change.services.data.RedisRepository;

public interface BusinessRepository extends RedisRepository<Business, String> {
}
