package com.Jobseeker.Jobseeker;

import com.Jobseeker.Jobseeker.bulldogJob.BulldogJobClient;
import com.Jobseeker.Jobseeker.bulldogJob.BulldogJobConnector;
import com.Jobseeker.Jobseeker.favoriteOffers.FavoriteOffers;
import com.Jobseeker.Jobseeker.favoriteOffers.FavoriteOffersRepository;
import com.Jobseeker.Jobseeker.justJoin.JustJoinClient;
import com.Jobseeker.Jobseeker.justJoin.JustJoinConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final TaskExecutor taskExecutor;

    @Autowired
    public JobseekerService(JustJoinClient justJoinClient,
                            JustJoinConnector justJoinConnector,
                            BulldogJobClient bulldogJobClient,
                            BulldogJobConnector bulldogJobConnector,
                            FavoriteOffersRepository favoriteOffersRepository,
                            @Qualifier("threadPoolTaskExecutor") TaskExecutor taskExecutor) {
        this.justJoinClient = justJoinClient;
        this.justJoinConnector = justJoinConnector;
        this.bulldogJobClient = bulldogJobClient;
        this.bulldogJobConnector = bulldogJobConnector;
        this.favoriteOffersRepository = favoriteOffersRepository;
        this.taskExecutor = taskExecutor;
    }
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<List<Offers>> getOffers(String location, String technology, String experience) throws IOException, ExecutionException, InterruptedException {
        List<Offers> offersList = new ArrayList<>();

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            String response = justJoinClient.getOffers(location, technology, experience);
            offersList.addAll(justJoinConnector.justJoinParser(response));
        }, taskExecutor);

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            String bulldogExperience = "mid".equals(experience) ? "medium" : experience;
            String response = bulldogJobClient.getOffers(location, technology, bulldogExperience);
            offersList.addAll(bulldogJobConnector.bulldogJobParser(response));
        }, taskExecutor);

        return CompletableFuture.allOf(future1, future2)
                .thenApplyAsync((Void) -> offersList);
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
