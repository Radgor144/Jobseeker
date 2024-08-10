package com.Jobseeker.Jobseeker.offers.justJoin;

import com.Jobseeker.Jobseeker.Offers;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;
public class JustJoinSiteParserTest {

    @Test
    public void shouldReturnOffersFromJustJoinIt() throws IOException {
        // given
        String filePathToExampleHtml = "src/test/java/resources/Example_JustJoinIt_Offers.html";
        String filePathToExpectedData = "src/test/java/resources/ResultOfJustJoinItParser.txt";
        JustJoinSiteParser justJoinSiteParser = new JustJoinSiteParser();

        // when
        String html = openFileWithData(filePathToExampleHtml);
        String expected = openFileWithData(filePathToExpectedData);

        //then
        List<Offers> parse = justJoinSiteParser.parse(html);
        assertEquals(parse.toString(), expected);
    }

    private String openFileWithData(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

}
