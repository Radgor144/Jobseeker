package com.Jobseeker.Jobseeker.offers.justJoin;

import com.Jobseeker.Jobseeker.Offers;
import com.Jobseeker.Jobseeker.offers.OffersAggregator;
import com.Jobseeker.Jobseeker.offers.OffersFetcher;
import com.Jobseeker.Jobseeker.offers.pracujPl.PracujPlSiteParser;
import com.Jobseeker.Jobseeker.util.FileReader;
import com.Jobseeker.Jobseeker.util.JsonFileReaderToList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OffersAggregatorTest {

    private static final String OFFERS_DATA_RESOURCE_PATH = "src/test/java/resources/Example_of_searching_for_offers.json";
    private static final String JUST_JOIN_IT_HTML_FILE_PATH = "src/test/java/resources/Example_JustJoinIt_Offers.html";
    private static final String PRACUJ_PL_HTML_FILE_PATH = "src/test/java/resources/Example_PracujPl_Offers.html";

    @Mock
    private OffersFetcher pracujPlFetcher;

    @Mock
    private OffersFetcher justJoinFetcher;

    @Autowired
    private OffersAggregator offersAggregator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnOffers() throws IOException, ExecutionException, InterruptedException {
        // given
        offersAggregator = new OffersAggregator(List.of(pracujPlFetcher, justJoinFetcher));

        String html = FileReader.openFileWithData(JUST_JOIN_IT_HTML_FILE_PATH);
        List<Offers> justJoinOffers = new JustJoinSiteParser().parse(html);

        String html2 = FileReader.openFileWithData(PRACUJ_PL_HTML_FILE_PATH);
        List<Offers> pracujPlOffers = new PracujPlSiteParser().parse(html2);

        when(justJoinFetcher.fetch("krakow", "java", "junior"))
                .thenReturn(CompletableFuture.completedFuture(justJoinOffers));
        when(pracujPlFetcher.fetch("krakow", "java", "junior"))
                .thenReturn(CompletableFuture.completedFuture(pracujPlOffers));

        List<Offers> expectedOffersData = JsonFileReaderToList.readJson(objectMapper, OFFERS_DATA_RESOURCE_PATH, new TypeReference<>() {
        });

        // when
        List<Offers> result = offersAggregator.aggregateOffers("krakow", "java", "junior");

        // then
        assertEquals(expectedOffersData, result);
    }

}
