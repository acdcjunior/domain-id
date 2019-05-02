package io.github.acdcjunior.domainid;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;


@SuppressWarnings("WeakerAccess")
public class DomainIdTest {

    static class ExampleDomainId extends DomainId {

        public ExampleDomainId(Long codExample) {
            super(codExample);
        }

        public static List<ExampleDomainId> converter(List<Long> codigosDosExamples) {
            return DomainId.converter(codigosDosExamples, ExampleDomainId.class);
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

    @Test(expected = NullPointerException.class)
    public void null_long_throws_exception() {
        new ExampleDomainId(null);
    }

    @Test
    public void conversaoDeLongsParaObjetos() {
        // setup
        ExampleDomainId example11 = new ExampleDomainId(11L);
        ExampleDomainId example22 = new ExampleDomainId(22L);
        // execute
        List<ExampleDomainId> exampleIds = ExampleDomainId.converter(asList(11L, 22L));
        // verify
        assertThat(exampleIds, contains(example11, example22));
    }

    @Test
    public void conversaoDeObjetosParaLongs() {
        // setup
        long cod11 = 11L;
        long cod22 = 22L;
        List<ExampleDomainId> exampleIds = asList(new ExampleDomainId(cod11), new ExampleDomainId(cod22));
        // execute
        List<Long> longs = DomainId.converterParaLongs(exampleIds);
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
    public void valor_zero_lanca_excecao() {
        new ExampleDomainId(0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void valor_negativo_lanca_excecao() {
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

}

