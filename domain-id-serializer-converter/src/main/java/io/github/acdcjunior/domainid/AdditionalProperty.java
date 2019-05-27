package io.github.acdcjunior.domainid;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface AdditionalProperty {
    String name();

    String value();
}
