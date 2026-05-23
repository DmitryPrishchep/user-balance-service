package by.staryhroft.userbalance.scheduler;

import by.staryhroft.userbalance.service.InterestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InterestScheduler {

    private final InterestService interestService;

    @Scheduled(fixedRate = 30000)
    public void applyInterest() {
        log.info("Запуск начисления процентов");
        interestService.applyInterest();
        log.info("Начисление процентов завершено");
    }
}