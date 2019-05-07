package io.github.acdcjunior.domainid.hibernate.sequence;

import io.github.acdcjunior.domainid.DomainId;
import org.hibernate.MappingException;
import org.hibernate.boot.model.relational.QualifiedName;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.DatabaseStructure;
import org.hibernate.id.enhanced.SequenceStructure;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;


public class DomainIdSequenceStyleGenerator extends SequenceStyleGenerator {

    public static final String SEQUENCE = "io.github.acdcjunior.domainid.hibernate.sequence.DomainIdSequenceStyleGenerator";

    private Class<? extends DomainId> classeDoCampo;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        long newId = (long) super.generate(session, obj);
        return DomainId.newInstance(classeDoCampo, newId);
    }

    @Override
    protected DatabaseStructure buildSequenceStructure(Type type, Properties params, JdbcEnvironment jdbcEnvironment, QualifiedName sequenceName, int initialValue, int incrementSize) {
//        return new DomainIdSequenceStructure(jdbcEnvironment, sequenceName, initialValue, incrementSize, type.getReturnedClass());
        return new SequenceStructure(jdbcEnvironment, sequenceName, initialValue, incrementSize, Long.class);
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        super.configure(type, params, serviceRegistry);
        if (!DomainId.class.isAssignableFrom(type.getReturnedClass())) {
            throw new IllegalArgumentException("Este Sequence Generator somente deve ser usado em campos de tipos Id (que estendem DomainId). Tipo fornecido: " + type.getReturnedClass());
        }
        //noinspection unchecked
        this.classeDoCampo = (Class<? extends DomainId>) type.getReturnedClass();
    }

}
