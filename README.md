# NOTE: NOT COMPLETED YET, BUT SOON.

# domain-id

An implementation for Domain IDs, as suggested by Implementing Domain Driven Design.

This library includes:

- a `DomainId` base class;
- a hibernate `UserType`, which allows you to map the IDs directly into entities;
- a Jackson serializer and deserializer.

Importing:

```groovy
dependencies {
    implementation('io.github.acdcjunior:domain-id:?.?.?')
```
```xml
<dependencies>
	<dependency>
		<groupId>io.github.acdcjunior</groupId>
		<artifactId>domain-id</artifactId>
		<version>?.?.?</version>
	</dependency>
```

**NOTE:** Observe that this lib's `.jar` does not bring any dependencies.
That is **intentional**. Our purpose is not to force any specific Hibernate or Jackson minor versions.
You should, therefore, declare the dependencies as usual (in your `build.gradle` or `pom.xml`) and guarantee
everything works via at least one runtime test.

<br>


# Declaring

In order to use an ID, you must declare some classes first. Basically, you'll declare the ID itself, the hibernate
`UserType` and the Jackson serializer/deserializer, if needed.

### The ID class:

```java
// src/main/java/com/myservice/domain/myentity/MyEntityId.java
package com.myservice.domain.myentity;

import io.github.acdcjunior.domainid.DomainId;

public class MyEntityId extends DomainId {

    public MyEntityId(Long id) {
        super(id);
    }

}
```
    
### The `UserType` for the ID class:

```java
// src/main/java/com/myservice/infra/myentity/MyEntityIdType.java
package com.myservice.infra.myentity;

import com.myservice.domain.myentity.MyEntityId;
import io.github.acdcjunior.domainid.DomainIdUserType;

public class MyEntityIdType extends DomainIdUserType<MyEntityId> {

    public MyEntityIdType() {
        super(MyEntityId.class);
    }

}
```

and, at the (IMPORTANT) **`package-info.java`**:

```java
// src/main/java/com/myservice/infra/myentity/package-info.java
@TypeDefs({
    @TypeDef(name = "MyEntityIdType", typeClass = MyEntityIdType.class, defaultForType = MyEntityId.class)
})
package com.myservice.infra.myentity;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.myservice.domain.myentity.MyEntityId;
```
    
#### Add, if desired, the Jackson serializer/deserializer

```java
// src/main/java/com/myservice/MyServiceApplication.java
package com.myservice;

// ...   
import io.github.acdcjunior.domainid.DomainIdSerializer;

@SpringBootApplication(scanBasePackages = "com.myservice", scanBasePackageClasses = DomainIdSerializer.class)
public class MyServiceApplication {
```

<br><br>
    
# Usage

After the declarations above, use as follows:

# Non-ID field:

```java
@Entity
@Table(name = "MY_ENTITY", schema = "MYSERVICE")
public class MyEntity {

    // ...

    @Column
    private OtherEntityId otherEntity;
```

#### Using as `@Id` and auto-generated using a Sequence:

```java
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import io.github.acdcjunior.domainid.DomainIdSequenceGenerator;

@Entity
@Table(name = "MY_ENTITY", schema = "MYSERVICE")
public class MyEntity {

    @Id
    @GenericGenerator(name = "generator", strategy = DomainIdSequenceGenerator.STRATEGY, parameters = @Parameter(name = "sequence", value = "MYSERVICE.SEQ_EXEMPLO"))
    @GeneratedValue(generator = "generator", strategy = GenerationType.SEQUENCE)
    @Column
    private MyEntityId id;
```

<br>

# Details

`@GenericGenerator` - Parameters:

- `sequence`: Sequence's full name, including the schema/owner name. Example: `MY_SCHEMA.MY_ENTITY_SEQ`.