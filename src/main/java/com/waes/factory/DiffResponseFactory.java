package com.waes.factory;

import com.waes.dto.DiffResponse;
import com.waes.dto.DiffResponseDetail;
import com.waes.model.Node;
import org.skyscreamer.jsonassert.JSONCompareResult;

public final class DiffResponseFactory {

    public static final DiffResponseDetail buildForDifferentData(String message, Node node) {
        JSONCompareResult l = node.getDifferences();
        String messageDetail = l.getMessage().replaceAll("\\r\\n", "");
        DiffResponseDetail someResponse = new DiffResponseDetail(message, messageDetail);
        return someResponse;
    }

    public static final DiffResponse buildForEqualData(String message) {
        DiffResponse someResponse = new DiffResponse(message);
        return someResponse;
    }
}
