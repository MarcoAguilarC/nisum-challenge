package com.nisum.challenge.testutil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class TestResourceUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private TestResourceUtils() { }

    public static String readJsonAsString(String classpathLocation) {
        try (InputStream is = new ClassPathResource(classpathLocation).getInputStream()) {
            JsonNode node = OBJECT_MAPPER.readTree(is);
            return OBJECT_MAPPER.writeValueAsString(node);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read JSON from classpath: " + classpathLocation, e);
        }
    }

    public static <T> T readJsonAsObject(String classpathLocation, Class<T> type) {
        try (InputStream is = new ClassPathResource(classpathLocation).getInputStream()) {
            return OBJECT_MAPPER.readValue(is, type);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read JSON from classpath: " + classpathLocation, e);
        }
    }

    public static String readText(String classpathLocation) {
        try (InputStream is = new ClassPathResource(classpathLocation).getInputStream()) {
            byte[] bytes = is.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read text from classpath: " + classpathLocation, e);
        }
    }
}
