package springbootdemoapp.domain.parent.child;

import io.github.acdcjunior.domainid.hibernate.sequence.DomainIdSequenceStyleGenerator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Entity
@Table(schema = "MYDOMAINID", name = "GRANDCHILD")
@GenericGenerator(
        name = "SEQ_GRANDCHILD",
        strategy = DomainIdSequenceStyleGenerator.SEQUENCE,
        parameters = @org.hibernate.annotations.Parameter(name = "sequence_name", value = "MYDOMAINID.SEQ_GRANDCHILD")
)
public class GrandChild implements Comparable<GrandChild> {

    @Id
    @GeneratedValue(generator = "SEQ_GRANDCHILD", strategy = GenerationType.SEQUENCE)
    @Column
    private GrandChildId id;

    @Column
    private String name;

    public GrandChildId getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(GrandChild grandChild) {
        if (this.id == null || grandChild == null) return 1;
        return this.id.compareTo(grandChild.id);
    }

}
