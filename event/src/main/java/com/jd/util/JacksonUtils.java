package com.jd.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import org.apache.commons.lang3.StringUtils;

import lombok.experimental.UtilityClass;

/**
 * @author Jaedoo Lee
 */
@UtilityClass
public class JacksonUtils {

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new Hibernate5Module());
        objectMapper.registerModule(new Jdk8Module());

        return objectMapper;
    }

    public static String toJson(Object obj) {
        if (obj != null) {
            try {
                ObjectMapper mapper = getObjectMapper();
                return mapper.writeValueAsString(obj);
            } catch (Exception e) {
                return StringUtils.EMPTY;
            }
        } else {
            return StringUtils.EMPTY;
        }
    }

}
