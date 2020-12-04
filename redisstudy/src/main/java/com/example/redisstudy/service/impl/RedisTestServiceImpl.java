package com.example.redisstudy.service.impl;

import com.example.redisstudy.service.RedisTestService;
import com.example.redisstudy.template.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wuhao
 * @desc ...
 * @date 2020-12-04 15:40:35
 */
@Service
@Slf4j
public class RedisTestServiceImpl implements RedisTestService {
    @Autowired
    private  RedisUtil RedisUtil;

    /**
     * 对文章进行投票
     * 文章评分随着时间流逝而减少 公式 fraction=System.currentTimeMillis()+(60*60*24/standardVotes)*vote
     * 文章存储结构  hash  article:12345 |title   测试标题
     *                                 |link    www.test.com
     *                                 |poster  user:123
     *                                 |time    1233333333
     *                                 |vote    234
     *
     * 存储文章  按发布时间存储  zset  time   article:12345  1233333333
     *         按评分排序     zset  score   article:12345  23223233
     *
     * 投票记录  set voted  user:1234  user:2343
     */
    //发布一个星期关闭投票
//    private Integer ONE_WEEK_IN_SECONDS=7*24*60*60;
    private Integer SCORE=432;
    @Override
    public void test() {
        String userId="user:12345";
        String article="article:12345";
        //跟新评分和投票记录,更新投票数
        RedisUtil.sSet("voted",userId);
        RedisUtil.zincrScore("time",article,SCORE);
        RedisUtil.hincr(article,"vote",1);
    }
}
