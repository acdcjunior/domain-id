package io.github.acdcjunior.domainid.hibernate.usertype;

import io.github.acdcjunior.domainid.DomainId;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.LiteralType;
import org.hibernate.type.descriptor.sql.BigIntTypeDescriptor;


public class DomainIdType<T extends DomainId>
		extends AbstractSingleColumnStandardBasicType<T>
		implements LiteralType<T> {

    private final Class<T> clazz;

    public DomainIdType(Class<T> clazz) {
		super( BigIntTypeDescriptor.INSTANCE, new DomainIdJavaDescriptor<>(clazz) );
		this.clazz = clazz;
	}

    @Override
	public String getName() {
		return clazz.getSimpleName();
	}

	@Override
	protected boolean registerUnderJavaType() {
		return true;
	}

	@Override public String objectToSQLString(T value, Dialect dialect) { return "'" + value.toLong() + "'"; }

}
