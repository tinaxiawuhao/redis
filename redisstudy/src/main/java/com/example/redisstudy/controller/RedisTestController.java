package com.example.redisstudy.controller;

import com.example.redisstudy.service.RedisTestService;
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
    @Autowired
    private RedisTestService redisTestService;

    @GetMapping("/test")
    public void test() {
         redisTestService.test();
    }

}
