package com.Jobseeker.Jobseeker.offers.justJoin;

import com.Jobseeker.Jobseeker.Offers;
import com.Jobseeker.Jobseeker.offers.pracujPl.PracujPlSiteParser;
import com.Jobseeker.Jobseeker.util.FileReader;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
public class ParsersTest {

    private static final String JUST_JOIN_IT_HTML_FILE_PATH = "src/test/java/resources/Example_JustJoinIt_Offers.html";
    private static final String JUST_JOIN_IT_EXPECTED_DATA = "src/test/java/resources/ResultOfJustJoinItParser.txt";

    private static final String PRACUJ_PL_HTML_FILE_PATH = "src/test/java/resources/Example_PracujPl_Offers.html";
    private static final String PRACUJ_PL_EXPECTED_DATA = "src/test/java/resources/ResultOfPracujPlParser.txt";

    @Test
    public void shouldReturnOffersFromJustJoinIt() throws IOException {
        // given
        JustJoinSiteParser justJoinSiteParser = new JustJoinSiteParser();

        // when
        String html = FileReader.openFileWithData(JUST_JOIN_IT_HTML_FILE_PATH);
        String expected = FileReader.openFileWithData(JUST_JOIN_IT_EXPECTED_DATA);

        //then
        List<Offers> parse = justJoinSiteParser.parse(html);
        assertEquals(parse.toString(), expected);
    }

    @Test
    public void shouldReturnOffersFromPracujPl() throws IOException {
        // given
        PracujPlSiteParser pracujPlSiteParser = new PracujPlSiteParser();

        // when
        String html = FileReader.openFileWithData(PRACUJ_PL_HTML_FILE_PATH);
        String expected = FileReader.openFileWithData(PRACUJ_PL_EXPECTED_DATA);

        //then
        List<Offers> parse = pracujPlSiteParser.parse(html);
        assertEquals(parse.toString(), expected);
    }

}
