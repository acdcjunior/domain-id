package io.github.acdcjunior.domainid;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;


@SuppressWarnings("WeakerAccess")
public class DomainIdTest {

    static class ExampleDomainId extends DomainId {
        public ExampleDomainId(long codExample) {
            super(codExample);
        }
    }

    private static class ExampleChildDomainId extends ExampleDomainId {
        public ExampleChildDomainId(Long codExample) {
            super(codExample);
        }
    }

    private static class ExampleNonChildDomainId extends DomainId {
        public ExampleNonChildDomainId(Long codExample) {
            super(codExample);
        }
    }

    @Test
    public void mapFromLongsToIds() {
        // setup
        ExampleDomainId example11 = new ExampleDomainId(11L);
        ExampleDomainId example22 = new ExampleDomainId(22L);
        // execute
        List<ExampleDomainId> exampleIds = DomainId.map(asList(11L, 22L), ExampleDomainId.class);
        // verify
        assertThat(exampleIds, contains(example11, example22));
    }

    @Test
    public void mapFromLongsToIds_varargs() {
        // setup
        ExampleDomainId example11 = new ExampleDomainId(11L);
        ExampleDomainId example22 = new ExampleDomainId(22L);
        // execute
        List<ExampleDomainId> exampleIds = DomainId.map(ExampleDomainId.class, 11L, 22L);
        // verify
        assertThat(exampleIds, contains(example11, example22));
    }

    @Test
    public void mapFromIdsToLongs() {
        // setup
        long cod11 = 11L;
        long cod22 = 22L;
        List<ExampleDomainId> exampleIds = asList(new ExampleDomainId(cod11), new ExampleDomainId(cod22));
        // execute
        List<Long> longs = DomainId.map(exampleIds);
        // verify
        assertThat(longs, contains(cod11, cod22));
    }

    @Test
    public void toString_returns_id_as_string() {
        // execute
        long idLong = 123L;
        ExampleDomainId id = new ExampleDomainId(idLong);
        // verify
        assertEquals(idLong + "", id.toString());
    }

    @Test
    public void toLong_returns_id_as_long() {
        // execute
        long idLong = 123L;
        ExampleDomainId id = new ExampleDomainId(idLong);
        // verify
        assertEquals(idLong, id.toLong());
    }

    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    public void equals_takes_child_classes__mas_doesnt_take_non_child() {
        // setup
        ExampleDomainId exampleId = new ExampleDomainId(123L);
        ExampleChildDomainId exampleChildId = new ExampleChildDomainId(123L);
        ExampleNonChildDomainId exampleNonChildId = new ExampleNonChildDomainId(123L);

        // execute/verify
        assertTrue(exampleId.equals(exampleChildId));
        assertTrue(exampleChildId.equals(exampleId));
        // noinspection EqualsBetweenInconvertibleTypes
        assertFalse(exampleId.equals(exampleNonChildId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void zero_value_throws_exception() {
        new ExampleDomainId(0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negative_value_throws_exception() {
        new ExampleDomainId(-1L);
    }

    @Test
    public void comparable() {
        ExampleDomainId exampleOne = new ExampleDomainId(111L);
        ExampleDomainId exampleOneAgain = new ExampleDomainId(111L);
        ExampleDomainId exampleTwo = new ExampleDomainId(222L);
        // execute/verify
        assertEquals(-1, exampleOne.compareTo(exampleTwo));
        assertEquals(0, exampleOne.compareTo(exampleOneAgain));
        assertEquals(1, exampleTwo.compareTo(exampleOne));
    }

    @Test
    public void cloneable() throws CloneNotSupportedException {
        ExampleDomainId exampleDomainId = new ExampleDomainId(111L);
        Object clone = exampleDomainId.clone();
        assertEquals(exampleDomainId, clone);
        assertEquals(clone, exampleDomainId);
    }

    static class DomainIdWithLongObjectConstructor extends DomainId {
        public DomainIdWithLongObjectConstructor(Long codExample) {
            super(codExample);
        }
    }

    @Test
    @SuppressWarnings("UnnecessaryBoxing")
    public void long_object_constructor_works() {
        Assertions.assertThat(new DomainIdWithLongObjectConstructor(112233L).toLong()).isEqualTo(112233L);
        Assertions.assertThat(new DomainIdWithLongObjectConstructor(Long.valueOf(112233L)).toLong()).isEqualTo(112233L);
    }

    @Test(expected = NullPointerException.class)
    public void null_long_throws_exception() {
        new DomainIdWithLongObjectConstructor(null);
    }

    @Test
    public void compare() {
        class MyEntity implements Comparable<MyEntity> {
            ExampleDomainId id;
            @Override
            public int compareTo(MyEntity other) {
                return DomainId.compare(this, other, new IdGetter<MyEntity, DomainId>() {
                    @Override
                    public DomainId getId(MyEntity e) {
                        return e.id;
                    }
                });
            }
            @Override
            public String toString() {
                return "{"+id+"}";
            }
        }
        MyEntity e1 = new MyEntity(); e1.id = new ExampleDomainId(1L);
        MyEntity e12 = new MyEntity(); e12.id = e1.id;
        MyEntity e2 = new MyEntity(); e2.id = new ExampleDomainId(2L);
        MyEntity e3 = new MyEntity(); e3.id = new ExampleDomainId(3L);
        MyEntity eN = new MyEntity(); eN.id = null;
        MyEntity eN2 = new MyEntity(); eN2.id = null;

        Set<MyEntity> entities = new TreeSet<>();
        entities.add(e3);
        entities.add(e1);
        entities.add(eN);
        entities.add(e2);
        entities.add(eN2);
        entities.add(e12);

        Assertions.assertThat(entities).containsExactly(e1, e2, e3, eN, eN2);
    }

    @Test
    public void comparator() {
        class MyEntity {
            ExampleDomainId id;
            @Override
            public String toString() {
                return "{"+id+"}";
            }
        }
        MyEntity e1 = new MyEntity(); e1.id = new ExampleDomainId(1L);
        MyEntity e12 = new MyEntity(); e12.id = e1.id;
        MyEntity e2 = new MyEntity(); e2.id = new ExampleDomainId(2L);
        MyEntity e3 = new MyEntity(); e3.id = new ExampleDomainId(3L);
        MyEntity eN = new MyEntity(); eN.id = null;
        MyEntity eN2 = new MyEntity(); eN2.id = null;

        List<MyEntity> entities = new ArrayList<>();
        entities.add(e3);
        entities.add(e1);
        entities.add(eN);
        entities.add(null);
        entities.add(e2);
        entities.add(eN2);
        entities.add(null);
        entities.add(e12);

        Collections.sort(entities, DomainId.comparator(new IdGetter<MyEntity, DomainId>() {
            @Override
            public DomainId getId(MyEntity e) {
                return e.id;
            }
        }));

        Assertions.assertThat(entities).containsExactly(e1, e12, e2, e3, eN, eN2, null, null);
    }

}

