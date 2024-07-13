package com.Jobseeker.Jobseeker.pracujPl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pracujPlClient", url = "https://it.pracuj.pl/praca")
public interface PracujPlClient {

    @GetMapping("/{location};wp/?et={technology}&itth={experience}")
    public String getOffers(
            @PathVariable("location") String location,
            @PathVariable("technology") int technology,
            @PathVariable("experience") int experience
    );
}
