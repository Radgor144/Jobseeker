package com.Jobseeker.Jobseeker.ParsersTests;

import com.Jobseeker.Jobseeker.Offers;
import com.Jobseeker.Jobseeker.offers.justJoin.JustJoinSiteParser;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
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
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder content = new StringBuilder();

            // Read the file line by line
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString().trim();
        }
    }

}
