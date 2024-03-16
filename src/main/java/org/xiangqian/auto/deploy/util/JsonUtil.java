package org.xiangqian.auto.deploy.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author xiangqian
 * @date 11:36 2024/03/10
 */
public class JsonUtil {

    private static final ObjectMapper om = new ObjectMapper();

    public static String serializeAsString(Object object) throws IOException {
        return om.writeValueAsString(object);
    }

    public static String serializeAsPrettyString(Object object) throws IOException {
        return om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    public static <T> T deserialize(String text, Class<T> type) throws IOException {
        return om.readValue(text, type);
    }

    public static <T> T deserialize(String text, TypeReference<T> typeRef) throws IOException {
        return om.readValue(text, typeRef);
    }

}
