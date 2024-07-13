package com.Jobseeker.Jobseeker;


import com.Jobseeker.Jobseeker.favoriteOffers.FavoriteOffers;
import com.Jobseeker.Jobseeker.offers.OffersAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class JobseekerController {

    private final OffersAggregator offersAggregator;

    private final JobseekerService jobseekerService;

    private List<Offers> offers = new ArrayList<>();

    @Autowired
    public JobseekerController(JobseekerService jobseekerService, OffersAggregator offersAggregator) {
        this.offersAggregator = offersAggregator;
        this.jobseekerService = jobseekerService;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @PostMapping("/offers")
    public String searchJobOffers(@RequestParam("location") String location,
                                  @RequestParam("technology") String technology,
                                  @RequestParam("experience") String experience,
                                  Model model) {
        try {
            offers = offersAggregator.aggregateOffers(location, technology, experience);

            model.addAttribute("offers", offers);
            return "offers";

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/offers")
    public String refreshOffers(Model model) {
        model.addAttribute("offers", offers);
        return "offers";
    }

    @PostMapping("/favorites")
    public String addOfferToFavorite(@ModelAttribute Offers offer) {
        jobseekerService.addToFavoriteList(offer);

        return "redirect:/offers";
    }

    @GetMapping("/favorites")
    public String getFavoriteOffers(Model model) {
        Page<FavoriteOffers> favoriteOffersPage = jobseekerService.getTenFavoriteOffers();
        model.addAttribute("favorites", favoriteOffersPage);
        return "favorites";
    }
}
