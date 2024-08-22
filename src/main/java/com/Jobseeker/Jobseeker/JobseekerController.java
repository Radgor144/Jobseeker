package com.Jobseeker.Jobseeker;


import com.Jobseeker.Jobseeker.dataBase.Favorite.OffersInDB;
import com.Jobseeker.Jobseeker.offers.OffersAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/secure")
public class JobseekerController {

    private final OffersAggregator offersAggregator;
    private final JobseekerService jobseekerService;

    @Autowired
    public JobseekerController(JobseekerService jobseekerService, OffersAggregator offersAggregator) {
        this.offersAggregator = offersAggregator;
        this.jobseekerService = jobseekerService;
    }

    @PostMapping("/offers")
    public List<Offers> searchJobOffers(@RequestParam("location") String location,
                                        @RequestParam("technology") String technology,
                                        @RequestParam("experience") String experience) throws ExecutionException, InterruptedException {
        List<Offers> offers = offersAggregator.aggregateOffers(location, technology, experience);
        jobseekerService.addToDataBase(offers);
        return offers;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFavorite(@RequestParam Long userId, @RequestParam Long favoriteOfferId) {
        jobseekerService.addFavorite(userId, favoriteOfferId);
        return ResponseEntity.ok("Offer added to favorites");
    }
    @PostMapping("/delete")
    public ResponseEntity<?> deleteFavorite(@RequestParam Long userId, @RequestParam Long favoriteOfferId) {
        jobseekerService.deleteFavorite(userId, favoriteOfferId);
        return ResponseEntity.ok("Offer deleted from favorites");
    }

    @GetMapping("/list")
    public ResponseEntity<List<OffersInDB>> getFavorites(@RequestParam Long userId) {
        return ResponseEntity.ok(jobseekerService.getFavorites(userId));
    }
}
