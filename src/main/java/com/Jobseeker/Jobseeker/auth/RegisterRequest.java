package com.Jobseeker.Jobseeker.auth;

import lombok.Builder;


@Builder
public record RegisterRequest (String firstname, String lastname, String email, String password) {

}
