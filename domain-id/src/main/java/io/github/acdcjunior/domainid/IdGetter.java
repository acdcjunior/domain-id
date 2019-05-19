package io.github.acdcjunior.domainid;


/**
 * Interface used to "convert" an instance of an object into its ID.
 * @param <E> the entity class
 * @param <ID> the type of the entity class' ID
 */
public interface IdGetter<E, ID extends DomainId> {

    /**
     * Gets the ID from a given entity class instance.
     *
     * @param entity the entity class instance.
     * @return the id of the given instance.
     */
    ID getId(E entity);

}
