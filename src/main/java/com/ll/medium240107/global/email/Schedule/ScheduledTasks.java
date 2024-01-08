package com.ll.medium240107.global.email.Schedule;

import com.ll.medium240107.global.email.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private final EmailRepository emailRepository;

    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void deleteOldEmails() {
        Instant fiveMinutesAgo = Instant.now().minus(5, ChronoUnit.MINUTES);
        emailRepository.deleteByExpiredDateBefore(fiveMinutesAgo);
    }
}
