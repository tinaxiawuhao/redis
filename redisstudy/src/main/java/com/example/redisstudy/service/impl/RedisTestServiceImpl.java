package com.example.redisstudy.service.impl;

import com.example.redisstudy.service.RedisTestService;
import com.example.redisstudy.template.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author wuhao
 * @desc ...
 * @date 2020-12-04 15:40:35
 */
@Service
@Slf4j
public class RedisTestServiceImpl implements RedisTestService {
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 对文章进行投票
     * 文章评分随着时间流逝而减少 公式 fraction=System.currentTimeMillis()+(60*60*24/standardVotes)*vote
     * 文章存储结构  hash  article:12345 |title   测试标题
     * |link    www.test.com
     * |poster  user:123
     * |time    1233333333
     * |vote    234
     * <p>
     * 存储文章  按发布时间存储  zset  time   article:12345  1607071797287
     * 按评分排序     zset  score   article:12345  23223233
     * <p>
     * 投票记录  set voted  user:1234  user:2343
     */
    //发布一个星期关闭投票
    private Integer ONE_WEEK_IN_SECONDS = 7 * 24 * 60 * 60;
    private Integer SCORE = 432;
    private Integer PAGE_NUMBER = 10;

    @Override
    public void vote() {
        String userId = "user:12345";
        String article = "article:12345";
        //跟新评分和投票记录,更新投票数
        redisUtil.sSet("voted", userId);
        redisUtil.zincrScore("time", article, SCORE);
        redisUtil.hincr(article, "vote", 1);
        Map map = new HashMap();
        map.put("title", "测试标题");
        map.put("link", "www.test.com");
        map.put("poster", userId);
        map.put("time", System.currentTimeMillis());
        redisUtil.hmset(article, map);
    }

    /**
     * 发布并获取文章
     */
    @Override
    public void publish() {
        String userId = "user:23465";
        String article = "article:23456";
        redisUtil.sSetAndTime("voted", ONE_WEEK_IN_SECONDS, userId);
        Map map = new HashMap();
        map.put("title", "测试标题2");
        map.put("link", "www.test2.com");
        map.put("poster", userId);
        map.put("time", System.currentTimeMillis());
        map.put("vote", 1);
        redisUtil.hmset(article, map);
        redisUtil.zadd("time", article, System.currentTimeMillis());
        redisUtil.zadd("score", article, System.currentTimeMillis());
    }

    /**
     * 获取最新和评分最高的文章
     */
    @Override
    public void sort() {
        int start = 0 * PAGE_NUMBER;
        int end = 1 * PAGE_NUMBER;
        Set<Object> time = redisUtil.zrevRange("time", start, end);
        System.out.println(time);
        Set<Object> score = redisUtil.zrevRange("score", start, end);
        System.out.println(score);
    }
}
