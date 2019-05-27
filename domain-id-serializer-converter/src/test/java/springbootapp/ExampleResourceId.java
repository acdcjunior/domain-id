package springbootapp;

import io.github.acdcjunior.domainid.AdditionalProperty;
import io.github.acdcjunior.domainid.DomainId;
import io.github.acdcjunior.domainid.DomainIdCustomSerialization;
import io.github.acdcjunior.domainid.SerializeDomainIdAsObject;


@DomainIdCustomSerialization(
        whenFieldNameIsNot = "id",
        generateAdditionalProperty = @AdditionalProperty(name = "_linksX.hrefX.selfX", value = "${some.prop.name}api/main/#")
)
@DomainIdCustomSerialization(
        whenFieldNameIs = "id",
        serializeDomainIdAsObject = @SerializeDomainIdAsObject(
                idPropertyName = "id",
                generateAdditionalProperty = @AdditionalProperty(name = "_linksX.hrefX.selfX", value = "${some.prop.name}apiX/res/#")
        )
)
public class ExampleResourceId extends DomainId {
    public ExampleResourceId(long id) {
        super(id);
    }
}
