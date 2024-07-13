package com.Jobseeker.Jobseeker.offers.bulldogJob;


import com.Jobseeker.Jobseeker.Offers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BulldogJobSiteParser {

    public List<Offers> parse(String html) {
        Document doc = Jsoup.parse(html);
        Elements jobs = doc.select(".JobListItem_item__M79JI");

        List<Offers> jobList = new ArrayList<>();

        for (Element job: jobs) {
            String name = job.select(".JobListItem_item__title__Ae2Pm > h3").text();
            String salary = job.select(".JobListItem_item__salary__Jd_AJ").text();
            String href = job.attr("href");
            Offers jobData = new Offers(name, salary, href);
            jobList.add(jobData);
        }

        return jobList;
    }
}
