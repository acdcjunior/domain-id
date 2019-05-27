package springbootapp.test;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.util.NameTransformer;

import java.io.IOException;
import java.io.StringWriter;

public class Test {
    public static void main(String args[]) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new CustomModule());
        Mainz m = new Mainz();
        m.setNome("nomemainz");
        Registration cc = new Registration();
        cc.setName("bob");
        cc.setEmail("bob");
        m.setReg(cc);
        StringWriter sw = new StringWriter();
        objectMapper.writeValue(sw, m);
        System.out.println(sw.toString());
    }

    static class Mainz {
        @JsonUnwrapped
        private Registration reg;
        private String nome;
        public Registration getReg() { return reg; }
        public void setReg(Registration reg) { this.reg = reg; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
    }

    static class Registration {
        private String email;
        private String name;
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) { this.email = email; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    static class UnwrappingRegistrationSerializer extends JsonSerializer<Registration> {
        @Override
        public void serialize(
                final Registration value,
                final JsonGenerator gen,
                final SerializerProvider serializers
        ) throws IOException {
            gen.writeStringField("unw-name", "name-value");
            gen.writeStringField("unw-email", "email-value");
        }
        @Override
        public boolean isUnwrappingSerializer() {
            return true;
        }
    }

    static class RegistrationSerializer extends JsonSerializer<Registration> {


        @Override
        public void serialize(
                final Registration value,
                final JsonGenerator gen,
                final SerializerProvider serializers
        ) throws IOException {
//            gen.writeStartObject();
//            this.delegate.serialize(value, gen, serializers);
//            gen.writeEndObject();
            gen.writeStringField("xname", "name-xvalue");
            gen.writeStringField("xemail", "email-xvalue");
        }
//
//        @Override
//        public JsonSerializer<Registration> unwrappingSerializer(
//                final NameTransformer nameTransformer
//        ) {
//            return new UnwrappingRegistrationSerializer(nameTransformer);
//        }
    }

    public static class CustomModule extends SimpleModule {
        public CustomModule() {
//            addSerializer(Registration.class, new RegistrationSerializer());
            addSerializer(Registration.class, new UnwrappingRegistrationSerializer());
        }

    }
}
