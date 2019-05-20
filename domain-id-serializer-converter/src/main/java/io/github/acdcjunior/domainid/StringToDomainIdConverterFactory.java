package io.github.acdcjunior.domainid;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;


@Component
public class StringToDomainIdConverterFactory implements ConverterFactory<String, DomainId> {

    private static class StringToDomainIdConverter<T extends DomainId> implements Converter<String, T> {
        private Class<T> domainIdClass;
        StringToDomainIdConverter(Class<T> domainIdClass) {
            this.domainIdClass = domainIdClass;
        }
        public T convert(String source) {
            try {
                long id = Long.valueOf(source.trim());
                return DomainId.newInstance(this.domainIdClass, id);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Error while converting String value \""+source+"\" into a Domain ID of type "
                        + domainIdClass.getName()+ ". Make sure it is a valid integer/long.");
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DomainId> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToDomainIdConverter(targetType);
    }

}