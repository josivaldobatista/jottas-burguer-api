package com.jfb.jottasburger;

import org.springframework.boot.SpringApplication;

public class TestJottasburgerApplication {

	public static void main(String[] args) {
		SpringApplication.from(JottasburgerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
