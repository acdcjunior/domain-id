package io.github.acdcjunior.domainid.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.acdcjunior.domainid.DomainId;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.stereotype.Component;

import java.io.IOException;


@JsonComponent
@Component
public class DomainIdJsonSerializer extends JsonSerializer<DomainId> {

    @Override
    public void serialize(DomainId domainId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(domainId.toLong());
    }

}
