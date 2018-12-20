package com.waes;

import com.waes.repository.NodesRepository;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NodesRepository nodesRepository;

    @After
    public void tearDown() {
        nodesRepository.clearNodeMap();
    }

    @Test
    public void testRegisterLeftNodeData() throws Exception {
        String base64Json = "eyJqb25hIjoiam9uYSJ9";
        Long nodeId = 1l;

        mockMvc.perform(
                post("/v1/diff/" + nodeId + "/left")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(base64Json))
                .andExpect(status().isCreated());

    }

    @Test
    public void testRegisterRightNodeData() throws Exception {
        String base64Json = "eyJhbmltYWwiOiJkb2cifQ==";
        Long nodeId = 1l;

        mockMvc.perform(
                post("/v1/diff/" + nodeId + "/right")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(base64Json))
                .andExpect(status().isCreated());

    }

    @Test
    public void testRegisterMalformedJsonNodeData() throws Exception {
        String malformedBase64Json = "e3siam9uYSI6ImpvbmF9";
        Long nodeId = 1l;

        mockMvc.perform(
                post("/v1/diff/" + nodeId + "/right")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(malformedBase64Json))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.is("JsonMalformedException")));

    }

    @Test
    public void testRegisterDataTwiceThrowNodeDataAlreadyAddedException() throws Exception {
        String base64Json = "eyJhbmltYWwiOiJkb2cifQ==";
        Long nodeId = 2l;

        mockMvc.perform(
                post("/v1/diff/" + nodeId + "/left")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(base64Json))
                .andExpect(status().isCreated());

        mockMvc.perform(
                post("/v1/diff/" + nodeId + "/left")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(base64Json))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.is("NodeDataAlreadyAddedException")));
    }

    @Test
    public void testCompareNodeEqualData() throws Exception {
        String base64Json = "eyJhbmltYWwiOiJkb2cifQ==";
        Long nodeId = 3l;

        mockMvc.perform(
                post("/v1/diff/" + nodeId + "/left")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(base64Json))
                .andExpect(status().isCreated());

        mockMvc.perform(
                post("/v1/diff/" + nodeId + "/right")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(base64Json))
                .andExpect(status().isCreated());

        mockMvc.perform(
                get("/v1/diff/" + nodeId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("EQUAL DATA")));
    }

    @Test
    public void testCompareNodeDifferentData() throws Exception {
        String base64Json = "eyJhbmltYWwiOiJkb2cifQ==";
        String differentBase64Json = "eyJoaSI6IndhZXMifQ==";
        Long nodeId = 4l;

        mockMvc.perform(
                post("/v1/diff/" + nodeId + "/left")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(base64Json))
                .andExpect(status().isCreated());

        mockMvc.perform(
                post("/v1/diff/" + nodeId + "/right")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(differentBase64Json))
                .andExpect(status().isCreated());

        mockMvc.perform(
                get("/v1/diff/" + nodeId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("DIFFERENT DATA")));
    }

    @Test
    public void testCompareNodeDifferentDataThrowNodeDataEmptyForComparisonException() throws Exception {
        String base64Json = "eyJhbmltYWwiOiJkb2cifQ==";
        Long nodeId = 5l;

        mockMvc.perform(
                post("/v1/diff/" + nodeId + "/left")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(base64Json))
                .andExpect(status().isCreated());

        mockMvc.perform(
                get("/v1/diff/" + nodeId))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.is("NodeDataEmptyForComparisonException")));
    }

    @Test
    public void testDifferentiateNotExistingNode() throws Exception {
        String base64Json = "eyJhbmltYWwiOiJkb2cifQ==";
        Long nodeId = 5l;

        mockMvc.perform(
                get("/v1/diff/" + nodeId)
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(base64Json))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.is("NodeNotExistsException")));
    }

}
