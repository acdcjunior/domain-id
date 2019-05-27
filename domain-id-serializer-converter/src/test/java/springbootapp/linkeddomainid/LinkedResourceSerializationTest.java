package springbootapp.linkeddomainid;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@JsonTest
@ExtendWith(SpringExtension.class)
class LinkedResourceSerializationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serialization() throws Exception {
        LinkedResource linkedResource = new LinkedResource();
        linkedResource.setId(new LinkedResourceId(123));
        linkedResource.setName("Bozo");
        linkedResource.setOtherResource(new LinkedResourceId(4444));
        String json = objectMapper.writeValueAsString(linkedResource);

        Assertions.assertThat(json).isEqualToIgnoringWhitespace("{" +
            "\"id\":123," +
            "\"_links\":{\"self\":{\"href\":\"http://example-resource.com/api/resources/123\"}}," +
            "\"name\":\"Bozo\"," +
            "\"otherResource\":{" +
            "\"id\":4444," +
            "\"_links\":{\"self\":{\"href\":\"http://example-resource.com/api/resources/4444\"}}" +
            "}" +
        "}");
    }

    @Test
    void deserialization() throws Exception {
        String json = "{\"id\":123,\"name\":\"Bozo\",\"otherResource\":9933}";
        LinkedResource linkedResource = objectMapper.readValue(json, LinkedResource.class);

        assertEquals(new LinkedResourceId(123), linkedResource.getId());
        assertEquals(new LinkedResourceId(9933), linkedResource.getOtherResource());
    }

}
