package dev.change.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business")
public class BusinessController {

    @PostMapping("/create")
    public ResponseEntity<?> createBusiness(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt, @RequestBody String name, @RequestBody String description, @RequestBody String userId) {
        // get headers
        

    }
}
