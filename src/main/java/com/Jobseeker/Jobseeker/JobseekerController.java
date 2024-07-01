package com.Jobseeker.Jobseeker;

import com.Jobseeker.Jobseeker.favoriteOffers.FavoriteOffers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class JobseekerController {

    private final JobseekerService jobseekerService;

    @Autowired
    public JobseekerController(JobseekerService jobseekerService) {
        this.jobseekerService = jobseekerService;
    }

    @GetMapping("/hub")
    public String hub() {
        return "hub";
    }

    @PostMapping("/offers")
    public String getOffers(@RequestParam("location") String location,
                            @RequestParam("technology") String technology,
                            @RequestParam("experience") String experience,
                            Model model) {
        try {
            List<Offers> offersList = new ArrayList<>();
            List<Offers> offers = jobseekerService.getOffers(location, technology, experience);

            for(Offers o : offers) {
                Offers offer = new Offers(o.name(), o.salary(), o.link());
                offersList.add(offer);
            }
            model.addAttribute("offers", offersList);
            return "offers";


        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/favorites")
    public String addToFavorite(@ModelAttribute Offers offer) {
        jobseekerService.addToFavorite(offer);

        return "redirect:/favorites";
    }

    @GetMapping("/favorites")
    public String showFavorites(Model model) {
        LinkedList<FavoriteOffers> favoriteOffers = new LinkedList<>(jobseekerService.getFavoriteOffers());
        model.addAttribute("favorites", favoriteOffers);
        return "favorites";
    }



}
