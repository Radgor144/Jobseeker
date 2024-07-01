package com.Jobseeker.Jobseeker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class JobseekerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobseekerApplication.class, args);
	}

}
