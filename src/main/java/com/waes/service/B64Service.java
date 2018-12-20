package com.waes.service;

import com.waes.dto.DiffResponse;
import com.waes.exception.EmptyDataForNodeException;
import com.waes.exception.JsonMalformedException;
import com.waes.exception.NodeDataAlreadyAddedException;
import com.waes.factory.DiffResponseFactory;
import com.waes.model.Node;
import com.waes.repository.NodesRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Service
public final class B64Service {

    private final NodesRepository nodesRepository;
    private static final String CHAR_SET_NAME = "utf-8";

    @Autowired
    public B64Service(NodesRepository nodesRepository){
        this.nodesRepository = nodesRepository;
    }


    public void registerLeftNodeData(final String jsonBase64Binary, final Long id) {
        String decodedJsonString = decodeAndCheck(jsonBase64Binary);

        if (nodesRepository.nodeNotExists(id)) {
            Node node = new Node();
            node.addLeftData(decodedJsonString);
            nodesRepository.addNode(id,node);
        } else if (nodesRepository.nodeHasEmptyLeftData(id)) {
            nodesRepository.addLeftDataToNode(id,decodedJsonString);
        } else {
            throw new NodeDataAlreadyAddedException("Left data for node id: {} already added. Please Add a new node.");
        }
    }

    public void registerRightNodeData(final String jsonBase64Binary, final Long id) {
        String decodedJsonString = decodeAndCheck(jsonBase64Binary);

        if (nodesRepository.nodeNotExists(id)) {
            Node node = new Node();
            node.addRightData(decodedJsonString);
            nodesRepository.addNode(id,node);
        } else if (nodesRepository.nodeHasEmptyRightData(id)) {
            nodesRepository.addRightDataToNode(id,decodedJsonString);
        } else {
            throw new NodeDataAlreadyAddedException("Right data for node id: {} already added. Please Add a new node.");
        }
    }

    public void clearNodesMap() {
        nodesRepository.clearNodeMap();
    }

    public String decodeAndCheck(final String jsonBase64Binary) {
        validateEmptyData(jsonBase64Binary);
        String decodedData = decodeData(jsonBase64Binary);
        validateDecodedJsonConsistency(decodedData);
        return decodedData;
    }

    private String decodeData(final String jsonBase64Binary) {
        byte[] base64decodedBytes = Base64.getDecoder().decode(jsonBase64Binary);
        String decodedData = null;
        try {
            decodedData = new String(base64decodedBytes, CHAR_SET_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
        return decodedData;
    }

    private void validateDecodedJsonConsistency(final String decodedData) {
        try {
            new JSONObject(decodedData);
        } catch (JSONException ex) {
            throw new JsonMalformedException("Json Malformed - Check the json structure");
        }
    }


    private void validateEmptyData(final String jsonBase64Binary) {
        if (jsonBase64Binary == null || jsonBase64Binary.isEmpty()) {
            throw new EmptyDataForNodeException("Data for node can not be null or empty");
        }
    }

    public DiffResponse compareNodeDataById(final Long id) {
        Node node = nodesRepository.findNodeById(id);
        if (node.hasEqualData()) {
            return DiffResponseFactory.buildForEqualData("EQUAL DATA");
        } else {
            return DiffResponseFactory.buildForDifferentData("DIFFERENT DATA", node);
        }
    }
}
