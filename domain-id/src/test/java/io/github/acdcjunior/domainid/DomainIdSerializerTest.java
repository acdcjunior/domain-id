package io.github.acdcjunior.domainid;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;


public class DomainIdSerializerTest {

    private static class ExampleDomainId extends DomainId {
        ExampleDomainId(Long codExemplo) {
            super(codExemplo);
        }
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    private static class SomeClassWithIdProperty {
        public int j = 1;
        public ExampleDomainId longo = new ExampleDomainId(20L);
    }

    @Test
    public void convertsObjectIntoJson() throws Exception {
        // setup
        SomeClassWithIdProperty objetoQualquer = new SomeClassWithIdProperty();
        // execute
        String objetoQualquerJson = ConversorJson.toJson(objetoQualquer);
        // verify
        assertEquals(objetoQualquerJson, "{\"j\":1,\"longo\":20}");
    }

    @Test
    public void convertsJsonIntoObject() throws Exception {
        //setup
        String objetoQualquerJson = "{\"j\":999,\"longo\":555}";
        // execute
        SomeClassWithIdProperty objetoQualquerFinal = ConversorJson.fromJson(objetoQualquerJson, SomeClassWithIdProperty.class);
        // verify
        assertEquals(objetoQualquerFinal.j, 999);
        assertEquals(objetoQualquerFinal.longo, new ExampleDomainId(555L));

    }

    @SuppressWarnings("WeakerAccess")
    private static class Pessoa {
        private final ExampleDomainId id;
        private final String nome;
        public Pessoa(@JsonProperty("id") ExampleDomainId id, @JsonProperty("nome") String nome) {
            this.nome = nome;
            this.id = id;
        }
        public String getNome() {
            return nome;
        }
        public ExampleDomainId getId() {
            return id;
        }
    }

    @Test
    public void convertsJsonIntoObject_withoutGetterAndSetter() throws Exception {
        //setup
        String objetoQualquerJson = "{\"id\":30,\"nome\":\"bozo\"}";
        // execute
        Pessoa objetoQualquerFinal = ConversorJson.fromJson(objetoQualquerJson, Pessoa.class);
        // verify
        assertEquals(objetoQualquerFinal.getId(), new ExampleDomainId(30L));
        assertEquals(objetoQualquerFinal.getNome(), "bozo");
    }

}

class ConversorJson {

    private static ObjectMapper mapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(DomainId.class, new DomainIdSerializer.DomainIdJsonSerializer());
        module.addDeserializer(DomainId.class, new DomainIdSerializer.DomainIdJsonDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }

    static String toJson(Object obj) throws JsonProcessingException {
        return mapper().writeValueAsString(obj);
    }

    static <T> T fromJson(String jsonString, Class<T> classeBase) throws IOException {
        return mapper().readValue(jsonString, classeBase);
    }

}