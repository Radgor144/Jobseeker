package com.Jobseeker.Jobseeker.pracujPl;

import com.Jobseeker.Jobseeker.Offers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PracujPlConnector {

    public List<Offers> pracujPlParser(String html) {
        Document doc = Jsoup.parse(html);
        Elements jobs = doc.select(".tiles_c1k2agp8");
        List<Offers> jobList = new ArrayList<>();

        for (Element job: jobs) {
            String name = job.select("h2").text();
            String salary = job.select(".tiles_s192qrcb").text();
            Elements link = job.select(".tiles_c8yvgfl");
            String href = link.attr("href");

            Offers jobData = new Offers(name, salary, href);
            jobList.add(jobData);
        }

        return jobList;
    }

}
