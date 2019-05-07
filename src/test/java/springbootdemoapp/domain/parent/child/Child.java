package springbootdemoapp.domain.parent.child;

import io.github.acdcjunior.domainid.hibernate.sequence.DomainIdSequenceStyleGenerator;
import org.hibernate.annotations.GenericGenerator;
import springbootdemoapp.domain.parent.ParentId;
import springbootdemoapp.domain.sibling.SiblingId;

import javax.persistence.*;
import java.util.Set;
import java.util.concurrent.CompletableFuture;


@Entity
@Table(schema = "MYDOMAINID", name = "CHILD")
@GenericGenerator(
        name = "SEQ_CHILD",
        strategy = DomainIdSequenceStyleGenerator.SEQUENCE,
        parameters = @org.hibernate.annotations.Parameter(name = "sequence_name", value = "MYDOMAINID.SEQ_CHILD")
)
public class Child implements Comparable<Child> {

    @Id
    @GeneratedValue(generator = "SEQ_CHILD", strategy = GenerationType.SEQUENCE)
    @Column(name = "KEY")
    private ChildId id;

    @Column
    private String name;

    @Column(name = "ANOTHER_CHILD_KEY")
    private ChildId anotherChild;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "PARENT_CHILD_KEY", nullable = false)
    private Set<GrandChild> grandChildren;

    public ChildId getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ChildId getAnotherChild() {
        return anotherChild;
    }
    public void setAnotherChild(ChildId anotherChild) {
        this.anotherChild = anotherChild;
    }

    public Set<GrandChild> getGrandChildren() {
        return grandChildren;
    }
    public void setGrandChildren(Set<GrandChild> grandChildren) {
        this.grandChildren = grandChildren;
    }

    @Override
    public int compareTo(Child child) {
        if (this.id == null || child == null) return 1;
        return this.id.compareTo(child.id);
    }

}
