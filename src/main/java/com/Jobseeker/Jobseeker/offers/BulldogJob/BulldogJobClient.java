package com.Jobseeker.Jobseeker.offers.BulldogJob;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bulldogJobClient", url = "https://bulldogjob.pl/companies/jobs/s")
public interface BulldogJobClient {

    @GetMapping("city,{location}/skills,{technology}/experienceLevel,{experience}")
    public String getOffers(
            @PathVariable("location") String location,
            @PathVariable("technology") String technology,
            @PathVariable("experience") String experience
    );
}
