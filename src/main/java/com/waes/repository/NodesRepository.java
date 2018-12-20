package com.waes.repository;

import com.waes.exception.NodeNotExistsException;
import com.waes.model.Node;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public final class NodesRepository {

    private static final Map<Long, Node> nodesMap = new ConcurrentHashMap<>();

    public boolean nodeNotExists(Long nodeID) {
        return !nodesMap.containsKey(nodeID);
    }

    public boolean nodeHasEmptyLeftData(Long nodeId) {
        return !nodesMap.get(nodeId).hasLeftData();
    }

    public boolean nodeHasEmptyRightData(Long nodeId) {
        return !nodesMap.get(nodeId).hasRightData();
    }

    public void addLeftDataToNode(Long id, String data) {
        nodesMap.get(id).addLeftData(data);

    }

    public void addRightDataToNode(Long id, String data) {
        nodesMap.get(id).addRightData(data);

    }

    public void addNode(Long id, Node node) {
        nodesMap.put(id, node);
    }

    public Node findNodeById(Long id) {
        if (!nodesMap.containsKey(id)) {
            throw new NodeNotExistsException("Node id: " + id + " does not exist");
        }
        return nodesMap.get(id);
    }

    public void clearNodeMap() {
        nodesMap.clear();
    }
}
