package io.github.acdcjunior.domainid;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
@SuppressWarnings("WeakerAccess")
public class DomainIdSerializer {

    public static class DomainIdJsonSerializer extends JsonSerializer<DomainId> {

        @Override
        public void serialize(DomainId domainId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(domainId.toLong());
        }

    }

    public static class DomainIdJsonDeserializer extends JsonDeserializer<DomainId> {

        @Override
        public DomainId deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            Long id = deserializationContext.readValue(jsonParser, Long.class);
            @SuppressWarnings("unchecked")
            Class<? extends DomainId> classeId = (Class<? extends DomainId>) handledType();
            return DomainId.instanciar(classeId, id);
        }

    }

}