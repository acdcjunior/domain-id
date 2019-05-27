package springbootapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeId;

public class ExampleResource {

    private ExampleResourceId id;
    private String name;
    private ExampleResourceId otherResource;

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

    public ExampleResourceId getOtherResource() {
        return otherResource;
    }
    public void setOtherResource(ExampleResourceId otherResource) {
        this.otherResource = otherResource;
    }

}
