package io.github.acdcjunior.domainid;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;


@SuppressWarnings("WeakerAccess")
public abstract class DomainId implements Comparable<DomainId>, Serializable, Cloneable {

    private static final long serialVersionUID = -1L;

    protected long id;

    protected DomainId(long id) {
        if (id <= 0L) {
            throw new IllegalArgumentException("The id should be greated than zero. Supplied: " + id);
        }
        this.id = id;
    }

    /**
     * Converts a list of ids into a list of longs.
     * @param ids The ids to be converted.
     * @param <T> The id type.
     * @return A list of the long values of the ids.
     */
    public static <T extends DomainId> List<Long> map(List<T> ids) {
        List<Long> longs = new ArrayList<>(ids.size());
        for (T id : ids) {
            longs.add(id.id);
        }
        return longs;
    }

    /**
     * Converts a list of longs into a list of the given domain id type.
     *
     * @param longs Longs to be converted.
     * @param domainIdClass The Domain ID class.
     * @param <T>    Type of the Domain ID.
     * @return List of domain ids corresponding to the supplied longs.
     */
    public static <T extends DomainId> List<T> map(List<Long> longs, Class<T> domainIdClass) {
        List<T> ids = new ArrayList<>();
        for (Long id : longs) {
            T domainId = newInstance(domainIdClass, id);
            ids.add(domainId);
        }
        return ids;
    }

    /**
     * Converts a list of longs into a list of the given domain id type.
     *
     * @param domainIdClass The Domain ID class.
     * @param longs Longs to be converted.
     * @param <T>    Type of the Domain ID.
     * @return List of domain ids corresponding to the supplied longs.
     */
    public static <T extends DomainId> List<T> map(Class<T> domainIdClass, long... longs) {
        List<Long> longsList = new ArrayList<>(longs.length);
        for (long x : longs) {
            longsList.add(x);
        }
        return map(longsList, domainIdClass);
    }

    @SuppressWarnings("unchecked")
    public static <T extends DomainId> T newInstance(Class<T> domainIdClass, long id) {
        try {
            Constructor<?> constructorWithLongArgument = getConstructorWithLongArgument(domainIdClass);
            return (T) constructorWithLongArgument.newInstance(id);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException(format("Error while instantiating ID of type %s using the argument %d.", domainIdClass.getSimpleName(), id), e);
        }
    }

    private static <T extends DomainId> Constructor<?> getConstructorWithLongArgument(Class<T> domainIdClass) {
        try {
            return domainIdClass.getConstructor(Long.TYPE);
        } catch (NoSuchMethodException | SecurityException ignored) {
            try {
                return domainIdClass.getConstructor(Long.class);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new IllegalArgumentException(format("Error while looking for a single-argument long/Long constructor in class %s. Did you declare one?",
                        domainIdClass.getSimpleName()), e);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!o.getClass().isAssignableFrom(getClass()) && !getClass().isAssignableFrom(o.getClass())) {
            return false;
        }
        DomainId that = (DomainId) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return Long.toString(id);
    }

    public long toLong() {
        return id;
    }

    @Override
    public int compareTo(DomainId o) {
        return Long.compare(this.id, o.id);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
