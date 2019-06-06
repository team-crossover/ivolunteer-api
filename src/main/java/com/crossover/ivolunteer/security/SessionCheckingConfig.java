package com.crossover.ivolunteer.security;

import com.crossover.ivolunteer.business.service.SessaoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Periodicamente deleta as sessões que expiraram.
 */
@Log4j2
@Configuration
@EnableScheduling
public class SessionCheckingConfig {

    private static final long SESSION_CHECKING_INITIAL_DELAY = 1000 * 5; // 5 seconds

    private static final long SESSION_CHECKING_DELAY = 1000 * 60 * 10; // 10 minutes

    @Autowired
    private SessaoService sessaoService;

    @Scheduled(initialDelay = SESSION_CHECKING_INITIAL_DELAY, fixedDelay = SESSION_CHECKING_DELAY)
    public void killExpiredSessions() {
        sessaoService.deleteExpired();
    }

}
