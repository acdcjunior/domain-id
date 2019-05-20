package springbootapp;

import io.github.acdcjunior.domainid.DomainIdJacksonSerializer;
import io.github.acdcjunior.domainid.StringToDomainIdConverterFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackageClasses = {
                ExampleController.class,
                DomainIdJacksonSerializer.class,
                StringToDomainIdConverterFactory.class
        }
)
public class SpringBootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }

}
