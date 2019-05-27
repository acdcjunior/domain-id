package io.github.acdcjunior.domainid;

public @interface SerializeDomainIdAsObject {
    String idPropertyName();
    AdditionalProperty[] generateAdditionalProperty();
}
