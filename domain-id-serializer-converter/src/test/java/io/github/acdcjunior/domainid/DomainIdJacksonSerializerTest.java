package io.github.acdcjunior.domainid;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.acdcjunior.domainid.linked.LinkedDomainId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
        assertEquals(someObjectJsonDeSerialized.j, 999);
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

    @LinkedDomainId("http://example.com/linked/#")
    private static class ExampleLinkedDomainId extends DomainId {
        ExampleLinkedDomainId(long id) {
            super(id);
        }
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    private static class SomeClassWithIdLinkedProperty {
        public int j = 1;
        public ExampleDomainId otherId = new ExampleDomainId(11);
        public ExampleLinkedDomainId id = new ExampleLinkedDomainId(22);
        public ExampleLinkedDomainId otherLinkedId = new ExampleLinkedDomainId(33);
    }

    @Nested
    @DisplayName("Linked Domain ID serialization")
    class LinkedDomainIdSerialization {

        @Test
        void convertsObjectIntoJson() throws Exception {
            // setup
            DomainIdJacksonSerializerTest.SomeClassWithIdLinkedProperty someObject = new DomainIdJacksonSerializerTest.SomeClassWithIdLinkedProperty();
            // execute
            String someObjectJson = JsonConverter.toJson(someObject);
            // verify
            Assertions.assertThat(someObjectJson).isEqualToIgnoringWhitespace(
                    "{\n" +
                    "  \"j\":1,\n" +
                    "  \"otherId\":11,\n" +
                    "  \"id\":22,\n" +
                    "  \"_links\":{\"self\":{\"href\":\"http://example.com/linked/22\"}},\n" +
                    "  \"otherLinkedId\":{\n" +
                    "    \"id\":33,\n" +
                    "    \"_links\":{\"self\":{\"href\":\"http://example.com/linked/33\"}}\n" +
                    "  }\n" +
                    "}"
            );
        }

        @Test
        void convertsJsonIntoObject() throws Exception {
            //setup
            String someObjectJson = "{\"j\":999,\"otherId\":333,\"id\":444,\"otherLinkedId\":555}";
            // execute
            DomainIdJacksonSerializerTest.SomeClassWithIdLinkedProperty someObjectJsonDeSerialized = JsonConverter.fromJson(someObjectJson, DomainIdJacksonSerializerTest.SomeClassWithIdLinkedProperty.class);
            // verify
            assertEquals(someObjectJsonDeSerialized.j, 999);
            assertEquals(someObjectJsonDeSerialized.otherId, new ExampleDomainId(333));
            assertEquals(someObjectJsonDeSerialized.id, new ExampleLinkedDomainId(444));
            assertEquals(someObjectJsonDeSerialized.otherLinkedId, new ExampleLinkedDomainId(555));
        }

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