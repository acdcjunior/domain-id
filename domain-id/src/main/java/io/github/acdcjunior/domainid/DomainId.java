/*
 * Copyright (c) 2019 domain-id authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.acdcjunior.domainid;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.format;


/**
 * <p>This class provides the base logic for implementation of <strong>Domain IDs</strong>.</p>
 * <br>
 * <p>Domain IDs are Value Objects used to hold the identity value of Aggregate Roots.</p>
 *
 * <p>They aid the application of the
 * <strong><a href="http://www.informit.com/articles/article.aspx?p=2020371&seqNum=4">DDD Rule: "REFERENCE OTHER
 * AGGREGATES BY IDENTITY"</a></strong>, and bring some advantages:</p>
 *
 * <ul>
 *     <li>Leverage the language type system to make IDs unambiguous throughout all the code (specially making method
 *     signatures clearer)</li>
 *     <li>Help the identification of Aggregate Roots in the system (since the only entities that have IDs are aggregate
 *     roots)</li>
 *     <li>Help enforcing the consistency boundary of an aggregate, that is, it makes more difficult for two aggregates
 *     be modified in a single transaction by accident (something that shouldn't happen without very good reasons) since
 *     an aggregate will not hold a reference to another (just the id)</li>
 *     <li>Keep aggregates automatically smaller because references are never eagerly loaded. They load faster and
 *     consume less memory.</li>
 * </ul>
 *
 * <p>This class should be used as base for all ID classes. Also as an utilities class for common operations on IDs,
 * such as converting list of {@code long}s into list of IDs (e.g. {@link DomainId#map(Class, long...)}).</p>
 *
 * <p>Example ID class:</p>
 * <pre><code>
 * // src/main/java/com/myservice/domain/myagreggate/MyAggregateRootEntityId.java
 * package com.myservice.domain.myagreggate;
 *
 * import io.github.acdcjunior.domainid.DomainId;
 *
 * public class MyAggregateRootEntityId extends DomainId {
 *     public MyAggregateRootEntityId(long id) {
 *         super(id);
 *     }
 * }
 * </code></pre>
 *
 * @author acdcjunior
 * @since 1.0.0
 */
@SuppressWarnings("WeakerAccess")
public abstract class DomainId implements Comparable<DomainId>, Serializable, Cloneable {

    private static final long serialVersionUID = 100L;

    protected final long id;

    protected DomainId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("The id should be greater than zero. Supplied: " + id);
        }
        this.id = id;
    }

    /**
     * Converts the ID to its {@code long} value.
     * @return The ID value.
     */
    public long toLong() {
        return id;
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

    /**
     * <p>Creates a new instance of a Domain ID of the specified class.</p>
     * This method searches for a constructor of a single {@code long} (or {@code java.lang.Long}) argument in the specified ID class.
     * If none is found, an exception is thrown.
     * @param domainIdClass The ID class to be instantiated.
     * @param id The ID value.
     * @param <T> The ID class type.
     * @return An instance of the given ID class with the given id value.
     */
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
     * @param getter IdGetter that returns the {@link DomainId} field's value for a given entity.
     * @param <E> The entity type.
     * @param <T> The {@link DomainId} type.
     * @return a negative integer, zero, or a positive integer as the
     *         first argument's ID is less than, equal to, or greater than the
     *         second argument's ID.
     */
    public static <E, T extends DomainId> int compare(E thisE, E thatE, IdGetter<E, T> getter) {
        if (thisE == null) {
            return 1;
        }
        if (thatE == null) {
            return -1;
        }
        T thisId = getter.getId(thisE);
        if (thisId == null) {
            return 1;
        }
        T thatId = getter.getId(thatE);
        if (thatId == null) {
            return -1;
        }
        return thisId.compareTo(thatId);
    }

    /**
     * <p>Creates a comparator for two given entities that have an ID field returned by the provided getter function.</p>
     * <br>
     * <b>Note:</b> the returned comparator is based on the {@link DomainId#compare(Object, Object, IdGetter)} function.
     * See its docs for more details.
     * @param getter IdGetter that returns the {@link DomainId} field's value for a given entity.
     * @param <E> The entity type.
     * @param <T> The {@link DomainId} type.
     * @return A comparator instance.
     */
    public static <E, T extends DomainId> Comparator<E> comparator(final IdGetter<E, T> getter) {
        return new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                return DomainId.compare(o1, o2, getter);
            }
        };
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

    @Override
    public int compareTo(DomainId o) {
        return Long.compare(this.id, o.id);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
