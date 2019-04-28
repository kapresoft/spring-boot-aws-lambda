package com.kapresoft.demo.springbootlambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.kapresoft.demo.springbootlambda.util.JsonUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

import static java.util.Optional.ofNullable;
import static org.springframework.util.StringUtils.hasLength;

public class Lambda {

    private final Logger log = LoggerFactory.getLogger(Lambda.class);

    private final ConfigurableApplicationContext appContext;
    private final ConfigurableEnvironment environment;
    private final JsonUtils jsonUtils;

    public Lambda() {
        final SpringApplicationBuilder builder = new SpringApplicationBuilder(Application.class)
                .logStartupInfo(false);
        initProfile(builder);

        appContext = builder.build().run();
        this.environment = appContext.getEnvironment();
        jsonUtils = appContext.getBean(JsonUtils.class);
    }

    private void initProfile(SpringApplicationBuilder builder) {
        final String environment = ofNullable(System.getenv("ENVIRONMENT")).orElse("default");
        if (hasLength(environment)) {
            log.info("Starting lambda with profile: {}", environment);
            builder.profiles(environment);
        }
    }

    public LambdaResponse handler(Map<String, Object> request, Context context) {
        log.info("Environment: {}", (Object[]) environment.getActiveProfiles());
        log.info("Message #1: {}", environment.getProperty("message"));
        log.info("Message #2: {}", environment.getProperty("message2"));
        log.info("Input: {}", jsonUtils.toJson(request));
        log.info("Context: {}", ofNullable(context).map(c -> ReflectionToStringBuilder.toString(c, ToStringStyle.MULTI_LINE_STYLE))
                .orElse(null));
        log.info("AppContext: {}", appContext);

        final LambdaResponse response = LambdaResponse.builder().status("success").build();
        log.info("Response: {}", jsonUtils.toJson(response));
        return response;
    }

    ApplicationContext getContext() {
        return appContext;
    }


}
