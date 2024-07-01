package com.Jobseeker.Jobseeker;

import com.Jobseeker.Jobseeker.bulldogJob.BulldogJobClient;
import com.Jobseeker.Jobseeker.bulldogJob.BulldogJobConnector;
import com.Jobseeker.Jobseeker.favoriteOffers.FavoriteOffers;
import com.Jobseeker.Jobseeker.favoriteOffers.FavoriteOffersRepository;
import com.Jobseeker.Jobseeker.justJoinIT.JustJoinClient;
import com.Jobseeker.Jobseeker.justJoinIT.JustJoinConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class JobseekerService {

    private final JustJoinClient justJoinClient;
    private final JustJoinConnector justJoinConnector;
    private final BulldogJobClient bulldogJobClient;
    private final BulldogJobConnector bulldogJobConnector;
    private final FavoriteOffersRepository favoriteOffersRepository;

    @Autowired
    public JobseekerService(JustJoinClient justJoinClient,
                            JustJoinConnector justJoinConnector,
                            BulldogJobClient bulldogJobClient,
                            BulldogJobConnector bulldogJobConnector,
                            FavoriteOffersRepository favoriteOffersRepository) {
        this.justJoinClient = justJoinClient;
        this.justJoinConnector = justJoinConnector;
        this.bulldogJobClient = bulldogJobClient;
        this.bulldogJobConnector = bulldogJobConnector;
        this.favoriteOffersRepository = favoriteOffersRepository;
    }

    public List<Offers> getOffers(String location, String technology, String experience) throws IOException, ExecutionException, InterruptedException {
        List<Offers> offersList = new ArrayList<>();

        CompletableFuture<List<Offers>> watek1 = CompletableFuture.supplyAsync(() -> {
            ResponseEntity<String> response = justJoinClient.getOffers(location, technology, experience);
            String html = response.getBody();
            offersList.addAll(justJoinConnector.getJustJoin(html));
            return offersList;
        });

        CompletableFuture<List<Offers>> watek2 = CompletableFuture.supplyAsync(() -> {
            String bulldogExperience = "mid".equals(experience) ? "medium" : experience;
            ResponseEntity<String> response = bulldogJobClient.getOffers(location, technology, bulldogExperience);
            String html = response.getBody();
            offersList.addAll(bulldogJobConnector.getBulldogJob(html));
            return offersList;
        });

        watek1.get();
        return offersList;
    }

    @Transactional
    public void addToFavorite(Offers offer) {
        if (!favoriteOffersRepository.existsByNameAndSalaryAndLink(offer.name(), offer.salary(), offer.link())) {
            FavoriteOffers favoriteOffer = new FavoriteOffers(offer.name(), offer.salary(), offer.link());
            favoriteOffersRepository.save(favoriteOffer);
        }
    }


    public List<FavoriteOffers> getFavoriteOffers() {
        return favoriteOffersRepository.findAll();
    }
}
