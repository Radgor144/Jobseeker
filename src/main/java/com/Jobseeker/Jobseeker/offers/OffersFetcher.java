package com.Jobseeker.Jobseeker.offers;

import com.Jobseeker.Jobseeker.Offers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OffersFetcher {

    CompletableFuture<List<Offers>> fetch(String location, String technology, String experience);
}
