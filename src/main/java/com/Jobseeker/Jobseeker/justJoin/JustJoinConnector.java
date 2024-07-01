package com.Jobseeker.Jobseeker.justJoin;

import com.Jobseeker.Jobseeker.Offers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JustJoinConnector {

    public List<Offers> justJoinParser(String html) {
        Document doc = Jsoup.parse(html);
        Elements jobs = doc.select("div[data-index]");
        Elements links = doc.select("a.offer_list_offer_link");
        List<Offers> jobList = new ArrayList<>();

        for (int i = 0; i < jobs.size(); i++) {
            Element job = jobs.get(i);
            Element link = links.get(i);

            String name = job.select("h3").text();
            String salary = job.select(".css-18ypp16").text();
            String href = link.attr("href");

            Offers offer = new Offers(name, salary, "https://justjoin.it" + href);
            jobList.add(offer);
        }
        return jobList;
    }

}
