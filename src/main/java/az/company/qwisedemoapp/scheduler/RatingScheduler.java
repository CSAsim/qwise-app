package az.company.qwisedemoapp.scheduler;

import az.company.qwisedemoapp.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingScheduler {

    private final RatingService ratingService;

    @Scheduled(cron = "0 0 0 * * *")
    public void deactivateDailyRatings(){
        ratingService.rolloverExpiredRatings();
    }
}
