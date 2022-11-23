package com.devmountain.noteApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication (exclude = {DataSourceAutoConfiguration.class})
public class NoteAppApplication {
	public static void main(String[] args) {

		SpringApplication.run(NoteAppApplication.class, args);
	}

}
