package springbootapp.linkeddomainid;

import io.github.acdcjunior.domainid.DomainId;
import io.github.acdcjunior.domainid.LinkedDomainId;


@LinkedDomainId("${my.exampleresource.baseUrl}api/resources/#")
public class LinkedResourceId extends DomainId {
    public LinkedResourceId(long id) {
        super(id);
    }
}
