package org.simulspeak.bridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// @SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class BridgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BridgeApplication.class, args);
	}

}
