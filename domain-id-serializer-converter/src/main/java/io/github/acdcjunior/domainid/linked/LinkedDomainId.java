package io.github.acdcjunior.domainid.linked;

import java.lang.annotation.*;


/**
 * Configures the DomainId class to be serialized as an object with id and _links.self.href properties.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LinkedDomainId {

    /**
     * <p>The href for the self link. Can be a Spring Expression.</p>
     * <p>If there is a {@code #} character in the string, it will be replaced with the actual id value.</p>
     * Example:
     * <br>
     *     {@code @LinkedDomainId("${MY_API.BASE_URL}/api/resources/#")}
     * <br>
     *     becomes {@code "http://the-value-for-that-base-url-spring-prop.com/api/resources/123"}
     */
    String value();

}

