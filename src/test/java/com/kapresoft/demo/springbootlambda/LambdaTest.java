package com.kapresoft.demo.springbootlambda;

import com.kapresoft.demo.springbootlambda.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class LambdaTest {

    private final Logger log = LoggerFactory.getLogger(LambdaTest.class);

    @Test
    public void lambdaExecutes() {
        final Lambda lambda = new Lambda();
        final JsonUtils jsonUtils = lambda.getContext().getBean(JsonUtils.class);

        final LambdaResponse response = lambda.handler(Collections.emptyMap(), null);

        assertThat(response).as("Response").isNotNull().satisfies(r -> {
            assertThat(r.getStatus()).as("Response.status")
                    .isEqualToIgnoringCase("success");
        });

        final String responseText = jsonUtils.toJson(response);
        log.info("Response: {}", responseText);
    }

}