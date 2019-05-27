package springbootapp.linkeddomainid;

public class LinkedResource {

    private LinkedResourceId id;
    private String name;
    private LinkedResourceId otherResource;

    public LinkedResourceId getId() {
        return id;
    }
    public void setId(LinkedResourceId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public LinkedResourceId getOtherResource() {
        return otherResource;
    }
    public void setOtherResource(LinkedResourceId otherResource) {
        this.otherResource = otherResource;
    }

}
