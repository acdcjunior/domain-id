package io.github.acdcjunior.domainid.serializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.acdcjunior.domainid.DomainId;
import io.github.acdcjunior.domainid.serializer.DomainIdJsonDeserializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


class DomainIdJsonDeserializerTest {

    @SuppressWarnings("WeakerAccess")
    public static class AnotherExampleDomainId extends DomainId {
        public AnotherExampleDomainId(long id) {
            super(id);
        }
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    private static class SomeClassWithIdProperty {
        public int j = 1;
        public AnotherExampleDomainId idObject = new AnotherExampleDomainId(20L);
    }

    @Test
    void convertsJsonLongIntoObject() throws Exception {
        //setup
        String someObjectJson = "{\"j\":999,\"idObject\":555}";
        // execute
        SomeClassWithIdProperty someObjectJsonDeSerialized = fromJson(someObjectJson);
        // verify
        assertThat(someObjectJsonDeSerialized.j).isEqualTo(999);
        assertEquals(someObjectJsonDeSerialized.idObject, new AnotherExampleDomainId(555L));
    }

    @Test
    @DisplayName("error: cant deserialize object into id if the deserializer is not used")
    void convertsJsonObjectIntoObject() {
        //setup
        String someObjectJson = "{\"j\":999,\"idObject\":{\"id\":777,\"whatever\":{\"stuff\":\"123\"}}}";
        // execute
        Assertions.assertThatThrownBy(() -> fromJson(someObjectJson))
        // verify
        .hasMessageContaining("cannot deserialize from Object value (no delegate- or property-based Creator)");
    }

    @Test
    void convertsJsonLongIntoObject_fromJsonWithDeserializer() throws Exception {
        //setup
        String someObjectJson = "{\"j\":999,\"idObject\":555}";
        // execute
        SomeClassWithIdProperty someObjectJsonDeSerialized = fromJsonWithDeserializer(someObjectJson);
        // verify
        assertThat(someObjectJsonDeSerialized.j).isEqualTo(999);
        assertEquals(someObjectJsonDeSerialized.idObject, new AnotherExampleDomainId(555L));
    }

    @Test
    void convertsJsonLongIntoObject_fromJsonWithDeserializer_idGreaterThanMaxInt() throws Exception {
        //setup
        String someObjectJson = "{\"j\":999,\"idObject\":2147483648}";
        // execute
        SomeClassWithIdProperty someObjectJsonDeSerialized = fromJsonWithDeserializer(someObjectJson);
        // verify
        assertThat(someObjectJsonDeSerialized.j).isEqualTo(999);
        assertEquals(someObjectJsonDeSerialized.idObject, new AnotherExampleDomainId(2147483648L));
    }

    @Test
    void convertsJsonObjectIntoObject_fromJsonWithDeserializer() throws Exception {
        //setup
        String someObjectJson = "{\"j\":999,\"idObject\":{\"id\":777,\"whatever\":{\"stuff\":\"123\"}}}";
        // execute
        SomeClassWithIdProperty someObjectJsonDeSerialized = fromJsonWithDeserializer(someObjectJson);
        // verify
        assertThat(someObjectJsonDeSerialized.j).isEqualTo(999);
        assertEquals(someObjectJsonDeSerialized.idObject, new AnotherExampleDomainId(777L));
    }

    @Test
    void convertsJsonObjectIntoObject_fromJsonWithDeserializer_idGreaterThanMaxInt() throws Exception {
        //setup
        String someObjectJson = "{\"idObject\":{\"whatever1\":{\"stuff\":\"123\"},\"whatever2\":\"two\",\"id\":2147483648,\"whatever3\":{\"stuff\":\"123\"}},\"j\":999}";
        // execute
        SomeClassWithIdProperty someObjectJsonDeSerialized = fromJsonWithDeserializer(someObjectJson);
        // verify
        assertThat(someObjectJsonDeSerialized.j).isEqualTo(999);
        assertEquals(someObjectJsonDeSerialized.idObject, new AnotherExampleDomainId(2147483648L));
    }

    @Test
    @DisplayName("Error: object id not long/int")
    void t1() {
        //setup
        String someObjectJson = "{\"j\":999,\"idObject\":{\"id\":123.45}}";
        // execute
        Assertions.assertThatThrownBy(() -> {
            SomeClassWithIdProperty x = fromJsonWithDeserializer(someObjectJson);
            System.out.println(x.idObject);
        })
        // verify
        .hasMessageContaining("Field \"id\" of the object value of field \"idObject\" should be a long/int.");
    }

    @Test
    @DisplayName("cod instead of id")
    void t2() throws Exception {
        //setup
        String someObjectJson = "{\"j\":999,\"idObject\":{\"cod\":777}}";
        // execute
        SomeClassWithIdProperty someObjectJsonDeSerialized = fromJsonWithDeserializer(someObjectJson);
        // verify
        assertEquals(someObjectJsonDeSerialized.idObject, new AnotherExampleDomainId(777L));
    }

    @Test
    @DisplayName("Error: object cod not long")
    void t3() {
        //setup
        String someObjectJson = "{\"j\":999,\"idObject\":{\"cod\":\"str\"}}";
        // execute
        Assertions.assertThatThrownBy(() -> fromJsonWithDeserializer(someObjectJson))
        // verify
        .hasMessageContaining("Field \"cod\" of the object value of field \"idObject\" should be a long/int.");
    }

    @Test
    @DisplayName("Error: not id, nor cod present")
    void t4() {
        //setup
        String someObjectJson = "{\"j\":999,\"idObject\":{\"w00t\":777},\"id\":\"whatever\"}";
        // execute
        Assertions.assertThatThrownBy(() -> fromJsonWithDeserializer(someObjectJson))
        // verify
        .hasMessageContaining("Expected a field named \"id\" or \"cod\" in the object value of field \"idObject\", but none was found.");
    }

    static SomeClassWithIdProperty fromJson(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        objectMapper.registerModule(module);
        return objectMapper.readValue(jsonString, SomeClassWithIdProperty.class);
    }

    static SomeClassWithIdProperty fromJsonWithDeserializer(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(AnotherExampleDomainId.class, new DomainIdJsonDeserializer<>(AnotherExampleDomainId.class));
        objectMapper.registerModule(module);
        return objectMapper.readValue(jsonString, SomeClassWithIdProperty.class);
    }

}