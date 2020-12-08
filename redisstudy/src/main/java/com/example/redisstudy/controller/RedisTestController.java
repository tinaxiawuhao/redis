package com.example.redisstudy.controller;

import com.example.redisstudy.service.impl.RedisTestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuhao
 * @desc ...
 * @date 2020-12-04 15:40:18
 */
@RestController
public class RedisTestController {
    /*SpringBoot默认的AOP实现就是使用的CGLib代理*/
    @Autowired
    private RedisTestServiceImpl redisTestService;

    @GetMapping("/vote")
    public void vote() {
         redisTestService.vote();
    }
    @GetMapping("/publish")
    public void publish() {
         redisTestService.publish();
    }

    @GetMapping("/sort")
    public void sort() {
         redisTestService.sort();
    }

    @GetMapping("/pipelineTest")
    public void pipelineTest() {
         redisTestService.pipelineTest();
    }

    @GetMapping("/transactionalTest")
    public void transactionalTest() {
         redisTestService.transactionalTest();
    }

    @GetMapping("/transactionalTest2")
    public void transactionalTest2() {
        redisTestService.transactionalTest2();
    }

}
