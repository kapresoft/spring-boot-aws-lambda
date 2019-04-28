package com.kapresoft.demo.springbootlambda.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

@Component
public class JsonUtils {

    private final Logger log = LoggerFactory.getLogger(JsonUtils.class);
    private ObjectMapper objectMapper;

    @Autowired
    public JsonUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonUtils() {
        this(new ObjectMapper());
    }

    public String toJson(Object object) {
        return ofNullable(object).map(o -> {
            try {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
            } catch (JsonProcessingException e) {
                log.error("Unexpected exception converting to json: {}", object, e);
            }
            return null;
        }).orElse(null);
    }

}
