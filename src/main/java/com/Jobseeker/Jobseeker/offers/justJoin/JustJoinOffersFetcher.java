package com.Jobseeker.Jobseeker.offers.justJoin;

import com.Jobseeker.Jobseeker.Offers;
import com.Jobseeker.Jobseeker.offers.OffersFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Component
public class JustJoinOffersFetcher implements OffersFetcher {

    private final JustJoinClient justJoinClient;

    private final JustJoinSiteParser justJoinSiteParser;

    @Override
    public CompletableFuture<List<Offers>> fetch(String location, String technology, String experience) {
        return CompletableFuture.supplyAsync(() -> {
            String response = justJoinClient.getOffers(location, technology, experience);
            return justJoinSiteParser.parse(response);
        });
    }
}
