package com.nuttu.aicloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages ={"com.nuttu.aicloud.repository"})
@EntityScan(basePackages ={ "com.nuttu.aicloud.model"})
@EnableTransactionManagement
@IntegrationComponentScan
public class AicloudServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AicloudServerApplication.class, args);
	}
}
