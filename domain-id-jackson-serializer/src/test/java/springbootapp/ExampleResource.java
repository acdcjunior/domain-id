package springbootapp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExampleResource {

    private ExampleResourceId id;
    private String name;

    public ExampleResourceId getId() {
        return id;
    }

    public void setId(ExampleResourceId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
