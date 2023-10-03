package org.springcloud.mcsv.cours.mcsvcours;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients //this annotation is to enable feign client to communicate with other microservices
@SpringBootApplication
public class McsvCoursApplication {

	public static void main(String[] args) {
		SpringApplication.run(McsvCoursApplication.class, args);
	}

}
