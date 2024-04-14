package com.a601.refesta.common.response;


public class SuccessResponse<T> extends BaseResponse<T> {

    public SuccessResponse(T data) {
        super("success", data);
    }
}
