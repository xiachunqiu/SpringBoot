package com.x.y.dto;

import com.x.y.common.ResponseCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseData implements Serializable {
    private boolean success;
    private Object data;
    private String msg;
    private String code;

    public static ResponseData okData(Object data) {
        return new ResponseData(data);
    }

    public static ResponseData ok() {
        ResponseData responseData = new ResponseData(null);
        responseData.setCode(ResponseCode.success);
        responseData.setMsg("success");
        responseData.setSuccess(true);
        return responseData;
    }

    public static ResponseData ok(String msg, Object data) {
        ResponseData responseData = new ResponseData(null);
        responseData.setCode(ResponseCode.success);
        responseData.setMsg(msg);
        responseData.setData(data);
        responseData.setSuccess(true);
        return responseData;
    }

    public static ResponseData fail() {
        ResponseData responseData = new ResponseData(null);
        responseData.setCode(ResponseCode.fail);
        responseData.setMsg("fail");
        responseData.setSuccess(false);
        return responseData;
    }

    public static ResponseData fail(String msg) {
        ResponseData responseData = new ResponseData(null);
        responseData.setCode(ResponseCode.fail);
        responseData.setMsg(msg);
        responseData.setSuccess(false);
        return responseData;
    }

    public static ResponseData fail(String msg, String code) {
        ResponseData responseData = new ResponseData(null);
        responseData.setCode(code);
        responseData.setMsg(msg);
        responseData.setSuccess(false);
        return responseData;
    }

    private ResponseData(Object data) {
        super();
        this.setData(data);
        this.setCode(ResponseCode.success);
        this.setMsg("success");
        this.setSuccess(true);
    }

    private ResponseData(String msg) {
        super();
        this.setMsg(msg);
        this.setCode(ResponseCode.success);
        this.setSuccess(true);
    }
}