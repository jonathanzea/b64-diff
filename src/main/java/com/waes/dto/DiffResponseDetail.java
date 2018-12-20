package com.waes.dto;

import org.skyscreamer.jsonassert.FieldComparisonFailure;

import java.util.List;

public final class DiffResponseDetail extends DiffResponse {

    private final String differenceDetail;

    public DiffResponseDetail(String message, String differenceDetail) {
        super(message);
        this.differenceDetail = differenceDetail;
    }

    public String getDifferenceDetail() {
        return differenceDetail;
    }
}
