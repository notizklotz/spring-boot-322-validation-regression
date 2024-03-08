package com.example.demo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@SpringBootApplication
@RestController
public class DemoApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(DemoApplication.class, args);
    }

    @PostMapping("/api/{mypath}/with-constraint")
    public void withConstraintOnPathVariable(
            @PathVariable @Pattern(regexp =  "[a-z0-9-]+") String mypath,
            @RequestBody @Valid RequestData mydata
    ) {

    }

    @PostMapping("/api/{mypath}/without-constraint")
    public void withoutConstraintOnPathVariable(
            @PathVariable String mypath,
            @RequestBody @Valid RequestData mydata
    ) {

    }

    public record RequestData(@NotBlank String propertyA) {
    }

}
