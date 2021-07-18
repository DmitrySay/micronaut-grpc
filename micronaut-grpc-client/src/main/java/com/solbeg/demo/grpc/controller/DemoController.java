package com.solbeg.demo.grpc.controller;

import com.solbeg.demo.grpc.service.DemoClientService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;


@Controller("/api/demo")
public class DemoController {
    private final DemoClientService demoClientService;

    public DemoController(DemoClientService demoClientService) {
        this.demoClientService = demoClientService;
    }

    @Get("/{name}")
    public String getDemoMessage(@PathVariable("name") String name) {
        return demoClientService.generateDemoMessage(name);
    }
}
