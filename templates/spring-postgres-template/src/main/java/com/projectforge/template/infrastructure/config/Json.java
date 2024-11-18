package com.projectforge.template.infrastructure.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import java.util.concurrent.Callable;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

// Using an enum to implement a singleton is considered one of the best practices to ensure that the
// instance is created in a lazy and thread-safe manner, without having to worry about
// synchronization issues.
public enum Json {
  INSTANCE;

  private final ObjectMapper mapper =
      new Jackson2ObjectMapperBuilder()
          // For serialization defaults to using an ISO-8601 compliant format (format String
          // "yyyy-MM-dd'T'HH:mm:ss. SSSZ") and for deserialization, both ISO-8601 and RFC-1123.
          .dateFormat(new StdDateFormat())
          .featuresToDisable(
              DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
              DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
              DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,
              SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .modules(new JavaTimeModule(), new Jdk8Module(), afterburnerModule())
          .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
          .build();

  public static ObjectMapper mapper() {
    return INSTANCE.mapper.copy();
  }

  public static String writeValueAsString(final Object obj) {
    return invoke(() -> INSTANCE.mapper.writeValueAsString(obj));
  }

  public static <T> T readValue(final String json, final Class<T> clazz) {
    return invoke(() -> INSTANCE.mapper.readValue(json, clazz));
  }

  private static <T> T invoke(final Callable<T> callable) {
    try {
      return callable.call();
    } catch (final Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  private AfterburnerModule afterburnerModule() {
    var module = new AfterburnerModule();
    // make Afterburner generate bytecode only for public getters/setter and fields
    // without this, Java 9+ complains of "Illegal reflective access"
    module.setUseValueClassLoader(false);
    return module;
  }
}
