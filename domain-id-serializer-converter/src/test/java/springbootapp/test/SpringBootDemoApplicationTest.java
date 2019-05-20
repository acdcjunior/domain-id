package springbootapp.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import springbootapp.ExampleResource;
import springbootapp.ExampleResourceId;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootDemoApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void get() {
        ResponseEntity<ExampleResourceId> entity = this.restTemplate.getForEntity("/example-get/112233/stuff/445566", ExampleResourceId.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(new ExampleResourceId(557799));
    }

    @Test
    void post() {
        String requestJson = "{\"id\":123,\"name\":\"Bozo\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        ExampleResource response = restTemplate.postForObject("/example-post", entity, ExampleResource.class);
        System.out.println(response);
        assertThat(response.getId()).isEqualTo(new ExampleResourceId(123));
    }

}
