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
            int tech = getTechnologyCode(technology);
            int exp = getExperienceCode(experience);

            String response = pracujPlClient.getOffers(location, tech, exp);
            return pracujPlSiteParser.parse(response);
        });
    }

    private int getTechnologyCode(String technology) {
        return switch (technology) {
            case "javascript" -> 33;
            case "python" -> 37;
            case "java" -> 38;
            case "c" -> 39;
            default -> throw new IllegalStateException("Unexpected value: " + technology);
        };
    }

    private int getExperienceCode(String experience) {
        return switch (experience) {
            case "junior" -> 17;
            case "mid" -> 4;
            case "senior" -> 18;
            default -> throw new IllegalStateException("Unexpected value: " + experience);
        };
    }
}
