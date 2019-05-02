package io.github.acdcjunior.domainid;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

@SuppressWarnings("unused")
public abstract class DomainIdUserType<T extends DomainId> implements UserType {

    private static final int TIPO_SQL = Types.BIGINT;

    private final Class<T> clazz;

    protected DomainIdUserType(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{TIPO_SQL};
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] columnNames, SharedSessionContractImplementor session, Object owner) throws SQLException {
        Long id = rs.getLong(columnNames[0]);
        if (id != 0) {
            return DomainId.newInstance(clazz, id);
        }
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (null == value) {
            st.setNull(index, TIPO_SQL);
        } else {
            st.setLong(index, ((DomainId) value).toLong());
        }
    }

    @Override
    public Class returnedClass() {
        return clazz;
    }

    @Override
    public boolean equals(Object x, Object y) {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) {
        return x.hashCode();
    }

    @Override
    public Object deepCopy(Object value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object o) {
        return (Serializable) o;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) {
        return original;
    }

}
