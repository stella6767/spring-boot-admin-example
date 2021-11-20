package com.example.sbaclient.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class TestService {


    @PostConstruct
    public void test(){

        log.info("시작!!");
    }

}
