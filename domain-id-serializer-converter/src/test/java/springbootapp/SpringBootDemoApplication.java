package springbootapp;

import io.github.acdcjunior.domainid.serializer.DomainIdsSerializersAndConverters;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(
        scanBasePackageClasses = {
                ExampleController.class,
                DomainIdsSerializersAndConverters.class
        }
)
public class SpringBootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }

}
