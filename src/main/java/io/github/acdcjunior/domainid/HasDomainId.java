package io.github.acdcjunior.domainid;

public interface HasDomainId<ID extends DomainId> extends Comparable<HasDomainId<ID>> {

    ID getId();

    @Override
    @SuppressWarnings("NullableProblems")
    default int compareTo(HasDomainId<ID> idHasDomainId) {
        if (this.getId() == null || idHasDomainId == null) {
            return 1;
        } else {
            return this.getId().compareTo(idHasDomainId.getId());
        }
    }

}
