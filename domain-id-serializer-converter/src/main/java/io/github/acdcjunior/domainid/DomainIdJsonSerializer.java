package io.github.acdcjunior.domainid;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@JsonComponent
@Component
public class DomainIdJsonSerializer extends JsonSerializer<DomainId> implements ContextualSerializer {

    static final List<String> UNWRAPPED_ID_FIELDS = Arrays.asList("id", "cod");

    private RequiredPlaceholderResolver environment = (s) -> s;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment::resolveRequiredPlaceholders;
    }

    private interface RequiredPlaceholderResolver {
        String resolveRequiredPlaceholders(String text) throws IllegalArgumentException;
    }

    @Override
    public void serialize(DomainId domainId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(domainId.toLong());
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        if (property == null) {
            return this;
        }
        LinkedDomainId linkedDomainId = property.getType().getRawClass().getAnnotation(LinkedDomainId.class);
        if (linkedDomainId == null) {
            return this;
        }
        String href = environment.resolveRequiredPlaceholders(linkedDomainId.value());
        if (UNWRAPPED_ID_FIELDS.contains(property.getName())) {
            return new DomainIdAsObjectSerializers.UnwrappedDomainIdSerializer(href);
        }
        return new DomainIdAsObjectSerializers.WrappedDomainIdSerializer(href);
    }

}

class DomainIdAsObjectSerializers {

    private static void writeLinksSelfHrefField(JsonGenerator jsonGenerator, String value) throws IOException {
        Map<String, Object> _links = new HashMap<>();
        Map<String, Object> self = new HashMap<>();
        self.put("href", value);
        _links.put("self", self);
        jsonGenerator.writeObjectField("_links", _links);
    }

    static class WrappedDomainIdSerializer extends JsonSerializer<DomainId> {
        private final String href;
        WrappedDomainIdSerializer(String href) {
            this.href = href;
        }
        @Override
        public void serialize(DomainId domainId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("id", domainId.toLong());
            writeLinksSelfHrefField(jsonGenerator, href.replace("#", domainId.toString()));
            jsonGenerator.writeEndObject();
        }
    }

    static class UnwrappedDomainIdSerializer extends JsonSerializer<DomainId> {
        private final String href;
        UnwrappedDomainIdSerializer(String href) {
            this.href = href;
        }
        @Override
        public void serialize(DomainId domainId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(domainId.toLong());
            writeLinksSelfHrefField(jsonGenerator, href.replace("#", domainId.toString()));
        }
        @Override
        public boolean isUnwrappingSerializer() {
            return true;
        }
    }
}