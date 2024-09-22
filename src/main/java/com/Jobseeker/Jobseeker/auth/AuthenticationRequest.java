package com.Jobseeker.Jobseeker.auth;

import lombok.Builder;

@Builder
public record AuthenticationRequest (String email, String password) {

}
