package springbootdemoapp;

import org.springframework.stereotype.Component;

@Component
public class SomeBean {

    public String getSomething() {
        return "something!";
    }

}
