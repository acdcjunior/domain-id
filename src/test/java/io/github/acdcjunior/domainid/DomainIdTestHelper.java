package io.github.acdcjunior.domainid;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.Embeddable;

import org.apache.commons.lang3.SerializationUtils;
import org.hamcrest.Matchers;
import org.springframework.util.ReflectionUtils;

public final class DomainIdTestHelper<T extends DomainId> {

    @SuppressWarnings("WeakerAccess")
    public static <T extends DomainId> void verificarImplementacaoDeIdIdAtendeTodosRequisitosBasicos(Class<T> classe) {
        new DomainIdTestHelper<T>().verificarIdAtendeTodosRequisitos(classe, "converter");
    }

    @SuppressWarnings("unused")
    public static <T extends DomainId> void verificarImplementacaoDeIdIdAtendeTodosRequisitosBasicos(Class<T> classe,
                                                                                                     String nomeDoMetodoQueConverteLista) {
        new DomainIdTestHelper<T>().verificarIdAtendeTodosRequisitos(classe, nomeDoMetodoQueConverteLista);
    }

    private void verificarIdAtendeTodosRequisitos(Class<T> classe, String nomeDoMetodoQueConverteLista) {
        try {

            construtorLong_argumentoNullLancaExcecao(classe);
            if (classeTemMetodoQueConverteLista(classe, nomeDoMetodoQueConverteLista)) {
                classeImplementaMetodoConverterDeListDeLongsParaListDeIds(classe, nomeDoMetodoQueConverteLista);
            }
            classeTemAnotacaoEmbeddable(classe);
            classeImplementaSerializableAdequadamente(classe);

        } catch (Throwable t) {
            System.err.println("#\n\n\nERRO AO TESTAR CLASSE DE ID "+classe.getCanonicalName()+" -- Veja detalhes do erro abaixo!\n\n\n#");
            throw t;
        }
    }

    private void construtorLong_argumentoNullLancaExcecao(Class<T> classe) {
        try {
            instanciarClasseViaConstrutorLong(classe, null);
            fail("Classe de ID "+classe.getSimpleName()+": Construtor com parâmetro do tipo Long: argumento null deve lançar exceção.");
        } catch (IllegalArgumentException ignore) {
        }
    }

    @SuppressWarnings("ConstantConditions")
    private boolean classeTemMetodoQueConverteLista(Class<T> classe, String nomeDoMetodoQueConverteLista) {
        return ReflectionUtils.findMethod(classe, nomeDoMetodoQueConverteLista) != null;
    }

    @SuppressWarnings("unchecked")
    private void classeImplementaMetodoConverterDeListDeLongsParaListDeIds(Class<T> classe, String nomeDoMetodoQueConverteLista) {
        String msgFalha = "Classe de ID " + classe.getSimpleName() + " deve declarar um metodo estático (com " +
            "assinatura \"public static List<ClasseId> " + nomeDoMetodoQueConverteLista + "(List<Long> codigos) { ... }\") " +
            "que converta uma lista de Longs em uma lista de IDs.";
        try {
            // pega metodo estatico
            Method metodoEstaticoConverter = classe.getDeclaredMethod(nomeDoMetodoQueConverteLista, List.class);
            // executa metodo estatico
            List<T> ids = (List<T>) metodoEstaticoConverter.invoke(null, asList(11L, 22L, 33L));
            // confere valor trazido
            T id11 = instanciarClasseViaConstrutorLong(classe, 11L);
            T id22 = instanciarClasseViaConstrutorLong(classe, 22L);
            T id33 = instanciarClasseViaConstrutorLong(classe, 33L);

            assertThat(msgFalha, ids, Matchers.contains(id11, id22, id33));
        } catch (Exception e) {
            fail(msgFalha);
        }
    }

    /**
     * Classes ID devem ser serializaveis, pois o JPA precisa poder mapeá-las.
     */
    private void classeImplementaSerializableAdequadamente(Class<T> classe) {
        assertTrue( "Classe de ID " + classe.getSimpleName() + " deve implementar Serializable", Serializable.class.isAssignableFrom(classe));
        Serializable original = instanciarClasseViaConstrutorLong(classe, 777L);
        Serializable copy = SerializationUtils.clone(original);
        assertEquals(original, copy);
    }

    private void classeTemAnotacaoEmbeddable(Class<T> classe) {
        assertNotNull("Classe de ID " + classe.getSimpleName() + " deve ter a anotação @Embeddable", classe.getAnnotation(Embeddable.class));
    }

    /**
     * Equivalente a "new Classe((Long) argumentoLong));"
     */
    private T instanciarClasseViaConstrutorLong(Class<T> classe, Long argumentoLong) {
        Constructor<T> construtorComParametroLong = getConstructorPorTipoDeArgumento(classe, Long.class);
        return instanciarViaConstrutorComArgumento(construtorComParametroLong, argumentoLong);
    }

    @SuppressWarnings("SameParameterValue")
    private <A> Constructor<T> getConstructorPorTipoDeArgumento(Class<T> classe, Class<A> tipoDoArgumento) {
        try {
            return classe.getConstructor(tipoDoArgumento);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <A> T instanciarViaConstrutorComArgumento(Constructor<T> construtor, A argumento) {
        try {
            return construtor.newInstance(argumento);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw ((IllegalArgumentException) e.getCause());
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
