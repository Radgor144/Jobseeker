package com.Jobseeker.Jobseeker.offers.Schedule;

import com.Jobseeker.Jobseeker.JobseekerService;
import com.Jobseeker.Jobseeker.Offers;
import com.Jobseeker.Jobseeker.dataBase.favorite.OffersEntity;
import com.Jobseeker.Jobseeker.dataBase.repositories.OffersEntityRepository;
import com.Jobseeker.Jobseeker.offers.OffersAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Component
public class OffersScheduler {

    private final OffersAggregator offersAggregator;
    private final JobseekerService jobseekerService;
    private final OffersEntityRepository offersEntityRepository;

    @Autowired
    public OffersScheduler(OffersAggregator offersAggregator, JobseekerService jobseekerService, OffersEntityRepository offersEntityRepository) {
        this.offersAggregator = offersAggregator;
        this.jobseekerService = jobseekerService;
        this.offersEntityRepository = offersEntityRepository;
    }

    @Scheduled(cron = "0 1 17 * * ?")
    public void scheduleFetchJobOffers() throws ExecutionException, InterruptedException {
        jobseekerService.addToDataBase(processOffersData(aggregateOffers()));
    }

    private Set<Offers> aggregateOffers() throws ExecutionException, InterruptedException {
        Set<Offers> allOffers = new HashSet<>();

        for (Location location : Location.values()) {
            for (Technology technology : Technology.values()) {
                for (Experience experience : Experience.values()) {
                    List<Offers> offers = offersAggregator.aggregateOffers(
                            location.name().toLowerCase(),
                            technology.name().toLowerCase(),
                            experience.name().toLowerCase()
                    );

                    if (offers != null && !offers.isEmpty()) {
                        allOffers.addAll(offers);
                    }
                }
            }
        }
        return allOffers;
    }

    private Set<Offers> processOffersData(Set<Offers> offers) {
        Set<Offers> validOffers = new HashSet<>();

        // Sprawdzenie, czy oferty istnieją w bazie danych, dodanie tych, których nie ma
        for (Offers offer : offers) {
            if (!offersEntityRepository.existsByLink(offer.link())) {
                validOffers.add(offer);
            }
        }

        // Zaktualizowanie ofert, które nie zostały znalezione podczas agregacji
        List<OffersEntity> allOffersInDb = offersEntityRepository.findAll();
        for (OffersEntity offerEntity : allOffersInDb) {
            boolean offerFound = offers.stream().anyMatch(offer -> offer.link().equals(offerEntity.getLink()));
            if (!offerFound) {
                offerEntity.setCurrent(false);
                offersEntityRepository.save(offerEntity);
            }
        }

        return validOffers;
    }
}
