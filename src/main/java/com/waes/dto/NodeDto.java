package com.waes.dto;

import com.waes.model.Node;

public final class NodeDto {
    private Long nodeId;
    private String rightData = "EMPTY";
    private String leftData= "EMPTY";


    public Long getNodeId() {
        return nodeId;
    }

    public String getRightData() {
        return rightData;
    }

    public String getLeftData() {
        return leftData;
    }

    public void configureDetail(Long id, Node node) {
        nodeId = id;

        if(node.hasRightData()){
            rightData = "POPULATED";
        }
        if(node.hasLeftData()){
            leftData = "POPULATED";
        }
    }
}
