package springbootapp.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbootapp.ExampleResource;
import springbootapp.ExampleResourceId;

import static org.junit.jupiter.api.Assertions.assertEquals;


@JsonTest
@ExtendWith(SpringExtension.class)
class DomainIdJacksonSerializerJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serialization() throws Exception {
        ExampleResource exampleResource = new ExampleResource();
        exampleResource.setId(new ExampleResourceId(123));
        exampleResource.setName("Bozo");
        exampleResource.setOtherResource(new ExampleResourceId(4444));
        String json = objectMapper.writeValueAsString(exampleResource);

        assertEquals("{\"id\":123,\"name\":\"Bozo\",\"otherResource\":4444}", json);
    }

    @Test
    void deserialization() throws Exception {
        String json = "{\"id\":123,\"name\":\"Bozo\"}";
        ExampleResource user = objectMapper.readValue(json, ExampleResource.class);

        assertEquals(new ExampleResourceId(123), user.getId());
    }

}
