package xyz.therahul.creatorstore.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.therahul.creatorstore.dto.HealthCheckResponse;

@RestController
@RequestMapping("/")
public class HealthCheck {
    @GetMapping
    public HealthCheckResponse healthCheck() {
        return new HealthCheckResponse("Success", "Server is up and running");
    }
}
