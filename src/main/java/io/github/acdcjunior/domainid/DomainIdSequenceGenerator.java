package io.github.acdcjunior.domainid;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

@SuppressWarnings("unused")
public class DomainIdSequenceGenerator implements IdentifierGenerator, Configurable {

    public static final String STRATEGY = "io.github.acdcjunior.domainid.DomainIdSequenceGenerator";

    private String nomeSequence;
    private Class<? extends DomainId> classeDoCampo;

    @Override
    @SuppressWarnings("SqlNoDataSourceInspection")
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String consultaSequence = "SELECT " + nomeSequence + ".NEXTVAL AS COD FROM DUAL";
        try {
            Query query = session.createSQLQuery(consultaSequence).addScalar("cod", StandardBasicTypes.LONG);
            Long lon = (Long) query.uniqueResult();
            return DomainId.instanciar(classeDoCampo, lon);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar sequence para obtencao do próximo ID. Query usada: " + consultaSequence, e);
        }
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        this.nomeSequence = params.getProperty("sequence");
        if (this.nomeSequence == null) {
            throw new IllegalArgumentException("A declaração de um @Parameter \"sequence\" é obrigatória. Nenhum foi fornecido.");
        }
        if (this.nomeSequence.split("\\.").length != 2) {
            throw new IllegalArgumentException("O valor do @Parameter \"sequence\" deve ser \"OWNER.NOME_DA_SEQUENCE\". Fornecido: " + this.nomeSequence);
        }
        if (!DomainId.class.isAssignableFrom(type.getReturnedClass())) {
            throw new IllegalArgumentException("Este Sequence Generator somente deve ser usado em campos de tipos Id (que estendem DomainId). Tipo fornecido: " + type.getReturnedClass());
        }
        //noinspection unchecked
        this.classeDoCampo = (Class<? extends DomainId>) type.getReturnedClass();
    }

}
