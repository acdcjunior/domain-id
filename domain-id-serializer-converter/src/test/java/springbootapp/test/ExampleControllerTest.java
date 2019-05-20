package springbootapp.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import springbootapp.ExampleController;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {ExampleController.class}, secure = false)
class ExampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void exampleGet() throws Exception {
        this.mockMvc.perform(
                get("/example-get/112233/stuff/445566")
        )
                .andExpect(status().isOk())
                .andExpect(content().json("557799"))
        ;
    }

    @Test
    void examplePost() throws Exception {
        this.mockMvc.perform(
                post("/example-post")
                        .content("{\"id\":123,\"name\":\"Bozo\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(123)))
                .andExpect(jsonPath("$.name", equalTo("Bozo")))
        ;
    }

}
