package io.github.acdcjunior.domainid.linked;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import io.github.acdcjunior.domainid.DomainId;
import io.github.acdcjunior.domainid.DomainIdJsonDeserializer;
import io.github.acdcjunior.domainid.DomainIdJsonSerializer;

import java.io.IOException;


/**
 * This deserializer "consumes" the "_links" field right after the "id" field.<br>
 * This deserializer only makes sense if you have configured jackson to NOT ignore unknown fields (like _links), because,
 * if you did, there's no need to consume it (so just use {@link DomainIdJsonDeserializer}), as jackson won't complain
 * about the hanging "_links" anyway.
 *
 * @param <ID> The id class, that extends DomainId.
 */
public class LinkedDomainIdJsonDeserializer<ID extends DomainId> extends DomainIdJsonDeserializer<ID> {

    public LinkedDomainIdJsonDeserializer(Class<ID> handledType) {
        super(assertHandledTypeIsAnnotatedWithLinkedDomainId(handledType));
    }

    private static <ID extends DomainId> Class<ID> assertHandledTypeIsAnnotatedWithLinkedDomainId(Class<ID> handledType) {
        if (handledType.getAnnotation(LinkedDomainId.class) == null) {
            throw new IllegalArgumentException("LinkedDomainIdSerializer should only be used with DomainId classes" +
                    " annotated with @LinkedDomainId. If your DomainId is not annotated with it, you don't need" +
                    " to specify any deserializer, Jackson will already automatically know how to deserialize it.");
        }
        return handledType;
    }

    @Override
    protected Long deserializeIdAsLong(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Long id = deserializationContext.readValue(jsonParser, Long.class);

        String idFieldName = jsonParser.getCurrentName();
        if (DomainIdJsonSerializer.UNWRAPPED_ID_FIELDS.contains(idFieldName) && "_links".equals(jsonParser.nextFieldName())) {
            consumeToken(jsonParser, deserializationContext, JsonToken.START_OBJECT, idFieldName); // {
            consumeToken(jsonParser, deserializationContext, JsonToken.FIELD_NAME, idFieldName); // "self"
            consumeToken(jsonParser, deserializationContext, JsonToken.START_OBJECT, idFieldName); // {
            consumeToken(jsonParser, deserializationContext, JsonToken.FIELD_NAME, idFieldName); // "href"
            consumeToken(jsonParser, deserializationContext, JsonToken.VALUE_STRING, idFieldName); // "<url value>"
            consumeToken(jsonParser, deserializationContext, JsonToken.END_OBJECT, idFieldName); // }
            consumeToken(jsonParser, deserializationContext, JsonToken.END_OBJECT, idFieldName); // }
        }

        return id;
    }

    private void consumeToken(JsonParser jsonParser, DeserializationContext deserializationContext, JsonToken expectedToken, final String idFieldName) throws IOException {
        if (jsonParser.nextToken() != expectedToken) {
            deserializationContext.reportWrongTokenException(
                handledType, expectedToken,
                "\nThe \"_links\" property immediately after an \"" + idFieldName + "\" field should be an object of the format '{\"self\":{\"href\":\"http://some.url/path/123\"}}'," +
                " but was not.\n" +
                "Problem at token: " + deserializationContext.readValue(jsonParser, String.class) + ".\n" +
                "Example of object with a well-formed \"_links\" property: " +
                "{\"name\":\"name-value\",\"" + idFieldName + "\":878,\"_links\":{\"self\":{\"href\":\"http://some.com/linked/878\"}},\"street\":\"street-value\"}\n" +
                "If your object should have a \"_links\" property of a different format, you'll have to resort to a custom deserializer."
            );
        }
    }

}