package com.eni.encheres;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.eni.encheres")
public class EncheresApplication {

	public static void main(String[] args) {
		System.out.println("Démarrage de l'application.");
		SpringApplication.run(EncheresApplication.class, args);
	}

}
