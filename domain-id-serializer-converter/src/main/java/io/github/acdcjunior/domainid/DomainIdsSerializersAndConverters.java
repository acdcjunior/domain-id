package io.github.acdcjunior.domainid;

/**
 * <p>This class is just a placeholder to be used in {@code scanBasePackageClasses} of the {@code @SpringBootApplication}
 * annotation:</p>
 *
 * <pre><code>
 * &nbsp;@SpringBootApplication(
 *     scanBasePackageClasses = {
 *         io.github.acdcjunior.domainid.DomainIdsSerializersAndConverters.class,
 *         ... // other classes
 *     }
 * )
 * public class MySpringBootApplication {
 * </code></pre>
 *
 * <p>This class adds the Jackson Serializer and Deserializer for Domain ID classes.
 * It also registers the {@link org.springframework.core.convert.converter.ConverterFactory} that will automatically
 * create {@link org.springframework.core.convert.converter.Converter}s for each Domain ID classes the app has.</p>
 */
@SuppressWarnings("unused")
public class DomainIdsSerializersAndConverters {
}
