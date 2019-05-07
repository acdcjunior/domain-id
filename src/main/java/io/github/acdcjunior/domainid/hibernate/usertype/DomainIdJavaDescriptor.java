package io.github.acdcjunior.domainid.hibernate.usertype;

import io.github.acdcjunior.domainid.DomainId;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;


public class DomainIdJavaDescriptor<T extends DomainId> extends AbstractTypeDescriptor<T> {

    private final Class<T> clazz;

    @SuppressWarnings("unchecked")
    public DomainIdJavaDescriptor(Class<T> clazz) {
        super(clazz, ImmutableMutabilityPlan.INSTANCE);
        this.clazz = clazz;
    }

    @Override
    public String toString(T value) {
        return value.toLong() + "";
    }

    @Override
    public T fromString(String string) {
        return DomainId.newInstance(clazz, Long.valueOf(string));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> X unwrap(T value, Class<X> type, WrapperOptions options) {
        if (value == null) { return null; }
        if (DomainId.class.isAssignableFrom(type)) { return (X) value; }
        if (Long.class.isAssignableFrom(type)) { return (X) (Long) value.toLong(); }
        throw unknownUnwrap(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> T wrap(X value, WrapperOptions options) {
        if (value == null) { return null; }
        if (value instanceof Long && (Long) value == 0) { return null; }
        if (value instanceof DomainId) { return (T) value; }
        if (value instanceof Long) { return DomainId.newInstance(clazz, (Long) value); }
        throw unknownWrap(value.getClass());
    }

}
