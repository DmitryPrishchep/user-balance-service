package by.staryhroft.userbalance.scheduler;

import by.staryhroft.userbalance.service.InterestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InterestScheduler {

    private static final Logger log = LoggerFactory.getLogger(InterestScheduler.class);

    private final InterestService interestService;

    public InterestScheduler(InterestService interestService) {
        this.interestService = interestService;
    }

    @Scheduled(fixedRate = 30000)
    public void applyInterest() {
        log.info("Запуск начисления процентов");
        interestService.applyInterest();
        log.info("Начисление процентов завершено");
    }
}