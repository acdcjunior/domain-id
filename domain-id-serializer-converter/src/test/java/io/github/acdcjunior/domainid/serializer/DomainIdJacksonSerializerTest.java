package io.github.acdcjunior.domainid.serializer;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.acdcjunior.domainid.DomainId;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


class DomainIdJacksonSerializerTest {

    private static class ExampleDomainId extends DomainId {
        ExampleDomainId(long id) {
            super(id);
        }
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    private static class SomeClassWithIdProperty {
        public int j = 1;
        public ExampleDomainId idObject = new ExampleDomainId(20L);
    }

    @Test
    void convertsObjectIntoJson() throws Exception {
        // setup
        SomeClassWithIdProperty someObject = new SomeClassWithIdProperty();
        // execute
        String someObjectJson = JsonConverter.toJson(someObject);
        // verify
        assertEquals(someObjectJson, "{\"j\":1,\"idObject\":20}");
    }

    @Test
    void convertsJsonIntoObject() throws Exception {
        //setup
        String someObjectJson = "{\"j\":999,\"idObject\":555}";
        // execute
        SomeClassWithIdProperty someObjectJsonDeSerialized = JsonConverter.fromJson(someObjectJson, SomeClassWithIdProperty.class);
        // verify
        assertThat(someObjectJsonDeSerialized.j).isEqualTo(999);
        assertEquals(someObjectJsonDeSerialized.idObject, new ExampleDomainId(555L));
    }

    @SuppressWarnings("WeakerAccess")
    private static class Person {
        private final ExampleDomainId id;
        private final String name;
        public Person(@JsonProperty("id") ExampleDomainId id, @JsonProperty("name") String name) {
            this.name = name;
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public ExampleDomainId getId() {
            return id;
        }
    }

    @Test
    void convertsJsonIntoObject_withoutGetterAndSetter() throws Exception {
        //setup
        String jsonObject = "{\"id\":30,\"name\":\"bozo\"}";
        // execute
        Person deserializedJsonObject = JsonConverter.fromJson(jsonObject, Person.class);
        // verify
        assertEquals(deserializedJsonObject.getId(), new ExampleDomainId(30L));
        assertEquals(deserializedJsonObject.getName(), "bozo");
    }

}

class JsonConverter {

    private static ObjectMapper mapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(DomainId.class, new DomainIdJsonSerializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }

    static String toJson(Object obj) throws JsonProcessingException {
        return mapper().writeValueAsString(obj);
    }

    static <T> T fromJson(String jsonString, Class<T> clazzBase) throws IOException {
        return mapper().readValue(jsonString, clazzBase);
    }

}