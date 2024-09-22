package com.Jobseeker.Jobseeker.offers;

import com.Jobseeker.Jobseeker.Offers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class OffersAggregator {
    public List<OffersFetcher> offersFetchers;
    public OffersAggregator(List<OffersFetcher> offersFetchers) {
        this.offersFetchers = offersFetchers;
    }
    public List<Offers> aggregateOffers(String location, String technology, String experience) throws ExecutionException, InterruptedException {
        // Create a list of CompletableFutures for each externalOffersFacade
        List<CompletableFuture<List<Offers>>> futures = offersFetchers.stream()
                .map(facade -> facade.fetch(location, technology, experience))
                .toList();

        // Convert the list to an array
        CompletableFuture<?>[] futuresArray = futures.toArray(new CompletableFuture[0]);

        // Use CompletableFuture.allOf to wait for all futures to complete
        return CompletableFuture.allOf(futuresArray)
                .thenApply(voidResult ->
                        futures.stream()
                                .map(CompletableFuture::join) // Join each future to get the result
                                .flatMap(List::stream)       // Flatten the lists into a single stream
                                .toList()
                )
                .get(); // Collect the results into a single lis
    }
}
