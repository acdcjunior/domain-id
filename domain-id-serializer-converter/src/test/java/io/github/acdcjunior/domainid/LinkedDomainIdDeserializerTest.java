package io.github.acdcjunior.domainid;


import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LinkedDomainIdDeserializerTest {

    @Nested
    @DisplayName("deserialization of DomainId when field's name is 'id'/'cod'")
    class IdOfClass {

        @Test
        @DisplayName("works fine when there's no '_links'")
        void convertsIdJsonIntoObject() throws Exception {
            //setup
            String someObjectJson = "{\"name\":\"name-value\",\"id\":878,\"street\":\"street-value\"}";
            // execute
            verifyDeserialization(someObjectJson);
        }

        @Test
        @DisplayName("consumes '_links' prop when it is right after the 'id' and has a specific format (ignoring whitespace)")
        void convertsLinkedJsonIntoObject() throws Exception {
            //language=JSON
            verifyDeserialization("{\n" +
                    "\"name\":\n" +
                    "\"name-value\",\n" +
                    "\"id\":\n" +
                    "878,\n" +
                    "\"_links\":\n" +
                    "{\n" +
                    "\"self\":\n" +
                    "{\n" +
                    "\"href\":\n" +
                    "\"http://some.com/linked/878\"\n" +
                    "}\n" +
                    "},\n" +
                    "\"street\":\n" +
                    "\"street-value\"\n" +
                    "}");
            //language=JSON
            verifyDeserialization("{\"name\":\"name-value\",\"id\":878,\"_links\":{\"self\":{\"href\":\"http://some.com/linked/878\"}},\"street\":\"street-value\"}");
            //language=JSON
            verifyDeserialization("{" +
                    "\"name\":\"name-value\"," +
                    "\"id\":878," +
                    "\"_links\":{\"self\":{\"href\":\"http://some.com/linked/878\"}},\n" +
                    "\"street\":\"street-value\"" +
                    "}");
        }

        @Test
        @DisplayName("'_links' as last prop")
        void convertsLinkedJsonIntoObject4() throws Exception {
            //setup
            //language=JSON
            String someObjectJson =
                    "{" +
                            "\"name\":\"name-value\"," +
                            "\"street\":\"street-value\"," +
                            "\"id\":878," +
                            "\"_links\":{\"self\":{\"href\":\"http://some.com/linked/878\"}}" +
                            "}";
            // execute
            verifyDeserialization(someObjectJson);
        }

        @Test
        @DisplayName("'id' as first prop")
        void convertsLinkedJsonIntoObject5() throws Exception {
            //setup
            //language=JSON
            String someObjectJson =
                    "{" +
                            "\"id\":878," +
                            "\"_links\":{\"self\":{\"href\":\"http://some.com/linked/878\"}}," +
                            "\"name\":\"name-value\"," +
                            "\"street\":\"street-value\"" +
                            "}";
            verifyDeserialization(someObjectJson);
        }

        private void verifyDeserialization(String someObjectJson) throws IOException {
            // execute
            OneWithAaa someObjectJsonDeSerialized = TestJsonDeserializer.fromJsonAaaLinkedDomainId(someObjectJson, OneWithAaa.class);
            OneWithAaa someObjectJsonDeSerialized2 = TestJsonDeserializer.fromJsonAaaLinkedDomainId2(someObjectJson, OneWithAaa.class);
            // verify
            assertEquals(someObjectJsonDeSerialized.name, "name-value");
            assertEquals(someObjectJsonDeSerialized.id, new AaaLinkedId(878));
            assertEquals(someObjectJsonDeSerialized.street, "street-value");
            assertEquals(someObjectJsonDeSerialized2.name, "name-value");
            assertEquals(someObjectJsonDeSerialized2.id, new AaaLinkedId(878));
            assertEquals(someObjectJsonDeSerialized2.street, "street-value");
        }

        @Test
        @DisplayName("using 'cod' as id field name")
        void convertsLinkedJsonIntoObject6() throws Exception {
            //setup
            //language=JSON
            String someObjectJson =
                    "{" +
                            "\"name\":\"name-value\"," +
                            "\"cod\":878," +
                            "\"_links\":{\"self\":{\"href\":\"http://some.com/linked/878\"}},\n" +
                            "\"street\":\"street-value\"" +
                            "}";
            // execute
            TwoWithAaa someObjectJsonDeSerialized = TestJsonDeserializer.fromJsonAaaLinkedDomainId(someObjectJson, TwoWithAaa.class);
            TwoWithAaa someObjectJsonDeSerialized2 = TestJsonDeserializer.fromJsonAaaLinkedDomainId2(someObjectJson, TwoWithAaa.class);
            // verify
            assertEquals(someObjectJsonDeSerialized.name, "name-value");
            assertEquals(someObjectJsonDeSerialized.cod, new AaaLinkedId(878));
            assertEquals(someObjectJsonDeSerialized.street, "street-value");
            assertEquals(someObjectJsonDeSerialized2.name, "name-value");
            assertEquals(someObjectJsonDeSerialized2.cod, new AaaLinkedId(878));
            assertEquals(someObjectJsonDeSerialized2.street, "street-value");
        }

        @Test
        @DisplayName("error when field is 'id' and '_links' doesnt have the expected format")
        void convertsLinkedJsonIntoObject_error() {
            //setup
            //language=JSON
            String someObjectJson =
                    "{" +
                            "\"name\":\"name-value\"," +
                            "\"id\":878," +
                            "\"_links\":\"http://some.com/linked/878\"," +
                            "\"street\":\"street-value\"" +
                            "}";
            verifyDeserializationError(someObjectJson, "Unexpected token", OneWithAaa.class);
        }

        @Test
        @DisplayName("when id field is not 'id' or 'cod', the '_links' prop is not of our concern, so, in this case, jackson should throw an error")
        void convertsLinkedJsonIntoObject_error2() {
            //setup
            //language=JSON
            String someObjectJson =
                    "{" +
                            "\"name\":\"name-value\"," +
                            "\"notAnIdField\":878," +
                            "\"_links\":\"http://some.com/linked/878\"," +
                            "\"street\":\"street-value\"" +
                            "}";
            verifyDeserializationError(someObjectJson, "Unrecognized field \"_links\" ", ThreeWithAaa.class);
        }

        private void verifyDeserializationError(String someObjectJson, String messageContaining, Class<?> clazzBase) {
            Assertions.assertThatThrownBy(() -> {
                // execute
                TestJsonDeserializer.fromJsonAaaLinkedDomainId(someObjectJson, clazzBase);
                // verify
            }).hasMessageContaining(messageContaining);
        }

    }

}

