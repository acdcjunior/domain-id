package io.github.acdcjunior.domainid;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static java.lang.String.format;


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

    /**
     * <p>Helper function that compares two instances of an entity by their value of a {@link DomainId} field.</p>
     * <br>
     * <b>Note:</b> this function tolerates {@code null} ids. And, similar to Relational DBs, two {@code null} ids
     * are always considered different from each other.
     * @param thisE First instance to compare.
     * @param thatE Second instance to compare.
     * @param getter Function that returns the {@link DomainId} field's value for a given entity.
     * @param <E> The entity type.
     * @param <T> The {@link DomainId} type.
     * @return a negative integer, zero, or a positive integer as the
     *         first argument's ID is less than, equal to, or greater than the
     *         second argument's ID.
     */
    public static <E, T extends DomainId> int compare(E thisE, E thatE, Function<E, T> getter) {
        if (thisE == null) {
            return 1;
        }
        if (thatE == null) {
            return -1;
        }
        T thisId = getter.apply(thisE);
        if (thisId == null) {
            return 1;
        }
        T thatId = getter.apply(thatE);
        if (thatId == null) {
            return -1;
        }
        return thisId.compareTo(thatId);
    }

    /**
     * <p>Creates a comparator for two given entities that have an ID field returned by the provided getter function.</p>
     * <br>
     * <b>Note:</b> the returned comparator is based on the {@link DomainId#compare(Object, Object, Function)} function.
     * See its docs for more details.
     * @param getter Function that returns the {@link DomainId} field's value for a given entity.
     * @param <E> The entity type.
     * @param <T> The {@link DomainId} type.
     * @return A comparator instance.
     */
    public static <E, T extends DomainId> Comparator<E> comparator(Function<E, T> getter) {
        return (o1, o2) -> DomainId.compare(o1, o2, getter);
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
