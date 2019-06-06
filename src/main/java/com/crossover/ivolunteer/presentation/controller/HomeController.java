package com.crossover.ivolunteer.presentation.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "Consulte a documentação da API para ver os endpoints: https://github.com/team-crossover/ivolunteer-api";
    }

}