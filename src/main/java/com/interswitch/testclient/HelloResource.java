package com.interswitch.testclient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/rest/hello/client")
public class HelloResource {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(
            fallbackMethod = "fallback",
            groupKey = "HelloResource",
            commandKey = "hello",
            threadPoolKey = "helloThread"
//            commandProperties = @HystrixProperty(name = "hystrix.command.hello.execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    )
    @GetMapping
    public String hello(){
        String url = "http://hello-server/rest/hello/server";
        return restTemplate.getForObject(url, String.class) + " including client";
    }

    public String fallback(Throwable hystrixCommand){
        return "Service Timeout";
    }

}
