package io.github.acdcjunior.domainid;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("WeakerAccess")
public abstract class DomainId implements Comparable<DomainId>, Serializable {

    private static final long serialVersionUID = -1L;

    protected long id;

    public DomainId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O id não pode ser nulo.");
        }
        if (id <= 0L) {
            throw new IllegalArgumentException("O id deve ser maior que zero. Obtido: " + id);
        }
        this.id = id;
    }

    public static <T extends DomainId> List<Long> converterParaLongs(List<T> objetosIds) {
        List<Long> longs = new ArrayList<>(objetosIds.size());
        for (T id : objetosIds) {
            longs.add(id.id);
        }
        return longs;
    }

    /**
     * Converte uma lista de longs para uma lista do tipo ID passado.
     *
     * @param longs  Lista dos ids a serem convertidos.
     * @param classe Classe de ID.
     * @param <T>    Tipo da classe de ID.
     * @return Lista de objetos da classe de ID relativos aos longs passados.
     */
    @SuppressWarnings("SameParameterValue")
    protected static <T extends DomainId> List<T> converter(List<Long> longs, Class<T> classe) {
        List<T> ids = new ArrayList<>();
        for (Long id : longs) {
            T novaInstancia = instanciar(classe, id);
            ids.add(novaInstancia);
        }
        return ids;
    }

    @SuppressWarnings("unchecked")
    protected static <T extends DomainId> T instanciar(Class<T> classe, Long id) {
        try {
            Constructor<?> construtorComParametroLong = classe.getConstructor(Long.class);
            return (T) construtorComParametroLong.newInstance(id);
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Erro ao instanciar ID do tipo %s usando o argumento long %d.", classe.getSimpleName(), id), e);
        }
    }

    @Override
    @SuppressWarnings("all")
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
        return Long.compare(this.toLong(), o.toLong());
    }

}
