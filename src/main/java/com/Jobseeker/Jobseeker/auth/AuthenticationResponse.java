package com.Jobseeker.Jobseeker.auth;

import lombok.Builder;


@Builder
public record AuthenticationResponse (String token) {
}
