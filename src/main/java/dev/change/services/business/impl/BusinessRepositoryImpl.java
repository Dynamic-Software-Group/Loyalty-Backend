package dev.change.services.business.impl;

import dev.change.beans.Business;
import dev.change.services.business.BusinessRepository;
import dev.change.services.data.impl.RedisRepositoryImpl;

public class BusinessRepositoryImpl extends RedisRepositoryImpl<Business, String> implements BusinessRepository {

    @Override
    public void registerBusiness(Business business) {

    }
}
