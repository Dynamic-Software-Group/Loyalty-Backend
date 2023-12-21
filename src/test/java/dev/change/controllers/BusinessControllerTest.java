package dev.change.controllers;

import dev.change.services.authentication.UserRepository;
import dev.change.services.business.BusinessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BusinessControllerTest {
    @InjectMocks
    BusinessController businessController;

    @Mock
    UserRepository userRepository;

    @Mock
    BusinessRepository businessRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBusiness() {

    }

}
