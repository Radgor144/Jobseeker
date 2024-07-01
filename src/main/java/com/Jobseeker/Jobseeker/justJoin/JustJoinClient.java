package com.Jobseeker.Jobseeker.justJoin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "justJoinClient", url = "https://justjoin.it")
public interface JustJoinClient {

    @GetMapping("/{location}/{technology}/experience-level_{experience}")
    ResponseEntity<String> getOffers(
            @PathVariable("location") String location,
            @PathVariable("technology") String technology,
            @PathVariable("experience") String experience
    );
}
