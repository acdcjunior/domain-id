package io.github.acdcjunior.domainid.hibernate.usertype;

import io.github.acdcjunior.domainid.DomainId;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.service.ServiceRegistry;
import org.jboss.logging.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Scans the classpath for Id classes and automatically register usertypes for each of them.
 */
public class DomainIdTypeContributor implements TypeContributor {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(
            CoreMessageLogger.class,
            DomainIdTypeContributor.class.getName()
    );

	@Override
	public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        long startTime = System.nanoTime();
        List<Class<? extends DomainId>> types = findMyTypes("");
        LOG.info("Classpath scanned in " + (System.nanoTime() - startTime)/1000000 + " ms");

        for (Class<? extends DomainId> type : types) {
            typeContributions.contributeType(new DomainIdType<>(type));
        }

        LOG.info("Registered Id UserTypes: " + types);

        for (int i = 0; i < 50; i++) {
            LOG.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        }
	}

    private List<Class<? extends DomainId>> findMyTypes(String basePackage) {
        try {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + basePackage + "/**/*Id.class";
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

            List<Class<? extends DomainId>> candidates = new ArrayList<>();
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    try {
                        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        Class<?> clazz = Class.forName(metadataReader.getClassMetadata().getClassName());
                        if (clazz != DomainId.class && DomainId.class.isAssignableFrom(clazz)) {
                            //noinspection unchecked
                            candidates.add((Class<? extends DomainId>) clazz);
                        }
                    } catch (ClassNotFoundException | NoClassDefFoundError | ExceptionInInitializerError e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
            return candidates;
        } catch (IOException e) {
            throw new RuntimeException("Error while scanning classpath for DomainId classes.", e);
        }
    }

}
