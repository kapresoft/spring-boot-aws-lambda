package com.kapresoft.demo.springbootlambda;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
@ActiveProfiles("it")
public class ApplicationIT {

	@Autowired
	private ApplicationContext ctx;

	@Test
	public void contextLoads() {
		log.info("Application Loaded: {}", ctx.getId());
	}

}
