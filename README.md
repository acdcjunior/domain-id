# domain-id

An implementation for Domain IDs, as suggested by Implementing Domain Driven Design.

This library includes:

- a `DomainId` base class;
- a hibernate `UserType`, which allows you to map the IDs directly into entities;
- a Jackson serializer and deserializer.

Importing:

```groovy
dependencies {
    implementation('io.github.acdcjunior:domain-id-all:0.1.0')
```
```xml
<dependencies>
	<dependency>
		<groupId>io.github.acdcjunior</groupId>
		<artifactId>domain-id-all</artifactId>
		<version>0.1.0</version>
	</dependency>
```

**NOTE:** Observe that this lib's `.jar` does not bring any dependencies.
That is **intentional**. Our purpose is not to force any specific Hibernate or Jackson minor versions.
You should, therefore, declare the dependencies as usual (in your `build.gradle` or `pom.xml`) and guarantee
everything works via at least one runtime test.

<br>


# Declaring

In order to use an ID, you must declare some classes first. Basically, you'll declare the ID and that's all.

The hibernate `UserType` is automatically registered.
The Jackson serializer/deserializer may be registered, if you'll need it.

### The ID class:

```java
// src/main/java/com/myservice/domain/myentity/MyEntityId.java
package com.myservice.domain.myentity;

import io.github.acdcjunior.domainid.DomainId;

public class MyEntityId extends DomainId {
    public MyEntityId(long id) {
        super(id);
    }
}
```
    
An hibernate `UserType` will be automatically registered for that ID class.
    
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
import io.github.acdcjunior.domainid.hibernate.sequence.DomainIdSequenceStyleGenerator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "MY_ENTITY", schema = "MYSERVICE")
public class MyEntity {

    @Id
    @GenericGenerator(
            name = "SEQ_MY_ENTITY",
            strategy = DomainIdSequenceStyleGenerator.SEQUENCE,
            parameters = @org.hibernate.annotations.Parameter(name = "sequence_name", value = "MYSERVICE.SEQ_MY_ENTITY")
    )
    @GeneratedValue(generator = "SEQ_MY_ENTITY", strategy = GenerationType.SEQUENCE)
    @Column
    private MyEntityId id;
```

<br>

# Details

`@GenericGenerator` - Parameters:

- `sequence_name`: Sequence's full name, including the schema/owner name. Example: `MY_SCHEMA.MY_ENTITY_SEQ`.
