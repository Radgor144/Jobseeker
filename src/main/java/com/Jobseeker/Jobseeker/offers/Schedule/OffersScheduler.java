package com.Jobseeker.Jobseeker.offers.Schedule;

import com.Jobseeker.Jobseeker.JobseekerService;
import com.Jobseeker.Jobseeker.Offers;
import com.Jobseeker.Jobseeker.offers.OffersAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class OffersScheduler {

    private final OffersAggregator offersAggregator;
    private final JobseekerService jobseekerService;

    @Autowired
    public OffersScheduler(OffersAggregator offersAggregator, JobseekerService jobseekerService) {
        this.offersAggregator = offersAggregator;
        this.jobseekerService = jobseekerService;
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void scheduleFetchJobOffers() throws ExecutionException, InterruptedException {
        for (Location location : Location.values()) {
            for (Technology technology : Technology.values()) {
                for (Experience experience : Experience.values()) {
                    List<Offers> offers = offersAggregator.aggregateOffers(
                            location.name().toLowerCase(),
                            technology.name().toLowerCase(),
                            experience.name().toLowerCase()
                    );
                    jobseekerService.addToDataBase(offers);
                    System.out.println("Pobrano oferty dla: " + location + ", " + technology + ", " + experience);
                }
            }
        }
    }
}
