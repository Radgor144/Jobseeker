package com.Jobseeker.Jobseeker;

import com.Jobseeker.Jobseeker.bulldogJob.BulldogJobClient;
import com.Jobseeker.Jobseeker.bulldogJob.BulldogJobConnector;
import com.Jobseeker.Jobseeker.favoriteOffers.FavoriteOffers;
import com.Jobseeker.Jobseeker.favoriteOffers.FavoriteOffersRepository;
import com.Jobseeker.Jobseeker.justJoin.JustJoinClient;
import com.Jobseeker.Jobseeker.justJoin.JustJoinConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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

    @Async
    public CompletableFuture<List<Offers>> getJustJoinOffers(String location, String technology, String experience) {
        return CompletableFuture.supplyAsync(() -> {
            ResponseEntity<String> response = justJoinClient.getOffers(location, technology, experience);
            String html = response.getBody();
            return justJoinConnector.justJoinParser(html);
        });
    }

    @Async
    public CompletableFuture<List<Offers>> getBulldogJobOffers(String location, String technology, String experience) {
        return CompletableFuture.supplyAsync(() -> {
            String bulldogExperience = "mid".equals(experience) ? "medium" : experience;
            ResponseEntity<String> response = bulldogJobClient.getOffers(location, technology, bulldogExperience);
            String html = response.getBody();
            return bulldogJobConnector.bulldogJobParser(html);
        });
    }

    public List<Offers> getOffers(String location, String technology, String experience) throws IOException, ExecutionException, InterruptedException {
        List<Offers> offersList = new ArrayList<>();

        CompletableFuture<List<Offers>> justJoinFuture = getJustJoinOffers(location, technology, experience);
        CompletableFuture<List<Offers>> bulldogJobFuture = getBulldogJobOffers(location, technology, experience);

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(justJoinFuture, bulldogJobFuture);
        combinedFuture.get();

        offersList.addAll(justJoinFuture.get());
        offersList.addAll(bulldogJobFuture.get());

        return offersList;
    }

    @Transactional
    public void addToFavorite(Offers offer) {
        if (!favoriteOffersRepository.existsByNameAndSalaryAndLink(offer.name(), offer.salary(), offer.link())) {
            FavoriteOffers favoriteOffer = new FavoriteOffers(offer.name(), offer.salary(), offer.link());
            favoriteOffersRepository.save(favoriteOffer);
        }
    }

    public Page<FavoriteOffers> getFavoriteOffers() {
        Pageable pageable = PageRequest.of(0, 3);
        return favoriteOffersRepository.findAll(pageable);
    }
}
