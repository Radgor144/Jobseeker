package com.Jobseeker.Jobseeker.offers.pracujPl;

import com.Jobseeker.Jobseeker.Offers;
import com.Jobseeker.Jobseeker.offers.OffersFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Component
public class PracujPlOffersFetcher implements OffersFetcher {

    private final PracujPlClient pracujPlClient;

    private final PracujPlSiteParser pracujPlSiteParser;

    @Override
    public CompletableFuture<List<Offers>> fetch(String location, String technology, String experience) {
        return CompletableFuture.supplyAsync(() -> {
            String response = pracujPlClient.getOffers(location, 17, 38);
            return pracujPlSiteParser.parse(response);
        });
    }
}
