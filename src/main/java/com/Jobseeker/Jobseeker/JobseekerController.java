package com.Jobseeker.Jobseeker;


import com.Jobseeker.Jobseeker.dataBase.favorite.OffersEntity;
import com.Jobseeker.Jobseeker.offers.OffersAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PostMapping("/add")
    public ResponseEntity<?> addFavorite(@RequestParam Long userId, @RequestParam Long favoriteOfferId) {
        jobseekerService.addFavorite(userId, favoriteOfferId);
        return ResponseEntity.ok("Offer added to favorites");
    }
    @PostMapping("/delete")
    public String deleteFavorite(@RequestParam Long userId, @RequestParam Long favoriteOfferId) {
        jobseekerService.deleteFavorite(userId, favoriteOfferId);
        return "Offer deleted from favorites";
    }

    @GetMapping("/list")
    public ResponseEntity<List<OffersEntity>> getFavorites(@RequestParam Long userId) {
        return ResponseEntity.ok(jobseekerService.getFavorites(userId));
    }
}
