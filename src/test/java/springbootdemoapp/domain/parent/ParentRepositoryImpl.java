package springbootdemoapp.domain.parent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


@Component
public class ParentRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ParentRepository myEntityRepository;

    @SuppressWarnings({"unused", "WeakerAccess"})
    public List<Parent> exampleUsingEntityManager(ParentId id) {
        String hql = "SELECT f FROM Parent f WHERE f.id = :id";
        TypedQuery<Parent> query = entityManager.createQuery(hql, Parent.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @SuppressWarnings("unused")
    public List<Parent> exampleUsingOwnInterfaceMethod(ParentId id) {
        List<Parent> es = exampleUsingEntityManager(id);
        es.addAll(myEntityRepository.findByNomeContains("Deadpool"));
        es.add(myEntityRepository.findById(new ParentId(20003L)).get());
        return es;
    }

}