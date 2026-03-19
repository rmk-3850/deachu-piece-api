package com.rm.vote.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DummyLoader implements ApplicationRunner{
    private final VoteService voteService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*
        voteService.init();
         */
    }
}
