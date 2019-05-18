package springbootdemoapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class SpringBootDemoApplicationTest {

    @Autowired
    private SomeBean someBean;

    @Test
    void contextLoads() {
        assertThat(someBean.getSomething()).isEqualTo("something!");
    }

}