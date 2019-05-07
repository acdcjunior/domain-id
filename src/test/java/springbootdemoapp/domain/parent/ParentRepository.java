package springbootdemoapp.domain.parent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springbootdemoapp.domain.sibling.SiblingId;

import java.util.List;


@Repository
public interface ParentRepository extends JpaRepository<Parent, ParentId> {

    List<Parent> findBySibling(SiblingId sibling);

    List<Parent> findByNomeContains(String nome);

    @Query("SELECT f FROM Parent f WHERE f.id = 20003")
    List<Parent> exampleUsingJPQL();

    @Query(value = "SELECT * FROM MYDOMAINID.PARENT f WHERE f.id = 20003", nativeQuery = true)
    List<Parent> exampleUsingNativeSQL();

    List<Parent> exampleUsingEntityManager(ParentId id);

    List<Parent> exampleUsingOwnInterfaceMethod(ParentId id);

}