package dev.change.controllers;

import dev.change.beans.Business;
import dev.change.beans.User;
import dev.change.services.authentication.UserRepository;
import dev.change.services.business.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business")
public class BusinessController {
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;

    @Autowired
    public BusinessController(UserRepository userRepository, BusinessRepository businessRepository) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBusiness(@RequestHeader String api, @RequestBody Business business, @RequestHeader String jwt) {
        if (SecretHandler.notValid(api)) {
            return ResponseEntity.badRequest().body("Invalid API key");
        }
        if (!userRepository.verifyJwt(jwt)) {
            return ResponseEntity.badRequest().body("Invalid JWT");
        }
        User user = userRepository.decodeJwt(jwt);
        business.setOwnerId(user.getId());
        business.setId(SecretHandler.genId());
        businessRepository.save(business);
        return null;
    }
}