@LinkedDomainId("http://some.com/aaa/#")
class AaaLinkedId extends DomainId {
    public AaaLinkedId(long id) {
        super(id);
    }
}

class AaaLinkedIdDeserializer extends LinkedDomainIdDeserializer<AaaLinkedId>  {
    public AaaLinkedIdDeserializer() {
        super(AaaLinkedId.class);
    }
}

@SuppressWarnings({"unused", "WeakerAccess"})
class OneWithAaa {
    public String name = "name-value";
    public AaaLinkedId id = new AaaLinkedId(22);
    public String street = "street-value";
}

@SuppressWarnings({"unused", "WeakerAccess"})
class TwoWithAaa {
    public String name = "name-value";
    public AaaLinkedId cod = new AaaLinkedId(22);
    public String street = "street-value";
}

@SuppressWarnings({"unused", "WeakerAccess"})
class ThreeWithAaa {
    public String name = "name-value";
    public AaaLinkedId notAnIdField = new AaaLinkedId(22);
    public String street = "street-value";
}


@SuppressWarnings("SameParameterValue")
class TestJsonDeserializer {

    private static ObjectMapper mapper(Class<AaaLinkedId> type, JsonDeserializer<AaaLinkedId> deserializer) {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(type, deserializer);
        objectMapper.registerModule(module);
        return objectMapper;
    }

    static <T> T fromJsonAaaLinkedDomainId(String jsonString, Class<T> clazzBase) throws IOException {
        return mapper(AaaLinkedId.class, new LinkedDomainIdDeserializer<>(AaaLinkedId.class)).readValue(jsonString, clazzBase);
    }
    static <T> T fromJsonAaaLinkedDomainId2(String jsonString, Class<T> clazzBase) throws IOException {
        return mapper(AaaLinkedId.class, new AaaLinkedIdDeserializer()).readValue(jsonString, clazzBase);
    }

}