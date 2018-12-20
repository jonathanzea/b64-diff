package com.waes.service;

import com.waes.exception.EmptyDataForNodeException;
import com.waes.exception.JsonMalformedException;
import com.waes.exception.NodeDataAlreadyAddedException;
import com.waes.exception.NodeDataEmptyForComparisonException;
import com.waes.model.Node;
import com.waes.repository.NodesRepository;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class B64ServiceTest {

    private B64Service b64Service;
    private NodesRepository nodesRepository;
    private String decodedJsonExpectedData;
    private String encodedJson;
    private String encodedMalformedJson;
    private String encodedJson2;

    @Before
    public void setup() {
        nodesRepository = new NodesRepository();
        b64Service = new B64Service(nodesRepository);

        decodedJsonExpectedData = "{\"json_key\":\"json_value\"}";
        encodedJson = "eyJqc29uX2tleSI6Impzb25fdmFsdWUifQ=="; //{"json_key":"json_value"}
        encodedJson2 = "eyJqc29uX2tleSI6Impzb25fZGlmZmVyZW50X3ZhbHVlIn0="; //{"json_key":"json_different_value"}
        encodedMalformedJson = "eyJqc29uLSJqc29uIn0="; //{"json-"json"}
    }

    @After
    public void tearDown() {
        nodesRepository.clearNodeMap();
    }

    @Test
    public void testDecodeAndCompare() throws JSONException {
        String decodedJson = b64Service.decodeAndCheck(encodedJson);
        JSONAssert.assertEquals(
                decodedJson, decodedJsonExpectedData, JSONCompareMode.STRICT);
    }

    @Test(expected = JsonMalformedException.class)
    public void testDecodeAndCompareThrowJsonMalformedException() {
        b64Service.decodeAndCheck(encodedMalformedJson);
    }

    @Test
    public void testRegisterNodeLeftData() {
        b64Service.registerLeftNodeData(encodedJson, 7l);
        Node node = nodesRepository.findNodeById(7l);
        assertThat(node).isNotNull();
        assertThat(node.hasLeftData());
        assertThat(node.hasRightData()).isFalse();
    }

    @Test(expected = NodeDataAlreadyAddedException.class)
    public void testRegisterNodeLeftDataThrowNodeDataAlreadyAddedException() {
        b64Service.registerLeftNodeData(encodedJson, 1l);
        b64Service.registerLeftNodeData(encodedJson, 1l);
    }

    @Test(expected = EmptyDataForNodeException.class)
    public void testRegisterNullNodeLeftData() {
        b64Service.registerLeftNodeData(null, 1l);
    }

    @Test(expected = EmptyDataForNodeException.class)
    public void testRegisterNullNodeRightData() {
        b64Service.registerRightNodeData(null, 1l);
    }

    @Test
    public void testRegisterNodeRightData() {
        b64Service.registerRightNodeData(encodedJson, 2l);
        Node node = nodesRepository.findNodeById(2l);
        assertThat(node).isNotNull();
        assertThat(node.hasRightData());
        assertThat(node.hasLeftData()).isFalse();
    }

    @Test
    public void testRegisterNodeLeftAndRightData() {
        b64Service.registerRightNodeData(encodedJson, 3l);
        b64Service.registerLeftNodeData(encodedJson, 3l);

        Node node = nodesRepository.findNodeById(3l);
        assertThat(node).isNotNull();
        assertThat(node.hasLeftData());
        assertThat(node.hasRightData());
    }

    @Test(expected = RuntimeException.class)
    public void testClearNodes() {
        b64Service.registerRightNodeData(encodedJson, 3l);
        b64Service.registerLeftNodeData(encodedJson, 3l);

        Node node = nodesRepository.findNodeById(3l);
        assertThat(node).isNotNull();
        assertThat(node.hasLeftData());
        assertThat(node.hasRightData());

        b64Service.clearNodesMap();
        assertThat(nodesRepository.findNodeById(3l)).isNull();
    }


    @Test
    public void testCompareEqualsNodeData() {
        b64Service.registerRightNodeData(encodedJson, 5l);
        b64Service.registerLeftNodeData(encodedJson, 5l);

        Node node = nodesRepository.findNodeById(5l);
        assertThat(node.hasEqualData()).isTrue();
    }

    @Test
    public void testCompareNodeDifferentData() {
        b64Service.registerRightNodeData(encodedJson, 6l);
        b64Service.registerLeftNodeData(encodedJson2, 6l);

        Node node = nodesRepository.findNodeById(6l);
        assertThat(node.hasEqualData()).isFalse();
    }

    @Test(expected = NodeDataEmptyForComparisonException.class)
    public void testCompareNodeData() {
        b64Service.registerRightNodeData(encodedJson, 4l);
        Node node = nodesRepository.findNodeById(4l);
        node.hasEqualData();
    }


}
