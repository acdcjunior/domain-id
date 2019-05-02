package io.github.acdcjunior.domainid;


import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.Embeddable;

import org.junit.Test;

@SuppressWarnings("WeakerAccess")
public class DomainIdTest {

    @Embeddable
    static class ExemploDomainId extends DomainId {

        public ExemploDomainId(Long codExemplo) {
            super(codExemplo);
        }

        public static List<ExemploDomainId> converter(List<Long> codigosDosExemplos) {
            return DomainId.converter(codigosDosExemplos, ExemploDomainId.class);
        }

    }

    private static class ExemploFilhoDomainId extends ExemploDomainId {
        public ExemploFilhoDomainId(Long codExemplo) {
            super(codExemplo);
        }
    }

    private static class ExemploNaoFilhoDomainId extends DomainId {
        public ExemploNaoFilhoDomainId(Long codExemplo) {
            super(codExemplo);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void long_null_lanca_excecao() {
        new ExemploDomainId(null);
    }

    @Test
    public void conversaoDeLongsParaObjetos() {
        // setup
        ExemploDomainId exemplo11 = new ExemploDomainId(11L);
        ExemploDomainId exemplo22 = new ExemploDomainId(22L);
        // execute
        List<ExemploDomainId> exemploIds = ExemploDomainId.converter(asList(11L, 22L));
        // verify
        assertThat(exemploIds, contains(exemplo11, exemplo22));
    }

    @Test
    public void conversaoDeObjetosParaLongs() {
        // setup
        long cod11 = 11L;
        long cod22 = 22L;
        List<ExemploDomainId> exemploIds = asList(new ExemploDomainId(cod11), new ExemploDomainId(cod22));
        // execute
        List<Long> longs = DomainId.converterParaLongs(exemploIds);
        // verify
        assertThat(longs, contains(cod11, cod22));
    }

    @Test
    public void verificacaoIdAbstratoTestHelper() {
        // este teste funciona como um teste da classe DomainIdTestHelper inteira
        DomainIdTestHelper.verificarImplementacaoDeIdIdAtendeTodosRequisitosBasicos(ExemploDomainId.class);
    }

    @Test
    public void toString_retorna_id() {
        // execute
        long idLong = 123L;
        ExemploDomainId id = new ExemploDomainId(idLong);
        // verify
        assertEquals(idLong + "", id.toString());
    }

    @Test
    public void toLong_retorna_id_como_long() {
        // execute
        long idLong = 123L;
        ExemploDomainId id = new ExemploDomainId(idLong);
        // verify
        assertEquals(idLong, id.toLong());
    }

    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    public void equals_aceita_classes_filhas__mas_continua_sem_aceitar_nao_filhas() {
        // setup
        ExemploDomainId exemploId = new ExemploDomainId(123L);
        ExemploFilhoDomainId exemploFilhoId = new ExemploFilhoDomainId(123L);
        ExemploNaoFilhoDomainId exemploNaoFilhoId = new ExemploNaoFilhoDomainId(123L);

        // execute/verify
        assertTrue(exemploId.equals(exemploFilhoId));
        assertTrue(exemploFilhoId.equals(exemploId));
        // noinspection EqualsBetweenInconvertibleTypes
        assertFalse(exemploId.equals(exemploNaoFilhoId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void valor_zero_lanca_excecao() {
        new ExemploDomainId(0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void valor_negativo_lanca_excecao() {
        new ExemploDomainId(-1L);
    }

    @Test
    public void comparable() {
        ExemploDomainId exemploUm = new ExemploDomainId(111L);
        ExemploDomainId exemploUmDeNovo = new ExemploDomainId(111L);
        ExemploDomainId exemploDois = new ExemploDomainId(222L);
        // execute/verify
        assertEquals(-1, exemploUm.compareTo(exemploDois));
        assertEquals(0, exemploUm.compareTo(exemploUmDeNovo));
        assertEquals(1, exemploDois.compareTo(exemploUm));
    }

}

