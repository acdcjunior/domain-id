package springbootdemoapp.domain.parent;

import io.github.acdcjunior.domainid.hibernate.sequence.DomainIdSequenceStyleGenerator;
import org.hibernate.annotations.GenericGenerator;
import springbootdemoapp.domain.parent.child.Child;
import springbootdemoapp.domain.parent.child.GrandChild;
import springbootdemoapp.domain.sibling.SiblingId;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;


@Entity
@Table(schema = "MYDOMAINID", name = "PARENT")
public class Parent {

    @Id
    @GenericGenerator(
            name = "SEQ_PARENT",
            strategy = DomainIdSequenceStyleGenerator.SEQUENCE,
            parameters = @org.hibernate.annotations.Parameter(name = "sequence_name", value = "MYDOMAINID.SEQ_PARENT")
    )
    @GeneratedValue(generator = "SEQ_PARENT", strategy = GenerationType.SEQUENCE)
    @Column
    private ParentId id;

    @Column
    private String nome;

    @Column(name = "SIBLING_ID")
    private SiblingId sibling;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "PARENT_ID", nullable = false)
    private Set<Child> children;

    Parent() { }

    public Parent(String nome) {
        this.nome = nome;
    }

    public ParentId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public SiblingId getSibling() {
        return sibling;
    }

    public void setSibling(SiblingId sibling) {
        this.sibling = sibling;
    }

    public Set<Child> getChildren() {
        return children;
    }
    public void setChildren(Set<Child> children) {
        this.children = children;
    }

}
