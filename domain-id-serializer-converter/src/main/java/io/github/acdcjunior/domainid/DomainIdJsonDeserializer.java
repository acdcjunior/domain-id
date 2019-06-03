package io.github.acdcjunior.domainid;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;


/**
 * Deserializer that enables deserialization into IDs of JSON like:<br>
 * <pre><code>
 * {
 *     "some_field": "some_value",
 *     "myEntityId": {
 *         "any_other_fields": "will be ignored",
 *         "id": 12345,
 *         "either_before_or_after_the_id": "will all be ignored"
 *     },
 *     "some_other_field": "this is not ignored, because this is not part of the id field's value object"
 * }
 * </code></pre>
 * @param <ID> The id class, that extends DomainId.
 */
public class DomainIdJsonDeserializer<ID extends DomainId> extends JsonDeserializer<ID> {

    private final Class<ID> handledType;

    public DomainIdJsonDeserializer(Class<ID> handledType) {
        this.handledType = handledType;
    }

    @Override
    public ID deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return DomainId.newInstance(handledType, extractIdValue(jsonParser, deserializationContext));
    }

    private Long extractIdValue(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if (jsonParser.currentToken() == JsonToken.START_OBJECT) {
            String idObjectFieldName = jsonParser.currentName();

            String fieldName;
            while ((fieldName = jsonParser.nextFieldName()) != null && !fieldName.equals("id") && !fieldName.equals("cod")) {
                jsonParser.nextToken(); // move cursor to fieldName's value
                deserializationContext.readValue(jsonParser, Object.class); // consume it all
            }
            if (fieldName == null) { // reached end of object
                return reportWrongToken(deserializationContext, "Expected a field named \"id\" or \"cod\" in the object value of field \"%s\", but none was found.", idObjectFieldName);
            }
            JsonToken idValueToken = jsonParser.nextToken();
            if (idValueToken != JsonToken.VALUE_NUMBER_INT) {
                return reportWrongToken(deserializationContext, "Field \"%s\" of the object value of field \"%s\" should be a long/int.", fieldName, idObjectFieldName);
            }
            Long id = deserializationContext.readValue(jsonParser, Long.class);

            // consume every other prop until idObject ends
            jsonParser.nextToken();
            deserializationContext.readValue(jsonParser, Object.class);

            return id;
        } else {
            return deserializationContext.readValue(jsonParser, Long.class);
        }
    }

    private Long reportWrongToken(DeserializationContext deserializationContext, String msg, Object... msgArgs) throws JsonMappingException {
        deserializationContext.reportWrongTokenException(handledType, JsonToken.VALUE_NUMBER_INT, msg, msgArgs);
        return null;
    }

}
