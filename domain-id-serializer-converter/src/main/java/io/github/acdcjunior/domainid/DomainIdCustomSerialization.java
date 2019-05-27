package io.github.acdcjunior.domainid;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DomainIdCustomSerializations.class)
public @interface DomainIdCustomSerialization {

    String VALUE_LEFT_BLANK = "!!__VALUE_LEFT_BLANK__!!";

    String[] whenFieldNameIs() default VALUE_LEFT_BLANK;
    String[] whenFieldNameIsNot() default VALUE_LEFT_BLANK;

    String idPropertyName() default "id";

    AdditionalProperty[] generateAdditionalProperty() default {};
    SerializeDomainIdAsObject[] serializeDomainIdAsObject() default {};

}

