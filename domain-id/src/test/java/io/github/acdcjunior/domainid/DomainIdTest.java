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

    private static class ExampleFilhoDomainId extends ExampleDomainId {
        public ExampleFilhoDomainId(Long codExample) {
            super(codExample);
        }
    }

    private static class ExampleNaoFilhoDomainId extends DomainId {
        public ExampleNaoFilhoDomainId(Long codExample) {
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
    public void toString_retorna_id() {
        // execute
        long idLong = 123L;
        ExampleDomainId id = new ExampleDomainId(idLong);
        // verify
        assertEquals(idLong + "", id.toString());
    }

    @Test
    public void toLong_retorna_id_como_long() {
        // execute
        long idLong = 123L;
        ExampleDomainId id = new ExampleDomainId(idLong);
        // verify
        assertEquals(idLong, id.toLong());
    }

    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    public void equals_aceita_classes_filhas__mas_continua_sem_aceitar_nao_filhas() {
        // setup
        ExampleDomainId exampleId = new ExampleDomainId(123L);
        ExampleFilhoDomainId exampleFilhoId = new ExampleFilhoDomainId(123L);
        ExampleNaoFilhoDomainId exampleNaoFilhoId = new ExampleNaoFilhoDomainId(123L);

        // execute/verify
        assertTrue(exampleId.equals(exampleFilhoId));
        assertTrue(exampleFilhoId.equals(exampleId));
        // noinspection EqualsBetweenInconvertibleTypes
        assertFalse(exampleId.equals(exampleNaoFilhoId));
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
        ExampleDomainId exampleUm = new ExampleDomainId(111L);
        ExampleDomainId exampleUmDeNovo = new ExampleDomainId(111L);
        ExampleDomainId exampleDois = new ExampleDomainId(222L);
        // execute/verify
        assertEquals(-1, exampleUm.compareTo(exampleDois));
        assertEquals(0, exampleUm.compareTo(exampleUmDeNovo));
        assertEquals(1, exampleDois.compareTo(exampleUm));
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

