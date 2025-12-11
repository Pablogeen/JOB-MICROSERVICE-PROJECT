package com.rey.job.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JsonUtil {
    private final ObjectMapper mapper;

    public String convertJavaObjectToJsonString(Object object) {

        try {
            return  mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Error converting object to JSON", e);
            throw new RuntimeException("Error converting object to JSON", e);
        }

    }

    public  <T> T convertJsonStringToJavaObject(String jsonString, Class<T> clazz) {

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            log.error("Error converting JSON to object", e);
            throw new RuntimeException("Error converting JSON to object", e);
        }
    }


}
