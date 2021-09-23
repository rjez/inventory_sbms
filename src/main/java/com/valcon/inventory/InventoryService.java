package com.valcon.inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableFeignClients
@SpringBootApplication
@RestController
public class InventoryService {

	final static Logger LOG = LoggerFactory.getLogger(InventoryService.class);

	@GetMapping("/")
	String home() {
		return "This is inventory service!";
	}

	public static void main(String[] args) throws Exception {
		// ConfigurableApplicationContext ctx = 
		SpringApplication.run(InventoryService.class, args);
//		SampleDataSetup dataSetup = ctx.getBean(SampleDataSetup.class);
		//
//		if (dataSetup.isLocaleEnvironment()) {
//			dataSetup.createSampleData();
//		}
		//
		LOG.info("Inventory app. started.");
	}
}