package springbootdemoapp.domain.parent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbootdemoapp.domain.parent.child.Child;
import springbootdemoapp.domain.parent.child.ChildId;
import springbootdemoapp.domain.parent.child.GrandChild;
import springbootdemoapp.domain.parent.child.GrandChildId;
import springbootdemoapp.domain.sibling.SiblingId;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@SuppressWarnings("OptionalGetWithoutIsPresent")
class ParentRepositoryTest {

    @Autowired
    private ParentRepository parentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void findById() {
        ParentId parentId = new ParentId(20001L);
        Parent parent = parentRepository.findById(parentId).get();
        assertThat(parent.getId()).isEqualTo(parentId);
        assertThat(parent.getNome()).isEqualTo("Star Wars");
        assertThat(parent.getSibling()).isEqualTo(new SiblingId(11L));

        assertThat(parent.getChildren()).hasSize(2);
        List<Child> children = new ArrayList<>(parent.getChildren());
        assertThat(children.get(0).getId()).isEqualTo(new ChildId(50001));
        assertThat(children.get(0).getAnotherChild()).isEqualTo(null);
        assertThat(children.get(1).getId()).isEqualTo(new ChildId(50002));
        assertThat(children.get(1).getAnotherChild()).isEqualTo(new ChildId(50001));

        assertThat(children.get(0).getGrandChildren()).hasSize(1);
        GrandChild firstGrandChild = children.get(0).getGrandChildren().iterator().next();
        assertThat(firstGrandChild.getId()).isEqualTo(new GrandChildId(60001L));
    }

    @Test
    void findBySibling() {
        List<Parent> parents = parentRepository.findBySibling(new SiblingId(33L));
        assertThat(parents).hasSize(1);
        assertThat(parents.get(0).getId()).isEqualTo(new ParentId(20003L));
    }

    @Test
    void exampleUsingJPQL() {
        List<Parent> parents = parentRepository.exampleUsingJPQL();
        assertThat(parents).hasSize(1);
        assertThat(parents.get(0).getId()).isEqualTo(new ParentId(20003L));
    }

    @Test
    void exampleUsingNativeSQL() {
        List<Parent> parents = parentRepository.exampleUsingNativeSQL();
        assertThat(parents).hasSize(1);
        assertThat(parents.get(0).getId()).isEqualTo(new ParentId(20003L));
    }

    @Test
    void exampleUsingEntityManager() {
        List<Parent> parents = parentRepository.exampleUsingEntityManager(new ParentId(20002L));
        assertThat(parents).hasSize(1);
        assertThat(parents.get(0).getId()).isEqualTo(new ParentId(20002L));
    }

    @Test
    void exampleUsingOwnInterfaceMethod() {
        List<Parent> parents = parentRepository.exampleUsingOwnInterfaceMethod(new ParentId(20002L));
        assertThat(parents).hasSize(3);
        assertThat(parents.get(0).getId()).isEqualTo(new ParentId(20002L));
    }

    @Test
    @DirtiesContext
    void save() {
        /// setup
        Parent parent = new Parent();
        {
            parent.setNome("NewName");
            parent.setSibling(new SiblingId(99L));

            Set<Child> children = new TreeSet<>();
            parent.setChildren(children);

            Child child = new Child();
            children.add(child);
            child.setName("1.1");
            child.setAnotherChild(new ChildId(50001));

            Set<GrandChild> grandChildren = new TreeSet<>();
            child.setGrandChildren(grandChildren);
            GrandChild grandChild = new GrandChild();
            grandChildren.add(grandChild);
            grandChild.setName("1.1.1");

            Child child2 = new Child();
            children.add(child2);
            child2.setName("1.2");
        }

        /// execute
        Parent parentSalvo = parentRepository.save(parent);
        entityManager.flush();
        /// verify
        assertThat(parentSalvo.getId()).isEqualTo(new ParentId(20004L));
        assertThat(parentSalvo.getNome()).isEqualTo("NewName");
        assertThat(parentSalvo.getSibling()).isEqualTo(new SiblingId(99L));

        assertThat(parent.getChildren()).hasSize(2);
        List<Child> children = new ArrayList<>(parent.getChildren());
        assertThat(children.get(0).getId()).isEqualTo(new ChildId(50003));
        assertThat(children.get(0).getAnotherChild()).isEqualTo(new ChildId(50001));
        assertThat(children.get(0).getName()).isEqualTo("1.1");
        assertThat(children.get(1).getId()).isEqualTo(new ChildId(50004));
        assertThat(children.get(1).getName()).isEqualTo("1.2");

        assertThat(children.get(0).getGrandChildren()).hasSize(1);
        GrandChild firstGrandChild = children.get(0).getGrandChildren().iterator().next();
        assertThat(firstGrandChild.getId()).isEqualTo(new GrandChildId(60002L));
        assertThat(firstGrandChild.getName()).isEqualTo("1.1.1");
    }

}