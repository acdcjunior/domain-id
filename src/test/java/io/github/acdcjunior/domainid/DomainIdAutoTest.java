package io.github.acdcjunior.domainid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.reflections.Reflections;

public class DomainIdAutoTest {

    private final Set<String> nomesDeClassesUsadasEmTestes = new TreeSet<>(Arrays.asList("ExemploDomainId", "ExemploNaoFilhoDomainId", "ExemploFilhoDomainId"));

    @Test
    public void testarClassesFilhasDeID() {
        /*
         * Este teste varre o classpath atras de todas as filhas de DomainId. Pra cada filho encontrado, ele roda os testes automaticamente.
         */
        Reflections reflections = new Reflections("io.github.acdcjunior");
        Set<Class<? extends DomainId>> classes = reflections.getSubTypesOf(DomainId.class);

        Set<Class<? extends DomainId>> classes2 = new HashSet<>();
        for (Class<? extends DomainId> c : classes) {
            if (!nomesDeClassesUsadasEmTestes.contains(c.getSimpleName())) {
                classes2.add(c);
            }
        }

        for (Class<? extends DomainId> c : classes2) {
            DomainIdTestHelper.verificarImplementacaoDeIdIdAtendeTodosRequisitosBasicos(c);
        }
    }

}
