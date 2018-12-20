package com.waes.model;

import com.waes.exception.NodeDataEmptyForComparisonException;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

public final class Node {
    private String leftData;
    private String rightData;


    public void addLeftData(String leftData) {
        this.leftData = leftData;
    }

    public void addRightData(String rightData) {
        this.rightData = rightData;
    }

    public boolean hasLeftData() {
        return leftData != null;
    }

    public boolean hasRightData() {
        return rightData != null;
    }

    public String getLeftData() {
        return leftData;
    }

    public String getRightData() {
        return rightData;
    }

    public boolean hasEqualData() {
        if (rightData != null && leftData != null) {
            return compareData();
        }
        throw new NodeDataEmptyForComparisonException("Left and Right data of the node must be added to compare");
    }

    private boolean compareData() {
        JSONCompareResult result = null;
        try {
            result =
                    JSONCompare.compareJSON(leftData, rightData, JSONCompareMode.STRICT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.passed();
    }

    public JSONCompareResult getDifferences() {
        JSONCompareResult result = null;
        try {
            result =
                    JSONCompare.compareJSON(leftData, rightData, JSONCompareMode.STRICT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}

