package com.Jobseeker.Jobseeker.offers.bulldogJob;

import com.Jobseeker.Jobseeker.Offers;
import com.Jobseeker.Jobseeker.offers.OffersFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Component
public class BulldogJobOffersFetcher implements OffersFetcher {

    private final BulldogJobClient bulldogJobClient;

    private final BulldogJobSiteParser bulldogJobSiteParser;

    @Override
    public CompletableFuture<List<Offers>> fetch(String location, String technology, String experience) {
        String bulldogExperience = "mid".equals(experience) ? "medium" : experience;
        return CompletableFuture.supplyAsync(() -> {
            String response = bulldogJobClient.getOffers(location, technology, bulldogExperience);
            return bulldogJobSiteParser.parse(response);
        });
    }
}
