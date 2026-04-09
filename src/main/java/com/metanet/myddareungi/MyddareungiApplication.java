package com.metanet.myddareungi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MyddareungiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyddareungiApplication.class, args);
	}

}
