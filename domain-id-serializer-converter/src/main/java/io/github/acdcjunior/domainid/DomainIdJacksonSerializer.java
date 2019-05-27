package io.github.acdcjunior.domainid;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


//@JsonComponent
//@ConditionalOnBean(name = "dataSourcmmmme")
public class DomainIdJacksonSerializer {

    @Autowired
    ApplicationContext outerApplicationContext;

    public static class DomainIdJsonSerializer extends JsonSerializer<DomainId> {

        private final Environment environment;

        public DomainIdJsonSerializer(Environment environment) {
            this.environment = environment;
        }

        @Override
        public void serialize(DomainId domainId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {


            //Validate.isInstanceOf(CustomClass.class, value);
            {
                jsonGenerator.writeStartObject();
                JavaType javaType = serializerProvider.constructType(DomainId.class);
                BeanDescription beanDesc = serializerProvider.getConfig().introspect(javaType);
                JsonSerializer<Object> serializer = BeanSerializerFactory.instance.findBeanSerializer(serializerProvider,
                        javaType,
                        beanDesc);
                // this is basically your 'writeAllFields()'-method:
                serializer.unwrappingSerializer(null).serialize(domainId, jsonGenerator, serializerProvider);
                jsonGenerator.writeObjectField("my_extra_field", "some data");
                jsonGenerator.writeEndObject();
            }
            if (true) return;
            DomainIdCustomSerialization annotation = domainId.getClass().getAnnotation(DomainIdCustomSerialization.class);
            if (domainId.toLong() != 118877L && !isFieldNameThatShouldBeSerializedAsObject(annotation, jsonGenerator.getOutputContext().getCurrentName())) {



                jsonGenerator.writeStartObject();
                JavaType javaType = serializerProvider.constructType(String.class);
                BeanDescription beanDesc = serializerProvider.getConfig().introspect(javaType);
                JsonSerializer<Object> serializer = BeanSerializerFactory.instance.findBeanSerializer(serializerProvider,
                        javaType,
                        beanDesc);
                // this is basically your 'writeAllFields()'-method:
                this.unwrappingSerializer(null).serialize(new DomainId(118877L) {}, jsonGenerator, serializerProvider);
                jsonGenerator.writeObjectField("my_extra_field", "some data");
                jsonGenerator.writeEndObject();

//                jsonGenerator.writeNumber(domainId.toLong());
                return;
            }



            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField(annotation.idPropertyName(), domainId.toLong());
            for (AdditionalProperty additionalProperty : annotation.generateAdditionalProperty()) {
                String path = additionalProperty.name();
                String value = environment.resolveRequiredPlaceholders(additionalProperty.value());
                writeMapOfMaps(jsonGenerator, path, value);
            }
            jsonGenerator.writeEndObject();
        }

        private void writeMapOfMaps(JsonGenerator jsonGenerator, String path, String value) throws IOException {
            String[] split = path.split("\\.");
            Map<String, Object> root = new HashMap<>();
            Map<String, Object> current = root;
            for (int i = 1; i < split.length - 1; i++) {
                Map<String, Object> middle = new HashMap<>();
                current.put(split[i], middle);
                current = middle;
            }
            current.put(split[split.length - 1], value);
            jsonGenerator.writeObjectField(split[0], root);
        }
    }

    private static boolean isFieldNameThatShouldBeSerializedAsObject(DomainIdCustomSerialization annotation, String fieldName) {
        if (annotation == null) {
            return false;
        }
        boolean fieldNameIsInIncludeList = isLeftBlank(annotation.whenFieldNameIs()) || Arrays.asList(annotation.whenFieldNameIs()).contains(fieldName);
        boolean fieldNameIsNotInExcludeList = isLeftBlank(annotation.whenFieldNameIsNot()) || !Arrays.asList(annotation.whenFieldNameIsNot()).contains(fieldName);
        return fieldNameIsInIncludeList && fieldNameIsNotInExcludeList;
    }

    private static boolean isLeftBlank(String[] strings) {
        return strings.length == 1 && strings[0].equals(DomainIdCustomSerialization.VALUE_LEFT_BLANK);
    }

    public static class DomainIdJsonDeserializer extends JsonDeserializer<DomainId> {

        @Override
        public DomainId deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            Long id = deserializationContext.readValue(jsonParser, Long.class);
            @SuppressWarnings("unchecked")
            Class<? extends DomainId> idClass = (Class<? extends DomainId>) handledType();
            return DomainId.newInstance(idClass, id);
        }

    }

}


@Configuration
class XX {

    @Autowired
    Environment environment;

    @Bean
    public Module module(){                                 // register as module
        SimpleModule module = new SimpleModule();
        module.addSerializer(DomainId.class, new DomainIdJacksonSerializer.DomainIdJsonSerializer(environment));               // register as serialize class for Instant.class
        module.addDeserializer(DomainId.class, new DomainIdJacksonSerializer.DomainIdJsonDeserializer());          //  register as deserialize class for Instant.class
        return module;
    }

}